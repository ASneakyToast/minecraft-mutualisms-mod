package com.symbioticsurvival.worldgen;

import com.mojang.serialization.Codec;
import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.PollinatorNestBlock;
import com.symbioticsurvival.block.SpecialTreeBlock;
import com.symbioticsurvival.block.entity.PollinatorNestBlockEntity;
import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * World generation feature that places tree-nest pairs in appropriate biomes.
 * Trees and nests are linked during generation.
 */
public class BiomePairFeature extends Feature<BiomePairConfig> {

    public BiomePairFeature(Codec<BiomePairConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<BiomePairConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        BiomePairConfig config = context.getConfig();

        // Get the biome at this position
        RegistryEntry<Biome> biomeEntry = world.getBiome(origin);
        var biomeKey = biomeEntry.getKey();

        if (biomeKey.isEmpty()) {
            return false;
        }

        // Get the appropriate blocks for this biome
        BiomePairRegistry.BiomePair pair = BiomePairRegistry.getPairForBiome(biomeKey.get());

        if (pair == null) {
            // No special blocks for this biome
            return false;
        }

        // Find suitable ground for tree placement
        BlockPos treePos = findSuitableTreePosition(world, origin, random);
        if (treePos == null) {
            return false;
        }

        // Place the tree
        BlockState treeState = pair.treeBlock.getDefaultState()
            .with(SpecialTreeBlock.FRUIT_STATE, 0); // Start with immature fruit

        if (!world.setBlockState(treePos, treeState, 3)) {
            return false;
        }

        // Find suitable position for nest (within configured distance of tree)
        BlockPos nestPos = findSuitableNestPosition(world, treePos, random, config.nestMaxDistance(), config.placementAttempts());
        if (nestPos == null) {
            // Tree placed but no nest - still a partial success
            return true;
        }

        // Determine if this is a defensive nest based on biome type
        boolean isDefensive = isDefensiveBiome(pair.biomeType);

        // Place the nest
        BlockState nestState = pair.nestBlock.getDefaultState();
        if (!world.setBlockState(nestPos, nestState, 3)) {
            return true; // Tree already placed
        }

        // Link the tree and nest
        linkTreeAndNest(world, treePos, nestPos, pair.biomeType);

        if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
            SymbioticSurvival.LOGGER.info(
                "Generated tree-nest pair at tree={}, nest={}, biome={}",
                treePos, nestPos, pair.biomeType
            );
        }

        return true;
    }

    /**
     * Find a suitable position for tree placement
     */
    private BlockPos findSuitableTreePosition(StructureWorldAccess world, BlockPos origin, Random random) {
        // Search in a small area around the origin
        for (int attempts = 0; attempts < 4; attempts++) {
            int offsetX = random.nextInt(8) - 4;
            int offsetZ = random.nextInt(8) - 4;

            BlockPos testPos = origin.add(offsetX, 0, offsetZ);

            // Find the surface
            BlockPos surfacePos = world.getTopPosition(net.minecraft.world.Heightmap.Type.WORLD_SURFACE_WG, testPos);

            // Check if the block below is solid (soil/ground)
            BlockPos belowPos = surfacePos.down();
            if (world.getBlockState(belowPos).isSolidBlock(world, belowPos) &&
                world.getBlockState(surfacePos).isAir()) {
                return surfacePos;
            }
        }

        return null;
    }

    /**
     * Find a suitable position for nest placement near the tree
     */
    private BlockPos findSuitableNestPosition(StructureWorldAccess world, BlockPos treePos, Random random, int maxDistance, int maxAttempts) {
        for (int attempts = 0; attempts < maxAttempts; attempts++) {
            int offsetX = random.nextInt(maxDistance * 2) - maxDistance;
            int offsetZ = random.nextInt(maxDistance * 2) - maxDistance;

            BlockPos testPos = treePos.add(offsetX, 0, offsetZ);

            // Find the surface
            BlockPos surfacePos = world.getTopPosition(net.minecraft.world.Heightmap.Type.WORLD_SURFACE_WG, testPos);

            // Check if there's a solid block below and air above
            BlockPos belowPos = surfacePos.down();
            if (world.getBlockState(belowPos).isSolidBlock(world, belowPos) &&
                world.getBlockState(surfacePos).isAir()) {
                return surfacePos;
            }
        }

        return null;
    }

    /**
     * Link tree and nest block entities
     */
    private void linkTreeAndNest(StructureWorldAccess world, BlockPos treePos, BlockPos nestPos, String biomeType) {
        // Link tree to nest
        if (world.getBlockEntity(treePos) instanceof SpecialTreeBlockEntity treeEntity) {
            treeEntity.linkToNest(nestPos, biomeType);
        }

        // Link nest to tree
        if (world.getBlockEntity(nestPos) instanceof PollinatorNestBlockEntity nestEntity) {
            nestEntity.linkToTree(treePos);
        }
    }

    /**
     * Determine if a biome type should spawn defensive pollinators
     */
    private boolean isDefensiveBiome(String biomeType) {
        return switch (biomeType) {
            case "tropical", "savanna" -> true; // Fig wasps and mason wasps are defensive
            default -> false;
        };
    }
}
