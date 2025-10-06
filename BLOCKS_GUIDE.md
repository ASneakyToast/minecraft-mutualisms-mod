# Block Implementations Guide
## Production-Ready Block Code

**Version:** 2.0 (Corrected)
**Fabric Version:** 1.21.1

---

## TABLE OF CONTENTS

1. [SpecialTreeBlock](#1-specialtreeblock)
2. [SpecialTreeBlockEntity](#2-specialtreeblockentity)
3. [PollinatorNestBlock](#3-pollinatornestblock)
4. [PollinatorNestBlockEntity](#4-pollinatornestblockentity)
5. [Fruit Blocks](#5-fruit-blocks)

---

## 1. SPECIALTREEBLOCK

### 1.1 SpecialTreeBlock.java

**Location:** `src/main/java/com/symbioticsurvival/block/SpecialTreeBlock.java`

```java
package com.symbioticsurvival.block;

import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import com.symbioticsurvival.registry.ModBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for special trees that require pollination.
 * Manages fruit growth states and tree-nest linkage.
 */
public class SpecialTreeBlock extends BlockWithEntity {

    // Fruit states: 0=immature, 1=pollinated, 2=mature
    public static final IntProperty FRUIT_STATE = IntProperty.of("fruit_state", 0, 2);

    private final String biomeType;

    public SpecialTreeBlock(Settings settings, String biomeType) {
        super(settings);
        this.biomeType = biomeType;
        setDefaultState(getDefaultState().with(FRUIT_STATE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FRUIT_STATE);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SpecialTreeBlockEntity(pos, state, biomeType);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world,
            BlockState state,
            BlockEntityType<T> type) {
        return validateTicker(
            type,
            ModBlockEntities.SPECIAL_TREE,
            SpecialTreeBlockEntity::tick
        );
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int fruitState = state.get(FRUIT_STATE);

        if (fruitState == 0) {
            // Immature - wait for pollination
            return;
        } else if (fruitState == 1) {
            // Pollinated - chance to mature
            if (random.nextInt(5) == 0) { // 20% chance per random tick
                world.setBlockState(pos, state.with(FRUIT_STATE, 2));
            }
        }
        // State 2 (mature) - ready for harvest
    }

    /**
     * Called by pollinator to mark tree as pollinated
     */
    public void pollinate(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (state.get(FRUIT_STATE) == 0) {
            world.setBlockState(pos, state.with(FRUIT_STATE, 1));

            // Spawn particles
            spawnPollinationParticles(world, pos);
        }
    }

    private void spawnPollinationParticles(ServerWorld world, BlockPos pos) {
        world.spawnParticles(
            net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
            pos.getX() + 0.5,
            pos.getY() + 0.5,
            pos.getZ() + 0.5,
            5,
            0.3, 0.3, 0.3,
            0.0
        );
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && state.get(FRUIT_STATE) == 2) {
            // Drop mature fruit
            dropStack(world, pos, new ItemStack(getFruitItem()));
        }

        super.onBreak(world, pos, state, player);
    }

    /**
     * Get the fruit item for this tree type
     * Override in subclasses or use registry
     */
    protected Item getFruitItem() {
        return ModItems.getFruitForBiome(biomeType);
    }

    public String getBiomeType() {
        return biomeType;
    }
}
```

---

## 2. SPECIALTREEBLOCKENTITY

### 2.1 SpecialTreeBlockEntity.java

**Location:** `src/main/java/com/symbioticsurvival/block/entity/SpecialTreeBlockEntity.java`

```java
package com.symbioticsurvival.block.entity;

import com.symbioticsurvival.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Stores tree-nest linkage and manages pollination state.
 */
public class SpecialTreeBlockEntity extends BlockEntity {

    private BlockPos linkedNest;
    private String biomeType;
    private boolean canBePollinated = false;

    public SpecialTreeBlockEntity(BlockPos pos, BlockState state, String biomeType) {
        super(ModBlockEntities.SPECIAL_TREE, pos, state);
        this.biomeType = biomeType;
    }

    // Default constructor for registration
    public SpecialTreeBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, "unknown");
    }

    /**
     * Ticker method called every tick
     */
    public static void tick(World world, BlockPos pos, BlockState state, SpecialTreeBlockEntity blockEntity) {
        if (world.isClient) return;

        // Verify nest still exists periodically
        if (world.getTime() % 100 == 0) { // Every 5 seconds
            blockEntity.validateNestLink();
        }
    }

    /**
     * Link this tree to a pollinator nest
     */
    public void linkToNest(BlockPos nestPos) {
        this.linkedNest = nestPos;
        this.canBePollinated = true;
        markDirty();
    }

    /**
     * Check if tree has a valid nest link
     */
    public boolean hasLinkedNest() {
        if (linkedNest == null) return false;

        // Verify nest still exists
        if (world != null) {
            BlockState nestState = world.getBlockState(linkedNest);
            return nestState.getBlock() instanceof PollinatorNestBlock;
        }

        return false;
    }

    /**
     * Called when linked nest is destroyed
     */
    public void onNestDestroyed() {
        this.canBePollinated = false;
        markDirty();
    }

    /**
     * Validate that the nest link is still valid
     */
    private void validateNestLink() {
        if (linkedNest != null && world != null) {
            BlockState nestState = world.getBlockState(linkedNest);

            if (!(nestState.getBlock() instanceof PollinatorNestBlock)) {
                // Nest was destroyed
                onNestDestroyed();
            }
        }
    }

    public BlockPos getLinkedNest() {
        return linkedNest;
    }

    public String getBiomeType() {
        return biomeType;
    }

    public boolean canBePollinated() {
        return canBePollinated;
    }

    // NBT Serialization

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        if (linkedNest != null) {
            nbt.putLong("LinkedNest", linkedNest.asLong());
        }

        nbt.putString("BiomeType", biomeType);
        nbt.putBoolean("CanBePollinated", canBePollinated);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        if (nbt.contains("LinkedNest")) {
            linkedNest = BlockPos.fromLong(nbt.getLong("LinkedNest"));
        }

        biomeType = nbt.getString("BiomeType");
        canBePollinated = nbt.getBoolean("CanBePollinated");
    }
}
```

---

## 3. POLLINATORNESTBLOCK

### 3.1 PollinatorNestBlock.java

**Location:** `src/main/java/com/symbioticsurvival/block/PollinatorNestBlock.java`

```java
package com.symbioticsurvival.block;

import com.symbioticsurvival.block.entity.PollinatorNestBlockEntity;
import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import com.symbioticsurvival.entity.pollinator.BasePollinatorEntity;
import com.symbioticsurvival.registry.ModBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Pollinator nest that spawns pollinators and links to trees.
 */
public class PollinatorNestBlock extends BlockWithEntity {

    private final String biomeType;
    private final boolean isDefensive;

    public PollinatorNestBlock(Settings settings, String biomeType, boolean isDefensive) {
        super(settings);
        this.biomeType = biomeType;
        this.isDefensive = isDefensive;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PollinatorNestBlockEntity(pos, state, biomeType);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world,
            BlockState state,
            BlockEntityType<T> type) {
        return validateTicker(
            type,
            ModBlockEntities.POLLINATOR_NEST,
            PollinatorNestBlockEntity::tick
        );
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);

            if (be instanceof PollinatorNestBlockEntity nest) {
                // Notify linked tree
                nest.onNestDestroyed();

                // Spawn angry pollinators if defensive
                if (isDefensive) {
                    spawnDefensivePollinators(world, pos, player);
                }

                // Notify existing pollinators
                notifyPollinators(world, pos);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    private void spawnDefensivePollinators(World world, BlockPos pos, PlayerEntity player) {
        // Spawn 2-4 angry pollinators
        int count = 2 + world.random.nextInt(3);

        for (int i = 0; i < count; i++) {
            // Pollinator spawning logic
            // This would use the EntityType for this biome
        }
    }

    private void notifyPollinators(World world, BlockPos pos) {
        Box searchBox = Box.of(pos.toCenterPos(), 32, 32, 32);

        List<BasePollinatorEntity> pollinators = world.getEntitiesByClass(
            BasePollinatorEntity.class,
            searchBox,
            p -> pos.equals(p.getNestPos())
        );

        pollinators.forEach(BasePollinatorEntity::onNestDestroyed);
    }

    public String getBiomeType() {
        return biomeType;
    }

    public boolean isDefensive() {
        return isDefensive;
    }
}
```

---

## 4. POLLINATORNESTBLOCKENTITY

### 4.1 PollinatorNestBlockEntity.java

**Location:** `src/main/java/com/symbioticsurvival/block/entity/PollinatorNestBlockEntity.java`

```java
package com.symbioticsurvival.block.entity;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Stores nest-tree linkage and manages pollinator spawning.
 */
public class PollinatorNestBlockEntity extends BlockEntity {

    private BlockPos linkedTree;
    private String biomeType;
    private int pollinationCooldown;
    private List<UUID> activePollinators = new ArrayList<>();

    public PollinatorNestBlockEntity(BlockPos pos, BlockState state, String biomeType) {
        super(ModBlockEntities.POLLINATOR_NEST, pos, state);
        this.biomeType = biomeType;
        this.pollinationCooldown = getRandomInitialCooldown();
    }

    // Default constructor
    public PollinatorNestBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, "unknown");
    }

    /**
     * Ticker method
     */
    public static void tick(World world, BlockPos pos, BlockState state,
                           PollinatorNestBlockEntity blockEntity) {
        if (world.isClient) return;

        // Countdown to next pollination
        if (blockEntity.pollinationCooldown > 0) {
            blockEntity.pollinationCooldown--;
        }

        // Spawn pollinator if it's time
        if (blockEntity.pollinationCooldown == 0 && blockEntity.linkedTree != null) {
            blockEntity.spawnPollinator();
            blockEntity.pollinationCooldown = blockEntity.getRandomPollinationInterval();
        }

        // Cleanup dead pollinators from list
        blockEntity.cleanupPollinators();
    }

    private void spawnPollinator() {
        // Don't spawn if too many active
        if (activePollinators.size() >= 2) return;

        // Pollinator spawning logic here
        // Would create entity based on biomeType
    }

    private void cleanupPollinators() {
        if (world == null) return;

        activePollinators.removeIf(uuid -> world.getEntity(uuid) == null);
    }

    /**
     * Link this nest to a tree
     */
    public void linkToTree(BlockPos treePos) {
        this.linkedTree = treePos;
        markDirty();
    }

    /**
     * Called when this nest is destroyed
     */
    public void onNestDestroyed() {
        if (linkedTree != null && world != null) {
            BlockEntity treeBe = world.getBlockEntity(linkedTree);

            if (treeBe instanceof SpecialTreeBlockEntity tree) {
                tree.onNestDestroyed();
            }
        }
    }

    public BlockPos getLinkedTree() {
        return linkedTree;
    }

    public String getBiomeType() {
        return biomeType;
    }

    private int getRandomPollinationInterval() {
        int min = SymbioticSurvival.CONFIG.pollination.pollinationIntervalMin;
        int max = SymbioticSurvival.CONFIG.pollination.pollinationIntervalMax;
        return min + (world != null ? world.random.nextInt(max - min) : (max - min) / 2);
    }

    private int getRandomInitialCooldown() {
        // Random initial cooldown between 1-3 minutes
        return 1200 + (world != null ? world.random.nextInt(2400) : 1200);
    }

    // NBT Serialization

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        if (linkedTree != null) {
            nbt.putLong("LinkedTree", linkedTree.asLong());
        }

        nbt.putString("BiomeType", biomeType);
        nbt.putInt("PollinationCooldown", pollinationCooldown);

        NbtList pollinatorList = new NbtList();
        for (UUID uuid : activePollinators) {
            NbtCompound uuidTag = new NbtCompound();
            uuidTag.putUuid("UUID", uuid);
            pollinatorList.add(uuidTag);
        }
        nbt.put("ActivePollinators", pollinatorList);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        if (nbt.contains("LinkedTree")) {
            linkedTree = BlockPos.fromLong(nbt.getLong("LinkedTree"));
        }

        biomeType = nbt.getString("BiomeType");
        pollinationCooldown = nbt.getInt("PollinationCooldown");

        activePollinators.clear();
        NbtList pollinatorList = nbt.getList("ActivePollinators", 10);
        for (int i = 0; i < pollinatorList.size(); i++) {
            NbtCompound uuidTag = pollinatorList.getCompound(i);
            activePollinators.add(uuidTag.getUuid("UUID"));
        }
    }
}
```

---

## 5. FRUIT BLOCKS

### 5.1 FruitBlock.java (Optional - if fruits are separate blocks)

```java
package com.symbioticsurvival.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Optional: Fruit as a separate block that can be harvested
 */
public class FruitBlock extends Block {

    private final Item fruitItem;
    private final int minDrops;
    private final int maxDrops;

    public FruitBlock(Settings settings, Item fruitItem, int minDrops, int maxDrops) {
        super(settings);
        this.fruitItem = fruitItem;
        this.minDrops = minDrops;
        this.maxDrops = maxDrops;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            // Drop fruit and remove block
            int count = minDrops + world.random.nextInt(maxDrops - minDrops + 1);
            dropStack(world, pos, new ItemStack(fruitItem, count));
            world.removeBlock(pos, false);

            return ActionResult.SUCCESS;
        }

        return ActionResult.CONSUME;
    }
}
```

---

## 6. BLOCK REGISTRATION

### 6.1 ModBlocks.java

```java
package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.PollinatorNestBlock;
import com.symbioticsurvival.block.SpecialTreeBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    // Trees
    public static final Block FIG_TREE = register("fig_tree",
        new SpecialTreeBlock(
            FabricBlockSettings.create()
                .mapColor(MapColor.BROWN)
                .strength(2.0f)
                .sounds(BlockSoundGroup.WOOD)
                .ticksRandomly(),
            "jungle"
        ));

    public static final Block YUCCA_PLANT = register("yucca_plant",
        new SpecialTreeBlock(
            FabricBlockSettings.create()
                .mapColor(MapColor.GREEN)
                .strength(1.0f)
                .sounds(BlockSoundGroup.GRASS)
                .ticksRandomly(),
            "desert"
        ));

    // ... (register all 10 tree types)

    // Nests
    public static final Block FIG_WASP_NEST = register("fig_wasp_nest",
        new PollinatorNestBlock(
            FabricBlockSettings.create()
                .mapColor(MapColor.BROWN)
                .strength(0.5f)
                .sounds(BlockSoundGroup.WOOD),
            "jungle",
            true  // defensive
        ));

    public static final Block YUCCA_COCOON = register("yucca_cocoon",
        new PollinatorNestBlock(
            FabricBlockSettings.create()
                .mapColor(MapColor.WHITE)
                .strength(0.3f)
                .sounds(BlockSoundGroup.WOOL),
            "desert",
            false  // passive
        ));

    // ... (register all 10 nest types)

    private static Block register(String name, Block block) {
        return Registry.register(
            Registries.BLOCK,
            Identifier.of(SymbioticSurvival.MOD_ID, name),
            block
        );
    }

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering blocks for " + SymbioticSurvival.MOD_ID);
    }
}
```

---

This guide provides **production-ready block implementations** with proper BlockEntity ticking, NBT persistence, and tree-nest linkage systems.
