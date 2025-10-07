package com.symbioticsurvival.entity;

import com.symbioticsurvival.entity.ai.CallPlayerGoal;
import com.symbioticsurvival.entity.ai.FindNestGoal;
import com.symbioticsurvival.entity.ai.LeadPlayerToNestGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Honeyguide bird that leads players to bee nests.
 * Demonstrates mutualism through behavioral rewards.
 */
public class HoneyguideEntity extends AnimalEntity {

    private UUID targetPlayer;
    private BlockPos targetNest;
    private int leadingTimeout;
    private boolean isLeading;

    public HoneyguideEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.leadingTimeout = 6000; // 5 minutes
    }

    @Override
    protected void initGoals() {
        FindNestGoal findNestGoal = new FindNestGoal(this);

        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(2, new LeadPlayerToNestGoal(this));
        this.goalSelector.add(3, new CallPlayerGoal(this, findNestGoal));
        this.goalSelector.add(4, findNestGoal);
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getEntityWorld().isClient()) {
            if (isLeading && leadingTimeout > 0) {
                leadingTimeout--;

                if (leadingTimeout <= 0) {
                    // Abandon leading
                    abandonLeading();
                }
            }
        }
    }

    @Override
    public boolean isBreedingItem(net.minecraft.item.ItemStack stack) {
        // Honeyguides don't breed in this version
        return false;
    }

    /**
     * Start leading a player to a nest
     */
    public void startLeading(PlayerEntity player, BlockPos nest) {
        this.targetPlayer = player.getUuid();
        this.targetNest = nest;
        this.isLeading = true;
        this.leadingTimeout = 6000;
    }

    /**
     * Stop leading behavior
     */
    public void abandonLeading() {
        this.targetPlayer = null;
        this.targetNest = null;
        this.isLeading = false;
    }

    /**
     * Check if player took all the larvae (betrayal)
     */
    public void checkForBetrayal(PlayerEntity player) {
        // TODO: Implement memory system
        // Record betrayal and refuse to lead this player for 20 minutes
    }

    public boolean isLeading() {
        return isLeading;
    }

    public UUID getTargetPlayer() {
        return targetPlayer;
    }

    public BlockPos getTargetNest() {
        return targetNest;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        // Honeyguides don't breed in this version
        return null;
    }

    /**
     * Create default attributes for the Honeyguide entity.
     * Honeyguides are small, flying birds with moderate health.
     */
    public static DefaultAttributeContainer.Builder createHoneyguideAttributes() {
        return AnimalEntity.createAnimalAttributes()
            .add(EntityAttributes.MAX_HEALTH, 8.0)
            .add(EntityAttributes.FLYING_SPEED, 0.6)
            .add(EntityAttributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    public void writeCustomDataToNbt(net.minecraft.nbt.NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (targetPlayer != null) {
            nbt.putString("TargetPlayer", targetPlayer.toString());
        }

        if (targetNest != null) {
            nbt.putLong("TargetNest", targetNest.asLong());
        }

        nbt.putInt("LeadingTimeout", leadingTimeout);
        nbt.putBoolean("IsLeading", isLeading);
    }

    @Override
    public void readCustomDataFromNbt(net.minecraft.nbt.NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("TargetPlayer")) {
            String uuidString = nbt.getString("TargetPlayer");
            try {
                if (!uuidString.isEmpty()) {
                    this.targetPlayer = UUID.fromString(uuidString);
                }
            } catch (IllegalArgumentException e) {
                this.targetPlayer = null;
            }
        }

        if (nbt.contains("TargetNest")) {
            this.targetNest = BlockPos.fromLong(nbt.getLong("TargetNest"));
        }

        if (nbt.contains("LeadingTimeout")) {
            this.leadingTimeout = nbt.getInt("LeadingTimeout");
        }
        if (nbt.contains("IsLeading")) {
            this.isLeading = nbt.getBoolean("IsLeading");
        }
    }
}
