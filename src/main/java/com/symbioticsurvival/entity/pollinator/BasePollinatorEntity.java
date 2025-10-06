package com.symbioticsurvival.entity.pollinator;

import com.symbioticsurvival.entity.ai.DefendNestGoal;
import com.symbioticsurvival.entity.ai.PollinateTreeGoal;
import com.symbioticsurvival.entity.ai.ReturnToNestGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for all pollinator entities.
 * Handles tree-nest linkage and basic pollination behavior.
 */
public abstract class BasePollinatorEntity extends AnimalEntity {

    protected BlockPos linkedNest;
    protected BlockPos linkedTree;
    public int pollinationCooldown;
    public boolean nestDestroyed;

    protected final String biomeType;
    protected final boolean isDefensive;

    public BasePollinatorEntity(EntityType<? extends AnimalEntity> entityType, World world,
                                String biomeType, boolean isDefensive) {
        super(entityType, world);
        this.biomeType = biomeType;
        this.isDefensive = isDefensive;
        this.pollinationCooldown = 100;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(2, new ReturnToNestGoal(this));
        this.goalSelector.add(3, new PollinateTreeGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));

        // Defensive pollinators attack when nest is threatened
        if (isDefensive) {
            initDefensiveGoals();
        }
    }

    /**
     * Override in defensive pollinator subclasses to add attack behaviors
     */
    protected void initDefensiveGoals() {
        // Add melee attack and targeting goals
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.4, false));
        this.targetSelector.add(1, new DefendNestGoal(this));
        this.targetSelector.add(2, new RevengeGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getEntityWorld().isClient()) {
            // Countdown pollination cooldown
            if (pollinationCooldown > 0) {
                pollinationCooldown--;
            }
        }
    }

    @Override
    public boolean isBreedingItem(net.minecraft.item.ItemStack stack) {
        // Pollinators don't breed in this version
        return false;
    }

    /**
     * Link this pollinator to a nest
     */
    public void linkToNest(BlockPos nestPos) {
        this.linkedNest = nestPos;
    }

    /**
     * Link this pollinator to a tree
     */
    public void linkToTree(BlockPos treePos) {
        this.linkedTree = treePos;
    }

    /**
     * Called when the linked nest is destroyed
     */
    public void onNestDestroyed() {
        this.nestDestroyed = true;

        if (!isDefensive) {
            // Passive pollinators flee and despawn
            this.setRemoved(RemovalReason.DISCARDED);
        }
    }

    public BlockPos getLinkedNest() {
        return linkedNest;
    }

    public BlockPos getLinkedTree() {
        return linkedTree;
    }

    public String getBiomeType() {
        return biomeType;
    }

    public boolean isDefensive() {
        return isDefensive;
    }

    // TODO: Entity NBT serialization methods not yet available in 1.21.9 Yarn mappings
    // Will be implemented when API is clarified
    /*
    @Override
    public void writeCustomDataToNbt(net.minecraft.nbt.NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (linkedNest != null) {
            nbt.putLong("LinkedNest", linkedNest.asLong());
        }

        if (linkedTree != null) {
            nbt.putLong("LinkedTree", linkedTree.asLong());
        }

        nbt.putInt("PollinationCooldown", pollinationCooldown);
        nbt.putBoolean("NestDestroyed", nestDestroyed);
        nbt.putString("BiomeType", biomeType);
    }

    @Override
    public void readCustomDataFromNbt(net.minecraft.nbt.NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("LinkedNest")) {
            this.linkedNest = BlockPos.fromLong(nbt.getLong("LinkedNest"));
        }

        if (nbt.contains("LinkedTree")) {
            this.linkedTree = BlockPos.fromLong(nbt.getLong("LinkedTree"));
        }

        this.pollinationCooldown = nbt.getInt("PollinationCooldown");
        this.nestDestroyed = nbt.getBoolean("NestDestroyed");
    }
    */

    @Nullable
    @Override
    public BasePollinatorEntity createChild(ServerWorld world, net.minecraft.entity.passive.PassiveEntity entity) {
        // Pollinators don't breed in this version
        return null;
    }

    /**
     * Create default attributes for pollinator entities.
     * Pollinators are small, flying insects with low health but good mobility.
     */
    public static DefaultAttributeContainer.Builder createPollinatorAttributes() {
        return AnimalEntity.createAnimalAttributes()
            .add(EntityAttributes.MAX_HEALTH, 6.0)
            .add(EntityAttributes.FLYING_SPEED, 0.6)
            .add(EntityAttributes.MOVEMENT_SPEED, 0.25);
    }
}
