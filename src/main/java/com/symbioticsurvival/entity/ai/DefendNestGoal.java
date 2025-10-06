package com.symbioticsurvival.entity.ai;

import com.symbioticsurvival.entity.pollinator.BasePollinatorEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

/**
 * AI goal for defensive pollinators to attack entities that threaten their nest.
 */
public class DefendNestGoal extends ActiveTargetGoal<PlayerEntity> {

    private static final double NEST_DEFENSE_RADIUS = 10.0;
    private final BasePollinatorEntity pollinator;

    public DefendNestGoal(BasePollinatorEntity pollinator) {
        super(pollinator, PlayerEntity.class, true);
        this.pollinator = pollinator;
    }

    @Override
    public boolean canStart() {
        // Only defend if nest was destroyed
        if (!pollinator.nestDestroyed) return false;

        return super.canStart();
    }
}
