package com.symbioticsurvival.entity.ai;

import com.symbioticsurvival.entity.HoneyguideEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;
import java.util.List;

/**
 * AI goal for Honeyguide to call nearby players when a nest is found.
 */
public class CallPlayerGoal extends Goal {

    private final HoneyguideEntity honeyguide;
    private final FindNestGoal findNestGoal;
    private PlayerEntity targetPlayer;
    private int callingTicks;
    private static final int CALL_DURATION = 100; // 5 seconds
    private static final double CALL_RADIUS = 16.0;

    public CallPlayerGoal(HoneyguideEntity honeyguide, FindNestGoal findNestGoal) {
        this.honeyguide = honeyguide;
        this.findNestGoal = findNestGoal;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        // Only call if a nest was found and not already leading
        if (honeyguide.isLeading()) return false;
        if (findNestGoal.getFoundNest() == null) return false;

        // Find nearest player
        List<? extends PlayerEntity> players = honeyguide.getEntityWorld().getPlayers();
        PlayerEntity nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (PlayerEntity player : players) {
            double distance = honeyguide.squaredDistanceTo(player);
            if (distance < CALL_RADIUS * CALL_RADIUS && distance < nearestDistance) {
                nearest = player;
                nearestDistance = distance;
            }
        }

        if (nearest != null) {
            this.targetPlayer = nearest;
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldContinue() {
        return callingTicks < CALL_DURATION &&
               targetPlayer != null &&
               targetPlayer.isAlive() &&
               !honeyguide.isLeading();
    }

    @Override
    public void start() {
        callingTicks = 0;
    }

    @Override
    public void tick() {
        callingTicks++;

        // Look at player and make noise
        if (targetPlayer != null) {
            honeyguide.getLookControl().lookAt(targetPlayer);

            // Call every second
            if (callingTicks % 20 == 0) {
                honeyguide.playAmbientSound();
            }
        }

        // If player gets close, start leading
        if (targetPlayer != null && honeyguide.squaredDistanceTo(targetPlayer) < 9.0) {
            honeyguide.startLeading(targetPlayer, findNestGoal.getFoundNest());
        }
    }

    @Override
    public void stop() {
        callingTicks = 0;
        targetPlayer = null;
    }
}
