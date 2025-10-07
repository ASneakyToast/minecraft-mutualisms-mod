package com.symbioticsurvival.entity.ai;

import com.symbioticsurvival.block.SpecialTreeBlock;
import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import com.symbioticsurvival.entity.pollinator.BasePollinatorEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

/**
 * AI goal for pollinators to fly to their linked tree and pollinate it.
 */
public class PollinateTreeGoal extends Goal {

    private final BasePollinatorEntity pollinator;
    private BlockPos targetTree;
    private int pollinatingTicks;
    private static final int POLLINATION_TIME = 40; // 2 seconds
    private static final double ARRIVAL_DISTANCE = 2.0;

    public PollinateTreeGoal(BasePollinatorEntity pollinator) {
        this.pollinator = pollinator;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        // Only pollinate if linked to a tree and cooldown is done
        if (pollinator.getLinkedTree() == null) return false;
        if (pollinator.pollinationCooldown > 0) return false;

        this.targetTree = pollinator.getLinkedTree();

        // Check if tree still exists
        World world = pollinator.getEntityWorld();
        if (world.getBlockState(targetTree).getBlock() instanceof SpecialTreeBlock) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldContinue() {
        return targetTree != null && pollinatingTicks < POLLINATION_TIME;
    }

    @Override
    public void start() {
        pollinatingTicks = 0;
    }

    @Override
    public void stop() {
        pollinatingTicks = 0;
    }

    @Override
    public void tick() {
        if (targetTree == null) return;

        // Calculate distance to tree
        double distance = pollinator.getBlockPos().getSquaredDistance(targetTree);

        if (distance > ARRIVAL_DISTANCE * ARRIVAL_DISTANCE) {
            // Fly toward tree
            pollinator.getNavigation().startMovingTo(
                targetTree.getX() + 0.5,
                targetTree.getY() + 1,
                targetTree.getZ() + 0.5,
                1.0
            );
        } else {
            // Arrived at tree, start pollinating
            pollinator.getNavigation().stop();
            pollinatingTicks++;

            // Pollination complete
            if (pollinatingTicks >= POLLINATION_TIME) {
                pollinateTree();

                // Set cooldown before next pollination
                pollinator.pollinationCooldown = 6000; // 5 minutes
            }
        }
    }

    private void pollinateTree() {
        World world = pollinator.getEntityWorld();
        if (world.isClient()) return;

        // Pollinate the tree using SpecialTreeBlock's pollinate method
        if (world.getBlockState(targetTree).getBlock() instanceof SpecialTreeBlock treeBlock) {
            treeBlock.pollinate((net.minecraft.server.world.ServerWorld) world, targetTree);
        }
    }
}
