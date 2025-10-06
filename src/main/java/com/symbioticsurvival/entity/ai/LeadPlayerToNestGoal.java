package com.symbioticsurvival.entity.ai;

import com.symbioticsurvival.entity.HoneyguideEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

/**
 * AI goal for Honeyguide to lead a player to a discovered nest.
 */
public class LeadPlayerToNestGoal extends Goal {

    private final HoneyguideEntity honeyguide;
    private PlayerEntity targetPlayer;
    private BlockPos targetNest;
    private int waitTicks;
    private static final int WAIT_TIME = 40; // 2 seconds between movements
    private static final double PLAYER_FOLLOW_DISTANCE = 8.0;
    private static final double NEST_ARRIVAL_DISTANCE = 3.0;

    public LeadPlayerToNestGoal(HoneyguideEntity honeyguide) {
        this.honeyguide = honeyguide;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!honeyguide.isLeading()) return false;

        this.targetNest = honeyguide.getTargetNest();
        if (targetNest == null) return false;

        // Find the player
        if (honeyguide.getTargetPlayer() != null) {
            this.targetPlayer = honeyguide.getEntityWorld()
                .getPlayerByUuid(honeyguide.getTargetPlayer());
        }

        return targetPlayer != null && targetPlayer.isAlive();
    }

    @Override
    public boolean shouldContinue() {
        if (targetPlayer == null || !targetPlayer.isAlive()) return false;
        if (targetNest == null) return false;

        // Stop if player reached nest
        double distanceToNest = targetPlayer.getBlockPos().getSquaredDistance(targetNest);
        return distanceToNest > NEST_ARRIVAL_DISTANCE * NEST_ARRIVAL_DISTANCE;
    }

    @Override
    public void tick() {
        if (targetPlayer == null || targetNest == null) return;

        waitTicks++;

        // Wait between movements to give player time to follow
        if (waitTicks < WAIT_TIME) return;

        // Check if player is following
        double distanceToPlayer = honeyguide.squaredDistanceTo(targetPlayer);

        if (distanceToPlayer > PLAYER_FOLLOW_DISTANCE * PLAYER_FOLLOW_DISTANCE) {
            // Player too far, wait for them
            honeyguide.getNavigation().stop();
            honeyguide.getLookControl().lookAt(targetPlayer);

            // Emit a sound periodically to call the player
            if (waitTicks % 60 == 0) {
                honeyguide.playAmbientSound();
            }
        } else {
            // Player is close, lead toward nest
            double distanceToNest = honeyguide.getBlockPos().getSquaredDistance(targetNest);

            if (distanceToNest > 4) {
                // Move toward nest
                honeyguide.getNavigation().startMovingTo(
                    targetNest.getX() + 0.5,
                    targetNest.getY() + 0.5,
                    targetNest.getZ() + 0.5,
                    1.0
                );
            } else {
                // Near nest, circle around it
                honeyguide.getNavigation().stop();
                honeyguide.getLookControl().lookAt(
                    targetNest.getX() + 0.5,
                    targetNest.getY() + 0.5,
                    targetNest.getZ() + 0.5
                );
            }

            waitTicks = 0;
        }
    }

    @Override
    public void stop() {
        // Player reached nest or gave up
        honeyguide.abandonLeading();
        targetPlayer = null;
        targetNest = null;
        waitTicks = 0;
    }
}
