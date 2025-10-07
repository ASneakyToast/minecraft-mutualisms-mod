package com.symbioticsurvival.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.entity.PollinatorNestBlockEntity;
import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.List;

/**
 * TreeDecorator that places a pollinator nest near a tree and links them together.
 * This enables both sapling-grown and world-generated trees to have linked nests.
 */
public class NestLinkingDecorator extends TreeDecorator {

    public static final MapCodec<NestLinkingDecorator> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Registries.BLOCK.getCodec().fieldOf("nest_block").forGetter(decorator -> decorator.nestBlock),
            Codec.STRING.fieldOf("biome_type").forGetter(decorator -> decorator.biomeType),
            Codec.INT.fieldOf("max_distance").forGetter(decorator -> decorator.maxDistance),
            Codec.INT.fieldOf("placement_attempts").forGetter(decorator -> decorator.placementAttempts)
        ).apply(instance, NestLinkingDecorator::new)
    );

    private final Block nestBlock;
    private final String biomeType;
    private final int maxDistance;
    private final int placementAttempts;

    public NestLinkingDecorator(Block nestBlock, String biomeType, int maxDistance, int placementAttempts) {
        this.nestBlock = nestBlock;
        this.biomeType = biomeType;
        this.maxDistance = maxDistance;
        this.placementAttempts = placementAttempts;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return ModTreeDecoratorTypes.NEST_LINKING;
    }

    @Override
    public void generate(Generator generator) {
        Random random = generator.getRandom();

        // Get the tree's log positions - the base is typically the first one
        List<BlockPos> logs = generator.getLogPositions();
        if (logs.isEmpty()) {
            return;
        }

        // Use the lowest log position as the tree base
        BlockPos treeBase = logs.stream()
            .min((a, b) -> Integer.compare(a.getY(), b.getY()))
            .orElse(logs.get(0));

        // Try to find a suitable nest position
        BlockPos nestPos = findSuitableNestPosition(generator, treeBase, random);
        if (nestPos == null) {
            return;
        }

        // Place the nest
        BlockState nestState = nestBlock.getDefaultState();
        generator.replace(nestPos, nestState);

        // Link the tree and nest (this happens after generation, so we need to schedule it)
        // Note: We can't directly access block entities during generation, so we'll need
        // to handle this differently. For now, the linking will happen via the block entities'
        // tick methods when they detect each other.

        if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
            SymbioticSurvival.LOGGER.info(
                "NestLinkingDecorator placed nest at {} near tree at {}, biome={}",
                nestPos, treeBase, biomeType
            );
        }
    }

    /**
     * Find a suitable position for the nest near the tree.
     */
    private BlockPos findSuitableNestPosition(Generator generator, BlockPos treeBase, Random random) {
        for (int attempt = 0; attempt < placementAttempts; attempt++) {
            int offsetX = random.nextInt(maxDistance * 2 + 1) - maxDistance;
            int offsetZ = random.nextInt(maxDistance * 2 + 1) - maxDistance;

            // Search within a few blocks vertically
            for (int offsetY = -2; offsetY <= 2; offsetY++) {
                BlockPos testPos = treeBase.add(offsetX, offsetY, offsetZ);
                BlockPos belowPos = testPos.down();

                // Check if the position is suitable (air/replaceable above solid ground)
                if (generator.isAir(testPos) &&
                    !generator.isAir(belowPos)) {
                    return testPos;
                }
            }
        }

        return null;
    }
}
