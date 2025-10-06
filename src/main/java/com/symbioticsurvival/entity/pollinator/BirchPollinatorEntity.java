package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Birch Pollinator - Passive pollinator for birch forest biomes.
 * Pollinates flowering birch trees and flees when nest is destroyed.
 */
public class BirchPollinatorEntity extends BasePollinatorEntity {

    public BirchPollinatorEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "birch_forest", false);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        // Passive pollinator with standard pollination behavior from parent class
    }
}
