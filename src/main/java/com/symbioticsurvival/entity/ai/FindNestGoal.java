package com.symbioticsurvival.entity.ai;

import com.symbioticsurvival.block.PollinatorNestBlock;
import com.symbioticsurvival.entity.HoneyguideEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

/**
 * AI goal for Honeyguide to search for nearby pollinator nests.
 */
public class FindNestGoal extends Goal {

    private final HoneyguideEntity honeyguide;
    private BlockPos foundNest;
    private int searchCooldown;
    private static final int SEARCH_RADIUS = 32;
    private static final int SEARCH_INTERVAL = 200; // 10 seconds

    public FindNestGoal(HoneyguideEntity honeyguide) {
        this.honeyguide = honeyguide;
        this.setControls(EnumSet.of(Control.MOVE));
        this.searchCooldown = SEARCH_INTERVAL;
    }

    @Override
    public boolean canStart() {
        // Don't search if already leading or on cooldown
        if (honeyguide.isLeading()) return false;
        if (searchCooldown > 0) {
            searchCooldown--;
            return false;
        }

        // Search for nearby nests
        foundNest = findNearestNest();
        return foundNest != null;
    }

    @Override
    public void start() {
        // Found a nest, now need to attract a player
        searchCooldown = SEARCH_INTERVAL;
    }

    @Override
    public boolean shouldContinue() {
        return false; // This is just a search goal, doesn't have continuous behavior
    }

    private BlockPos findNearestNest() {
        World world = honeyguide.getEntityWorld();
        BlockPos honeyguidePos = honeyguide.getBlockPos();

        // Search in a cube around the honeyguide
        for (int x = -SEARCH_RADIUS; x <= SEARCH_RADIUS; x++) {
            for (int y = -SEARCH_RADIUS / 2; y <= SEARCH_RADIUS / 2; y++) {
                for (int z = -SEARCH_RADIUS; z <= SEARCH_RADIUS; z++) {
                    BlockPos checkPos = honeyguidePos.add(x, y, z);

                    if (world.getBlockState(checkPos).getBlock() instanceof PollinatorNestBlock) {
                        return checkPos;
                    }
                }
            }
        }

        return null;
    }

    public BlockPos getFoundNest() {
        return foundNest;
    }
}
