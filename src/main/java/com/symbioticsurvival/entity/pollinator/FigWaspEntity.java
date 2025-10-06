package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Fig Wasp - Defensive pollinator for tropical biomes.
 * Pollinates fig trees and defends nest when threatened.
 */
public class FigWaspEntity extends BasePollinatorEntity {

    public FigWaspEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "tropical", true);
    }

    // Defensive pollinator - defensive behavior inherited from BasePollinatorEntity
}
