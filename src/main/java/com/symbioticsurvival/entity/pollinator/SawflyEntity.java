package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Sawfly - Passive pollinator for taiga biomes.
 * Pollinates conifer trees and flees when nest is destroyed.
 */
public class SawflyEntity extends BasePollinatorEntity {

    public SawflyEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "taiga", false);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        // TODO: Add pollination-specific goals
        // this.goalSelector.add(3, new PollinateTreeGoal(this));
        // this.goalSelector.add(4, new ReturnToNestGoal(this));
    }
}
