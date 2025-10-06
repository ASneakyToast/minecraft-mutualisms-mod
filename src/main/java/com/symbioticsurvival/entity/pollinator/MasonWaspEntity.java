package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Mason Wasp - Defensive pollinator for savanna biomes.
 * Pollinates acacia trees and defends nest when threatened.
 */
public class MasonWaspEntity extends BasePollinatorEntity {

    public MasonWaspEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "savanna", true);
    }

    // Defensive pollinator - defensive behavior inherited from BasePollinatorEntity
}
