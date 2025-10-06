package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Orchard Bee - Defensive pollinator for cherry grove biomes.
 * Pollinates enhanced cherry trees and defends nest when threatened.
 */
public class OrchardBeeEntity extends BasePollinatorEntity {

    public OrchardBeeEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "cherry_grove", true);
    }

    @Override
    protected void initDefensiveGoals() {
        // TODO: Add attack goals when combat system is implemented
        // this.goalSelector.add(2, new DefendNestGoal(this, 1.4, true));
        // this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        // TODO: Add pollination-specific goals
        // this.goalSelector.add(3, new PollinateTreeGoal(this));
        // this.goalSelector.add(4, new ReturnToNestGoal(this));
    }
}
