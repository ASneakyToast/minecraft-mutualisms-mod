package com.symbioticsurvival.entity.ai;

import com.symbioticsurvival.block.PollinatorNestBlock;
import com.symbioticsurvival.entity.pollinator.BasePollinatorEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

/**
 * AI goal for pollinators to return to their nest at night or after pollinating.
 */
public class ReturnToNestGoal extends Goal {

    private final BasePollinatorEntity pollinator;
    private BlockPos targetNest;
    private static final double ARRIVAL_DISTANCE = 1.5;

    public ReturnToNestGoal(BasePollinatorEntity pollinator) {
        this.pollinator = pollinator;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        // Return to nest at night
        World world = pollinator.getEntityWorld();
        if (!world.isNight()) return false;

        BlockPos linkedNest = pollinator.getLinkedNest();
        if (linkedNest == null) return false;

        // Check if nest still exists
        if (world.getBlockState(linkedNest).getBlock() instanceof PollinatorNestBlock) {
            this.targetNest = linkedNest;
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (targetNest == null) return false;

        // Stop when we arrive at nest
        double distance = pollinator.getBlockPos().getSquaredDistance(targetNest);
        return distance > ARRIVAL_DISTANCE * ARRIVAL_DISTANCE;
    }

    @Override
    public void tick() {
        if (targetNest == null) return;

        // Navigate to nest
        pollinator.getNavigation().startMovingTo(
            targetNest.getX() + 0.5,
            targetNest.getY() + 0.5,
            targetNest.getZ() + 0.5,
            1.0
        );
    }

    @Override
    public void stop() {
        targetNest = null;
    }
}
