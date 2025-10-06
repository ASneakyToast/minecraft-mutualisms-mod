package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Bumblebee - Defensive pollinator for snowy biomes.
 * Pollinates arctic willow and defends nest when threatened.
 */
public class BumblebeeEntity extends BasePollinatorEntity {

    public BumblebeeEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "snowy", true);
    }

    // Defensive pollinator - defensive behavior inherited from BasePollinatorEntity
}
