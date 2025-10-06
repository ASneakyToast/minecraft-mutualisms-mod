package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Mangrove Pollinator - Defensive pollinator for swamp biomes.
 * Pollinates mangrove variants and defends nest when threatened.
 */
public class MangrovePollinatorEntity extends BasePollinatorEntity {

    public MangrovePollinatorEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "swamp", true);
    }

    // Defensive pollinator - defensive behavior inherited from BasePollinatorEntity
}
