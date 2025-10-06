package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Yucca Moth - Passive pollinator for desert biomes.
 * Pollinates yucca plants, flees when threatened.
 */
public class YuccaMothEntity extends BasePollinatorEntity {

    public YuccaMothEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "desert", false);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        // Passive entities have enhanced flee behavior
        // TODO: Add pollination goals when fully implemented
    }
}
