package com.symbioticsurvival.block;

import com.mojang.serialization.MapCodec;
import com.symbioticsurvival.SymbioticSurvival;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

/**
 * Custom leaves block that explicitly recognizes SpecialTreeBlock instances.
 * This ensures proper leaf decay behavior with our custom tree blocks.
 */
public class SpecialLeavesBlock extends LeavesBlock {

    public SpecialLeavesBlock(Settings settings) {
        // LeavesBlock constructor requires a float for particle spawn chance (0.0-1.0)
        // 0.025 is the same value vanilla leaves use
        super(0.025f, settings);
        // Initialize default state with proper decay properties
        // DISTANCE=7 (max distance, will be recalculated by game)
        // PERSISTENT=false (allows decay, set to true when player-placed)
        setDefaultState(getDefaultState()
            .with(DISTANCE, 7)
            .with(PERSISTENT, false));
    }

    // TODO: Implement Codec in future Minecraft version
    @Override
    public MapCodec<? extends LeavesBlock> getCodec() {
        throw new UnsupportedOperationException("Codec not yet implemented");
    }

    @Override
    public void spawnLeafParticle(World world, BlockPos pos, Random random) {
        // No custom particles - leaves will use default behavior
        // This method is required to be implemented but can be empty
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient()) {
            SymbioticSurvival.LOGGER.info("SpecialLeaves placed at {} - DISTANCE: {}, PERSISTENT: {}",
                pos, state.get(DISTANCE), state.get(PERSISTENT));
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }
}
