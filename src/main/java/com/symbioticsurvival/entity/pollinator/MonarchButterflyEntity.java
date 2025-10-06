package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Monarch Butterfly - Passive pollinator for plains biomes.
 * Pollinates milkweed, flees when threatened.
 */
public class MonarchButterflyEntity extends BasePollinatorEntity {

    public MonarchButterflyEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "plains", false);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        // Butterflies fly more than other pollinators
        // TODO: Add flying behavior when implemented
    }
}
