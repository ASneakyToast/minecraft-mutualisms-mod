package com.symbioticsurvival.entity.ai;

import com.symbioticsurvival.block.PollinatorNestBlock;
import com.symbioticsurvival.entity.HoneyguideEntity;
import com.symbioticsurvival.registry.ModPointOfInterest;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestStorage;

import java.util.EnumSet;

/**
 * AI goal for Honeyguide to search for nearby pollinator nests.
 * Uses the POI (Point of Interest) system for efficient searching.
 */
public class FindNestGoal extends Goal {

    private final HoneyguideEntity honeyguide;
    private BlockPos foundNest;
    private int searchCooldown;
    private static final int SEARCH_RADIUS = 48; // Matches POI registration distance
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

        // POI system only works on server side
        if (!(world instanceof ServerWorld serverWorld)) {
            return null;
        }

        BlockPos honeyguidePos = honeyguide.getBlockPos();
        PointOfInterestStorage poiStorage = serverWorld.getPointOfInterestStorage();

        // Use POI system to efficiently find nearest pollinator nest
        // This replaces the O(n³) triple nested loop with an O(log n) spatial query
        return poiStorage.getNearestPosition(
            poi -> poi.matchesKey(ModPointOfInterest.POLLINATOR_NEST_KEY),
            honeyguidePos,
            SEARCH_RADIUS,
            PointOfInterestStorage.OccupationStatus.ANY
        ).orElse(null);
    }

    public BlockPos getFoundNest() {
        return foundNest;
    }
}
