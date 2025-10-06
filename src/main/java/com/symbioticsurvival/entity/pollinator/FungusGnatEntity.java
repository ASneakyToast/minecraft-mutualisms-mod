package com.symbioticsurvival.entity.pollinator;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

/**
 * Fungus Gnat - Passive pollinator for mushroom biomes.
 * Pollinates glowing mushrooms and flees when nest is destroyed.
 */
public class FungusGnatEntity extends BasePollinatorEntity {

    public FungusGnatEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world, "mushroom", false);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        // TODO: Add pollination-specific goals
        // this.goalSelector.add(3, new PollinateTreeGoal(this));
        // this.goalSelector.add(4, new ReturnToNestGoal(this));
    }
}
