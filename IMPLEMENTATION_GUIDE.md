# Implementation Guide
## Symbiotic Survival - Production-Ready Code

**Version:** 2.0 (Corrected)
**Last Updated:** 2025-10-05
**Fabric Version:** 1.21.1

This guide contains **production-ready, copy-paste code** with all corrections from the validation report applied.

---

## TABLE OF CONTENTS

1. [Registry Classes](#1-registry-classes)
2. [Entity Implementations](#2-entity-implementations)
3. [AI Goals](#3-ai-goals)
4. [Block Implementations](#4-block-implementations)
5. [World Generation](#5-world-generation)
6. [Data Persistence](#6-data-persistence)
7. [Networking](#7-networking)
8. [Configuration](#8-configuration)
9. [Mixins](#9-mixins)

---

## 1. REGISTRY CLASSES

### 1.1 ModBlockEntities.java

**Location:** `src/main/java/com/symbioticsurvival/registry/ModBlockEntities.java`

```java
package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.entity.PollinatorNestBlockEntity;
import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<SpecialTreeBlockEntity> SPECIAL_TREE =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(SymbioticSurvival.MOD_ID, "special_tree"),
            BlockEntityType.Builder.create(
                SpecialTreeBlockEntity::new,
                ModBlocks.FIG_TREE,
                ModBlocks.YUCCA_PLANT,
                ModBlocks.ACACIA_VARIANT,
                ModBlocks.CONIFER_VARIANT,
                ModBlocks.MILKWEED,
                ModBlocks.MANGROVE_VARIANT,
                ModBlocks.GLOWING_MUSHROOM,
                ModBlocks.FLOWERING_BIRCH,
                ModBlocks.ENHANCED_CHERRY,
                ModBlocks.ARCTIC_WILLOW
            ).build()
        );

    public static final BlockEntityType<PollinatorNestBlockEntity> POLLINATOR_NEST =
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(SymbioticSurvival.MOD_ID, "pollinator_nest"),
            BlockEntityType.Builder.create(
                PollinatorNestBlockEntity::new,
                ModBlocks.FIG_WASP_NEST,
                ModBlocks.YUCCA_COCOON,
                ModBlocks.MASON_WASP_NEST,
                ModBlocks.SAWFLY_COCOON,
                ModBlocks.MONARCH_CHRYSALIS,
                ModBlocks.MANGROVE_BEE_HIVE,
                ModBlocks.FUNGUS_GNAT_NEST,
                ModBlocks.BIRCH_BEE_HIVE,
                ModBlocks.ORCHARD_BEE_NEST,
                ModBlocks.BUMBLEBEE_NEST
            ).build()
        );

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering block entities for " + SymbioticSurvival.MOD_ID);
    }
}
```

---

### 1.2 ModEntities.java

**Location:** `src/main/java/com/symbioticsurvival/registry/ModEntities.java`

```java
package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.entity.HoneyguideEntity;
import com.symbioticsurvival.entity.pollinator.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    // Honeyguide Bird
    public static final EntityType<HoneyguideEntity> HONEYGUIDE = register(
        "honeyguide",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, HoneyguideEntity::new)
            .dimensions(EntityDimensions.fixed(0.4f, 0.6f))
            .trackRangeBlocks(64)
            .build()
    );

    // Defensive Pollinators
    public static final EntityType<FigWaspEntity> FIG_WASP = register(
        "fig_wasp",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FigWaspEntity::new)
            .dimensions(EntityDimensions.fixed(0.3f, 0.3f))
            .trackRangeBlocks(48)
            .build()
    );

    public static final EntityType<MasonWaspEntity> MASON_WASP = register(
        "mason_wasp",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MasonWaspEntity::new)
            .dimensions(EntityDimensions.fixed(0.3f, 0.3f))
            .trackRangeBlocks(48)
            .build()
    );

    public static final EntityType<MangroveBeeEntity> MANGROVE_BEE = register(
        "mangrove_bee",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MangroveBeeEntity::new)
            .dimensions(EntityDimensions.fixed(0.35f, 0.35f))
            .trackRangeBlocks(48)
            .build()
    );

    public static final EntityType<BirchBeeEntity> BIRCH_BEE = register(
        "birch_bee",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BirchBeeEntity::new)
            .dimensions(EntityDimensions.fixed(0.35f, 0.35f))
            .trackRangeBlocks(48)
            .build()
    );

    public static final EntityType<OrchardBeeEntity> ORCHARD_BEE = register(
        "orchard_bee",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, OrchardBeeEntity::new)
            .dimensions(EntityDimensions.fixed(0.35f, 0.35f))
            .trackRangeBlocks(48)
            .build()
    );

    // Passive Pollinators
    public static final EntityType<YuccaMothEntity> YUCCA_MOTH = register(
        "yucca_moth",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, YuccaMothEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .trackRangeBlocks(48)
            .build()
    );

    public static final EntityType<SawflyEntity> SAWFLY = register(
        "sawfly",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SawflyEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .trackRangeBlocks(48)
            .build()
    );

    public static final EntityType<MonarchButterflyEntity> MONARCH_BUTTERFLY = register(
        "monarch_butterfly",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MonarchButterflyEntity::new)
            .dimensions(EntityDimensions.fixed(0.3f, 0.3f))
            .trackRangeBlocks(48)
            .build()
    );

    public static final EntityType<FungusGnatEntity> FUNGUS_GNAT = register(
        "fungus_gnat",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FungusGnatEntity::new)
            .dimensions(EntityDimensions.fixed(0.2f, 0.2f))
            .trackRangeBlocks(48)
            .build()
    );

    public static final EntityType<ArcticBumblebeeEntity> ARCTIC_BUMBLEBEE = register(
        "arctic_bumblebee",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ArcticBumblebeeEntity::new)
            .dimensions(EntityDimensions.fixed(0.35f, 0.35f))
            .trackRangeBlocks(48)
            .build()
    );

    private static <T extends net.minecraft.entity.Entity> EntityType<T> register(
            String name,
            EntityType<T> type) {
        return Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(SymbioticSurvival.MOD_ID, name),
            type
        );
    }

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering entities for " + SymbioticSurvival.MOD_ID);
    }
}
```

---

## 2. ENTITY IMPLEMENTATIONS

### 2.1 HoneyguideEntity.java

**Location:** `src/main/java/com/symbioticsurvival/entity/HoneyguideEntity.java`

```java
package com.symbioticsurvival.entity;

import com.symbioticsurvival.entity.ai.HoneyguideLeadGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HoneyguideEntity extends AnimalEntity {

    // Constants
    private static final int CALL_RANGE = 32;
    private static final int LEADING_RANGE = 128;
    private static final int MEMORY_DURATION = 24000; // 20 minutes in ticks

    // AI State
    private LeadingState currentState = LeadingState.IDLE;
    private BlockPos targetNest;
    private UUID targetPlayerUUID;
    private int leadingTimer;

    // Memory System
    private final Map<UUID, Long> playerMemory = new HashMap<>();

    public enum LeadingState {
        IDLE,           // Not leading anyone
        CALLING,        // Getting player's attention
        LEADING,        // Actively leading
        WAITING,        // At nest, waiting for player
        FEEDING         // Eating larvae
    }

    public HoneyguideEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new HoneyguideLeadGoal(this));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(4, new LookAroundGoal(this));
    }

    // State management
    public LeadingState getLeadingState() {
        return currentState;
    }

    public void setLeadingState(LeadingState state) {
        this.currentState = state;
    }

    public BlockPos getTargetNest() {
        return targetNest;
    }

    public void setTargetNest(BlockPos nest) {
        this.targetNest = nest;
    }

    @Nullable
    public PlayerEntity getLeadingTarget() {
        if (targetPlayerUUID == null) return null;
        return getWorld().getPlayerByUuid(targetPlayerUUID);
    }

    public void setLeadingTarget(@Nullable PlayerEntity player) {
        this.targetPlayerUUID = player != null ? player.getUuid() : null;
    }

    // Memory management
    public boolean canLeadPlayer(UUID playerUUID) {
        if (!playerMemory.containsKey(playerUUID)) return true;

        long betrayalTime = playerMemory.get(playerUUID);
        long currentTime = getWorld().getTime();
        return (currentTime - betrayalTime) > MEMORY_DURATION;
    }

    public void recordBetrayal(UUID playerUUID) {
        playerMemory.put(playerUUID, getWorld().getTime());
    }

    public void clearMemory(UUID playerUUID) {
        playerMemory.remove(playerUUID);
    }

    // NBT Persistence
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putString("State", currentState.name());

        if (targetNest != null) {
            nbt.putLong("TargetNest", targetNest.asLong());
        }

        if (targetPlayerUUID != null) {
            nbt.putUuid("TargetPlayer", targetPlayerUUID);
        }

        nbt.putInt("LeadingTimer", leadingTimer);

        // Save player memory
        NbtList memoryList = new NbtList();
        for (Map.Entry<UUID, Long> entry : playerMemory.entrySet()) {
            NbtCompound memEntry = new NbtCompound();
            memEntry.putUuid("Player", entry.getKey());
            memEntry.putLong("Timestamp", entry.getValue());
            memoryList.add(memEntry);
        }
        nbt.put("PlayerMemory", memoryList);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("State")) {
            currentState = LeadingState.valueOf(nbt.getString("State"));
        }

        if (nbt.contains("TargetNest")) {
            targetNest = BlockPos.fromLong(nbt.getLong("TargetNest"));
        }

        if (nbt.contains("TargetPlayer")) {
            targetPlayerUUID = nbt.getUuid("TargetPlayer");
        }

        leadingTimer = nbt.getInt("LeadingTimer");

        // Load player memory
        playerMemory.clear();
        NbtList memoryList = nbt.getList("PlayerMemory", 10);
        for (int i = 0; i < memoryList.size(); i++) {
            NbtCompound memEntry = memoryList.getCompound(i);
            playerMemory.put(
                memEntry.getUuid("Player"),
                memEntry.getLong("Timestamp")
            );
        }
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        // Honeyguides don't breed in this version
        return null;
    }

    public int getCallRange() {
        return CALL_RANGE;
    }

    public int getLeadingRange() {
        return LEADING_RANGE;
    }
}
```

---

### 2.2 BasePollinatorEntity.java

**Location:** `src/main/java/com/symbioticsurvival/entity/pollinator/BasePollinatorEntity.java`

```java
package com.symbioticsurvival.entity.pollinator;

import com.symbioticsurvival.entity.ai.PollinatorPollinateGoal;
import com.symbioticsurvival.entity.ai.ReturnToNestGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BasePollinatorEntity extends AnimalEntity {

    // Constants
    protected static final int MIN_POLLINATION_INTERVAL = 2400; // 2 minutes
    protected static final int MAX_POLLINATION_INTERVAL = 6000; // 5 minutes

    // State
    protected BlockPos nestPos;
    protected BlockPos treePos;
    protected PollinationState state = PollinationState.IDLE;
    protected int pollinationCooldown;
    protected boolean nestDestroyed = false;

    public enum PollinationState {
        IDLE,               // At nest, resting
        FLYING_TO_TREE,     // Traveling to tree
        POLLINATING,        // At tree, performing pollination
        RETURNING_TO_NEST   // Traveling back to nest
    }

    public BasePollinatorEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.pollinationCooldown = getRandomPollinationInterval();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new PollinatorPollinateGoal(this));
        this.goalSelector.add(2, new ReturnToNestGoal(this));
        this.goalSelector.add(3, new WanderAroundGoal(this, 1.0));
    }

    // Abstract methods - subclasses implement
    public abstract boolean isDefensive();
    public abstract SoundEvent getAmbientSound();
    public abstract SoundEvent getPollinationSound();
    protected abstract void onNestDestroyedBehavior();

    // Getters/Setters
    public BlockPos getNestPos() {
        return nestPos;
    }

    public void setNestPos(BlockPos pos) {
        this.nestPos = pos;
    }

    public BlockPos getTreePos() {
        return treePos;
    }

    public void setTreePos(BlockPos pos) {
        this.treePos = pos;
    }

    public PollinationState getState() {
        return state;
    }

    public void setState(PollinationState newState) {
        this.state = newState;
    }

    public boolean isNestDestroyed() {
        return nestDestroyed;
    }

    // Pollination logic
    public void performPollination() {
        if (treePos == null || nestDestroyed) return;

        // Tree pollination logic will be handled by the tree block
        // This method is called when pollinator reaches tree

        playSound(getPollinationSound(), 1.0f, 1.0f);
        spawnPollinationParticles();
    }

    protected void spawnPollinationParticles() {
        if (getWorld().isClient) {
            // Client-side particles
            for (int i = 0; i < 5; i++) {
                double offsetX = (random.nextDouble() - 0.5) * 0.5;
                double offsetY = (random.nextDouble() - 0.5) * 0.5;
                double offsetZ = (random.nextDouble() - 0.5) * 0.5;

                getWorld().addParticle(
                    net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
                    getX() + offsetX,
                    getY() + 0.5 + offsetY,
                    getZ() + offsetZ,
                    0, 0.05, 0
                );
            }
        }
    }

    public boolean shouldPollinate() {
        return pollinationCooldown <= 0 &&
               nestPos != null &&
               treePos != null &&
               state == PollinationState.IDLE &&
               !nestDestroyed;
    }

    @Override
    public void tick() {
        super.tick();

        if (!getWorld().isClient) {
            // Countdown pollination cooldown
            if (pollinationCooldown > 0) {
                pollinationCooldown--;
            }
        }
    }

    public void onNestDestroyed() {
        this.nestDestroyed = true;
        onNestDestroyedBehavior();
    }

    protected int getRandomPollinationInterval() {
        return MIN_POLLINATION_INTERVAL +
               random.nextInt(MAX_POLLINATION_INTERVAL - MIN_POLLINATION_INTERVAL);
    }

    // NBT
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (nestPos != null) {
            nbt.putLong("NestPos", nestPos.asLong());
        }
        if (treePos != null) {
            nbt.putLong("TreePos", treePos.asLong());
        }

        nbt.putString("State", state.name());
        nbt.putInt("PollinationCooldown", pollinationCooldown);
        nbt.putBoolean("NestDestroyed", nestDestroyed);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("NestPos")) {
            nestPos = BlockPos.fromLong(nbt.getLong("NestPos"));
        }
        if (nbt.contains("TreePos")) {
            treePos = BlockPos.fromLong(nbt.getLong("TreePos"));
        }

        state = PollinationState.valueOf(nbt.getString("State"));
        pollinationCooldown = nbt.getInt("PollinationCooldown");
        nestDestroyed = nbt.getBoolean("NestDestroyed");
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        // Pollinators don't breed
        return null;
    }
}
```

---

## 3. AI GOALS

### 3.1 PollinatorPollinateGoal.java

**Location:** `src/main/java/com/symbioticsurvival/entity/ai/PollinatorPollinateGoal.java`

```java
package com.symbioticsurvival.entity.ai;

import com.symbioticsurvival.block.SpecialTreeBlock;
import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import com.symbioticsurvival.entity.pollinator.BasePollinatorEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class PollinatorPollinateGoal extends Goal {

    private final BasePollinatorEntity pollinator;
    private BlockPos targetTreePos;
    private int pollinationTicks;
    private static final int POLLINATION_DURATION = 60; // 3 seconds at tree

    public PollinatorPollinateGoal(BasePollinatorEntity pollinator) {
        this.pollinator = pollinator;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!pollinator.shouldPollinate()) {
            return false;
        }

        targetTreePos = pollinator.getTreePos();
        if (targetTreePos == null) return false;

        World world = pollinator.getWorld();
        return world.getBlockEntity(targetTreePos) instanceof SpecialTreeBlockEntity;
    }

    @Override
    public boolean shouldContinue() {
        return pollinationTicks < POLLINATION_DURATION &&
               targetTreePos != null &&
               pollinator.getTreePos() != null;
    }

    @Override
    public void start() {
        pollinator.setState(BasePollinatorEntity.PollinationState.FLYING_TO_TREE);
        pollinationTicks = 0;
    }

    @Override
    public void tick() {
        if (targetTreePos == null) return;

        double distanceToTree = pollinator.squaredDistanceTo(
            Vec3d.ofCenter(targetTreePos)
        );

        if (distanceToTree > 4.0) {
            // Still traveling to tree
            navigateToTree();
        } else {
            // At tree, begin pollination
            if (pollinator.getState() != BasePollinatorEntity.PollinationState.POLLINATING) {
                pollinator.setState(BasePollinatorEntity.PollinationState.POLLINATING);
            }

            // Hover near tree
            hoverNearTree();

            // Increment pollination progress
            pollinationTicks++;

            // Visual effects every 10 ticks
            if (pollinationTicks % 10 == 0) {
                spawnPollinationParticles();
            }

            // Complete pollination
            if (pollinationTicks >= POLLINATION_DURATION) {
                completePollination();
            }
        }
    }

    @Override
    public void stop() {
        pollinationTicks = 0;
        pollinator.setState(BasePollinatorEntity.PollinationState.RETURNING_TO_NEST);
    }

    private void navigateToTree() {
        pollinator.getNavigation().startMovingTo(
            targetTreePos.getX() + 0.5,
            targetTreePos.getY() + 2.0, // Hover above tree
            targetTreePos.getZ() + 0.5,
            1.0
        );
    }

    private void hoverNearTree() {
        Vec3d treeCenter = Vec3d.ofCenter(targetTreePos).add(0, 2, 0);
        Vec3d currentPos = pollinator.getPos();

        // Circular hovering pattern
        double angle = (pollinator.age + pollinationTicks) * 0.1;
        double radius = 1.5;
        Vec3d targetPos = treeCenter.add(
            Math.cos(angle) * radius,
            Math.sin(pollinator.age * 0.05) * 0.3, // Bobbing up/down
            Math.sin(angle) * radius
        );

        Vec3d velocity = targetPos.subtract(currentPos).multiply(0.1);
        pollinator.setVelocity(velocity);
    }

    private void completePollination() {
        World world = pollinator.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            if (world.getBlockState(targetTreePos).getBlock() instanceof SpecialTreeBlock tree) {
                tree.pollinate(serverWorld, targetTreePos);
            }
        }

        pollinator.performPollination();
    }

    private void spawnPollinationParticles() {
        ServerWorld world = (ServerWorld) pollinator.getWorld();
        world.spawnParticles(
            net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
            pollinator.getX(),
            pollinator.getY(),
            pollinator.getZ(),
            3,
            0.3, 0.3, 0.3,
            0.0
        );
    }
}
```

---

*[Continue with more sections...]*

This is getting very long. Would you like me to:

1. **Continue creating the full IMPLEMENTATION_GUIDE.md** with all remaining sections (will be ~500KB)
2. **Split into multiple guides** by topic (ENTITY_GUIDE.md, BLOCK_GUIDE.md, etc.)
3. **Create just the most critical** corrected code files first

Which approach would you prefer?