package com.symbioticsurvival.worldgen;

import com.mojang.serialization.Codec;
import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.PollinatorNestBlock;
import com.symbioticsurvival.block.SpecialTreeBlock;
import com.symbioticsurvival.block.entity.PollinatorNestBlockEntity;
import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import com.symbioticsurvival.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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

        // Generate the tree structure
        if (!generateSimpleTree(world, treePos, pair.treeBlock, pair.biomeType, random)) {
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
     * Generate a simple tree structure with trunk and foliage.
     * Tree size and shape vary by biome type.
     */
    private boolean generateSimpleTree(StructureWorldAccess world, BlockPos basePos,
                                      net.minecraft.block.Block treeBlock, String biomeType, Random random) {
        // Determine tree dimensions based on biome type
        int trunkHeight;
        int canopyRadius;
        BlockState foliageState;

        switch (biomeType) {
            case "desert" -> {
                // Yucca: Short, thin plant
                trunkHeight = 2 + random.nextInt(2); // 2-3 blocks
                canopyRadius = 1;
                foliageState = ModBlocks.YUCCA_LEAVES.getDefaultState();
            }
            case "tropical" -> {
                // Fig tree: Tall with wide canopy
                trunkHeight = 5 + random.nextInt(3); // 5-7 blocks
                canopyRadius = 2;
                foliageState = ModBlocks.FIG_LEAVES.getDefaultState();
            }
            case "savanna" -> {
                // Acacia variant: Medium height, wide canopy
                trunkHeight = 4 + random.nextInt(2); // 4-5 blocks
                canopyRadius = 2;
                foliageState = ModBlocks.ACACIA_VARIANT_LEAVES.getDefaultState();
            }
            case "taiga" -> {
                // Conifer: Tall and narrow
                trunkHeight = 6 + random.nextInt(3); // 6-8 blocks
                canopyRadius = 1;
                foliageState = ModBlocks.CONIFER_VARIANT_LEAVES.getDefaultState();
            }
            case "plains" -> {
                // Milkweed: Very short, more like a tall flower
                trunkHeight = 1 + random.nextInt(2); // 1-2 blocks
                canopyRadius = 0; // No canopy, just the plant itself
                foliageState = ModBlocks.MILKWEED_LEAVES.getDefaultState();
            }
            case "swamp" -> {
                // Mangrove variant: Medium with hanging leaves
                trunkHeight = 4 + random.nextInt(2); // 4-5 blocks
                canopyRadius = 2;
                foliageState = ModBlocks.MANGROVE_VARIANT_LEAVES.getDefaultState();
            }
            case "mushroom" -> {
                // Glowing mushroom: Short, wide cap
                trunkHeight = 3 + random.nextInt(2); // 3-4 blocks
                canopyRadius = 2;
                foliageState = ModBlocks.GLOWING_MUSHROOM_LEAVES.getDefaultState();
            }
            case "birch_forest" -> {
                // Flowering birch: Tall, narrow
                trunkHeight = 5 + random.nextInt(2); // 5-6 blocks
                canopyRadius = 1;
                foliageState = ModBlocks.FLOWERING_BIRCH_LEAVES.getDefaultState();
            }
            case "cherry_grove" -> {
                // Enhanced cherry: Medium with pink leaves
                trunkHeight = 4 + random.nextInt(2); // 4-5 blocks
                canopyRadius = 2;
                foliageState = ModBlocks.ENHANCED_CHERRY_LEAVES.getDefaultState();
            }
            case "snowy" -> {
                // Arctic willow: Short, bushy
                trunkHeight = 3 + random.nextInt(2); // 3-4 blocks
                canopyRadius = 1;
                foliageState = ModBlocks.ARCTIC_WILLOW_LEAVES.getDefaultState();
            }
            default -> {
                // Default tree
                trunkHeight = 4 + random.nextInt(2);
                canopyRadius = 1;
                foliageState = ModBlocks.FIG_LEAVES.getDefaultState();
            }
        }

        // Place trunk blocks
        BlockState trunkState = treeBlock.getDefaultState()
            .with(PillarBlock.AXIS, Direction.Axis.Y)
            .with(SpecialTreeBlock.FRUIT_STATE, 0);

        for (int y = 0; y < trunkHeight; y++) {
            BlockPos trunkPos = basePos.up(y);

            if (!world.setBlockState(trunkPos, trunkState, 3)) {
                return false; // Failed to place trunk
            }
        }

        // Place canopy (if radius > 0)
        if (canopyRadius > 0) {
            BlockPos canopyCenter = basePos.up(trunkHeight);

            for (int x = -canopyRadius; x <= canopyRadius; x++) {
                for (int z = -canopyRadius; z <= canopyRadius; z++) {
                    for (int y = -1; y <= 1; y++) {
                        BlockPos leafPos = canopyCenter.add(x, y, z);

                        // Skip center column (where trunk is)
                        if (x == 0 && z == 0 && y <= 0) continue;

                        // Simple distance check for rounded canopy
                        double distance = Math.sqrt(x * x + z * z);
                        if (distance <= canopyRadius + 0.5) {
                            // Only place if air or replaceable
                            if (world.getBlockState(leafPos).isAir() ||
                                world.getBlockState(leafPos).isReplaceable()) {

                                // Calculate distance from this leaf to the tree trunk
                                int leafDistance = calculateDistanceToTrunk(leafPos, basePos, trunkHeight);

                                // Set the distance property (1-6 are valid, 7 means decay)
                                BlockState leafStateWithDistance = foliageState
                                    .with(LeavesBlock.DISTANCE, Math.min(leafDistance, 6))
                                    .with(LeavesBlock.PERSISTENT, false);

                                world.setBlockState(leafPos, leafStateWithDistance, 3);

                                // Schedule a block update to ensure decay system registers it properly
                                world.scheduleBlockTick(leafPos, leafStateWithDistance.getBlock(), 1);
                            }
                        }
                    }
                }
            }
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

    /**
     * Calculate the Manhattan distance from a leaf position to the nearest trunk block.
     * This is used to set the DISTANCE property on leaves so they don't decay.
     */
    private int calculateDistanceToTrunk(BlockPos leafPos, BlockPos basePos, int trunkHeight) {
        int minDistance = Integer.MAX_VALUE;

        // Check distance to each trunk block
        for (int y = 0; y < trunkHeight; y++) {
            BlockPos trunkPos = basePos.up(y);
            int distance = Math.abs(leafPos.getX() - trunkPos.getX()) +
                          Math.abs(leafPos.getY() - trunkPos.getY()) +
                          Math.abs(leafPos.getZ() - trunkPos.getZ());
            minDistance = Math.min(minDistance, distance);
        }

        return minDistance;
    }
}
