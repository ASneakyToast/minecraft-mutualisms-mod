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

    // Defensive pollinator - defensive behavior inherited from BasePollinatorEntity
}
