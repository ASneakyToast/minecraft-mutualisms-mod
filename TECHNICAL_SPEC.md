# Technical Specification Document
## Symbiotic Survival Mod

---

## 1. SYSTEM ARCHITECTURE OVERVIEW

### 1.1 Component Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    Minecraft Server                         │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   World Gen  │  │    Entities  │  │    Blocks    │      │
│  │              │  │              │  │              │      │
│  │ - BiomePair  │  │ - Honeyguide │  │ - SpecialTree│      │
│  │   Features   │  │ - Pollinators│  │ - Nests      │      │
│  │ - Tree Gen   │  │              │  │ - Fruits     │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
│         │                 │                 │              │
│         └─────────────────┴─────────────────┘              │
│                           │                                │
│         ┌─────────────────┴─────────────────┐              │
│         │                                   │              │
│  ┌──────▼───────┐                   ┌───────▼──────┐       │
│  │ NBT Storage  │                   │   Config     │       │
│  │              │                   │              │       │
│  │ - Tree-Nest  │                   │ - Spawn Rates│       │
│  │   Links      │                   │ - Timings    │       │
│  │ - Pollination│                   │ - Balance    │       │
│  │   States     │                   │              │       │
│  │ - Player     │                   │              │       │
│  │   Memory     │                   │              │       │
│  └──────────────┘                   └──────────────┘       │
└─────────────────────────────────────────────────────────────┘
```

### 1.2 Data Flow

**Pollination Cycle:**
```
1. World Gen → Place Tree + Nest (linked via NBT)
2. Tree grows → Produces immature fruit (random tick)
3. Pollinator AI → Pathfind from nest to tree (scheduled task)
4. Pollination event → Update fruit NBT state to "pollinated"
5. Fruit maturation → Random tick checks NBT, transitions to mature
6. Player harvests → Drop mature fruit item
```

**Honeyguide Behavior:**
```
1. Spawn system → Check biome + bee nest proximity
2. Honeyguide AI → Detect player within range
3. Leading behavior → Pathfind toward nest, wait for player
4. Nest break event → Drop larvae items
5. Bird behavior → Pick up larvae OR update player memory NBT
6. Memory check → Next spawn decision based on player UUID
```

---

## 2. DETAILED COMPONENT SPECIFICATIONS

### 2.1 Entity System

#### 2.1.1 HoneyguideEntity

**Class Hierarchy:**
```java
PathAwareEntity
  └── AnimalEntity
      └── HoneyguideEntity
```

**Key Fields:**
```java
public class HoneyguideEntity extends AnimalEntity {
    // AI State
    private UUID targetPlayer;
    private BlockPos targetNest;
    private LeadingState state; // IDLE, CALLING, LEADING, WAITING, FEEDING

    // Configuration
    private static final int CALL_RANGE = 32;
    private static final int LEADING_RANGE = 128;
    private static final int MEMORY_DURATION = 24000; // 20 minutes in ticks

    // Memory System
    private Map<UUID, Long> playerMemory; // UUID -> timestamp of betrayal

    // Behavior timers
    private int callCooldown;
    private int leadingTimeout;
}
```

**Key Methods:**
```java
// AI Goals
@Override
protected void initGoals() {
    this.goalSelector.add(0, new SwimGoal(this));
    this.goalSelector.add(1, new FindNestGoal(this));
    this.goalSelector.add(2, new CallPlayerGoal(this));
    this.goalSelector.add(3, new LeadToNestGoal(this));
    this.goalSelector.add(4, new FeedOnLarvaeGoal(this));
    this.goalSelector.add(5, new WanderAroundGoal(this, 1.0));
}

// Memory Management
public boolean canLeadPlayer(UUID playerUUID) {
    if (!playerMemory.containsKey(playerUUID)) return true;
    long betrayalTime = playerMemory.get(playerUUID);
    long currentTime = this.world.getTime();
    return (currentTime - betrayalTime) > MEMORY_DURATION;
}

public void recordBetrayal(UUID playerUUID) {
    playerMemory.put(playerUUID, this.world.getTime());
    markDirty(); // Trigger NBT save
}

// NBT Persistence
@Override
public void writeCustomDataToNbt(NbtCompound nbt) {
    super.writeCustomDataToNbt(nbt);
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
    NbtList memoryList = nbt.getList("PlayerMemory", 10);
    playerMemory.clear();
    for (int i = 0; i < memoryList.size(); i++) {
        NbtCompound memEntry = memoryList.getCompound(i);
        playerMemory.put(
            memEntry.getUuid("Player"),
            memEntry.getLong("Timestamp")
        );
    }
}
```

**AI Goal: LeadToNestGoal**
```java
public class LeadToNestGoal extends Goal {
    private final HoneyguideEntity bird;
    private PlayerEntity targetPlayer;
    private BlockPos targetNest;
    private int waitTimer;

    @Override
    public boolean canStart() {
        // Check if player is in range and bird can lead them
        PlayerEntity nearest = bird.world.getClosestPlayer(bird, CALL_RANGE);
        if (nearest == null || !bird.canLeadPlayer(nearest.getUuid())) {
            return false;
        }

        // Find nearest bee nest
        BlockPos nest = findNearestBeeNest(bird.getBlockPos(), LEADING_RANGE);
        if (nest == null) return false;

        this.targetPlayer = nearest;
        this.targetNest = nest;
        return true;
    }

    @Override
    public void tick() {
        double distanceToPlayer = bird.squaredDistanceTo(targetPlayer);
        double distanceToNest = bird.getBlockPos().getSquaredDistance(targetNest);

        if (distanceToNest < 4.0) {
            // At nest - circle and call
            circleAboveNest();
            bird.playSound(SoundEvents.HONEYGUIDE_CALL_INSISTENT, 1.0f, 1.0f);
        } else if (distanceToPlayer > 100.0) {
            // Player too far - wait
            waitTimer++;
            if (waitTimer > 100) { // 5 seconds
                bird.getNavigation().startMovingTo(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), 1.2);
                waitTimer = 0;
            }
        } else {
            // Lead toward nest
            bird.getNavigation().startMovingTo(targetNest.getX(), targetNest.getY(), targetNest.getZ(), 1.0);
        }
    }
}
```

---

#### 2.1.2 Pollinator Entities

**Base Class:**
```java
public abstract class BasePollinatorEntity extends AnimalEntity {
    // Linkage
    protected BlockPos linkedNest;
    protected BlockPos linkedTree;

    // Pollination State
    protected int pollinationCooldown;
    protected static final int MIN_POLLINATION_INTERVAL = 2400; // 2 minutes
    protected static final int MAX_POLLINATION_INTERVAL = 6000; // 5 minutes

    // Lifecycle
    protected boolean nestDestroyed = false;

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new PollinateTreeGoal(this));
        this.goalSelector.add(2, new ReturnToNestGoal(this));
        this.goalSelector.add(3, new WanderNearNestGoal(this, 1.0, 10));
    }

    public void onNestDestroyed() {
        this.nestDestroyed = true;
        onNestDestroyedBehavior(); // Abstract - implemented by subclasses
    }

    protected abstract void onNestDestroyedBehavior();

    // Pollination logic
    public void pollinateTree() {
        if (linkedTree == null || nestDestroyed) return;

        BlockState treeState = world.getBlockState(linkedTree);
        if (treeState.getBlock() instanceof SpecialTreeBlock tree) {
            tree.pollinate(world, linkedTree);
            pollinationCooldown = world.random.nextBetween(
                MIN_POLLINATION_INTERVAL,
                MAX_POLLINATION_INTERVAL
            );
        }
    }
}
```

**Defensive Pollinator:**
```java
public class DefensivePollinatorEntity extends BasePollinatorEntity {
    private static final int AGGRO_RANGE = 8;
    private static final float DAMAGE = 2.0f;

    @Override
    protected void initGoals() {
        super.initGoals();
        this.targetSelector.add(0, new DefendNestGoal(this));
    }

    @Override
    protected void onNestDestroyedBehavior() {
        // Attack the entity that broke the nest
        LivingEntity attacker = findNestBreaker();
        if (attacker != null) {
            this.setTarget(attacker);
        }
    }

    private class DefendNestGoal extends ActiveTargetGoal<LivingEntity> {
        public DefendNestGoal(DefensivePollinatorEntity entity) {
            super(entity, LivingEntity.class, 10, true, false,
                (target) -> target.squaredDistanceTo(linkedNest.toCenterPos()) < AGGRO_RANGE * AGGRO_RANGE
            );
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean hit = super.tryAttack(target);
        if (hit && target instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(
                StatusEffects.POISON, 60, 0 // 3 seconds, level 1
            ));
        }
        return hit;
    }
}
```

**Passive Pollinator:**
```java
public class PassivePollinatorEntity extends BasePollinatorEntity {
    private static final int DESPAWN_DELAY = 6000; // 5 minutes
    private int despawnTimer = 0;

    @Override
    protected void onNestDestroyedBehavior() {
        // Flee and eventually despawn
        this.goalSelector.add(0, new FleeEntityGoal<>(
            this, PlayerEntity.class, 16.0f, 1.5, 1.8
        ));
        despawnTimer = DESPAWN_DELAY;
    }

    @Override
    public void tick() {
        super.tick();
        if (nestDestroyed) {
            despawnTimer--;
            if (despawnTimer <= 0) {
                this.discard();
            }
        }
    }
}
```

---

### 2.2 Block System

#### 2.2.1 SpecialTreeBlock

**Key Features:**
- Stores linked nest position in NBT
- Manages fruit growth states
- Handles pollination events

```java
public class SpecialTreeBlock extends Block {
    public static final IntProperty FRUIT_STATE = IntProperty.of("fruit_state", 0, 2);
    // 0 = immature, 1 = pollinated, 2 = mature

    public SpecialTreeBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FRUIT_STATE, 0));
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int fruitState = state.get(FRUIT_STATE);

        if (fruitState == 0) {
            // Immature - check if we have a linked nest
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof SpecialTreeBlockEntity tree && tree.hasLinkedNest()) {
                // Can be pollinated - wait for pollinator
                return;
            }
        } else if (fruitState == 1) {
            // Pollinated - mature over time
            if (random.nextInt(5) == 0) { // 20% chance per random tick
                world.setBlockState(pos, state.with(FRUIT_STATE, 2));
            }
        }
        // State 2 (mature) - ready for harvest
    }

    public void pollinate(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.get(FRUIT_STATE) == 0) {
            world.setBlockState(pos, state.with(FRUIT_STATE, 1));

            // Particle effect
            if (world.isClient) {
                spawnPollinationParticles(world, pos);
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.get(FRUIT_STATE) == 2) {
            // Drop mature fruit
            dropStack(world, pos, new ItemStack(ModItems.getFruitForTree(this)));
        }
        super.onBreak(world, pos, state, player);
    }
}
```

**BlockEntity:**
```java
public class SpecialTreeBlockEntity extends BlockEntity {
    private BlockPos linkedNest;

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (linkedNest != null) {
            nbt.putLong("LinkedNest", linkedNest.asLong());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("LinkedNest")) {
            linkedNest = BlockPos.fromLong(nbt.getLong("LinkedNest"));
        }
    }

    public void linkToNest(BlockPos nestPos) {
        this.linkedNest = nestPos;
        markDirty();
    }

    public boolean hasLinkedNest() {
        if (linkedNest == null) return false;
        // Verify nest still exists
        return world.getBlockState(linkedNest).getBlock() instanceof PollinatorNestBlock;
    }
}
```

---

#### 2.2.2 PollinatorNestBlock

```java
public class PollinatorNestBlock extends Block {
    private final PollinatorType pollinatorType;

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            // Notify linked tree
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof PollinatorNestBlockEntity nest) {
                BlockPos treePos = nest.getLinkedTree();
                if (treePos != null) {
                    BlockEntity treeBe = world.getBlockEntity(treePos);
                    if (treeBe instanceof SpecialTreeBlockEntity tree) {
                        tree.onNestDestroyed();
                    }
                }

                // Notify pollinator entity
                List<BasePollinatorEntity> pollinators = world.getEntitiesByClass(
                    BasePollinatorEntity.class,
                    Box.of(pos.toCenterPos(), 32, 32, 32),
                    p -> p.linkedNest.equals(pos)
                );
                pollinators.forEach(BasePollinatorEntity::onNestDestroyed);
            }
        }

        super.onBreak(world, pos, state, player);
    }
}

public class PollinatorNestBlockEntity extends BlockEntity {
    private BlockPos linkedTree;

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (linkedTree != null) {
            nbt.putLong("LinkedTree", linkedTree.asLong());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("LinkedTree")) {
            linkedTree = BlockPos.fromLong(nbt.getLong("LinkedTree"));
        }
    }

    public BlockPos getLinkedTree() {
        return linkedTree;
    }
}
```

---

### 2.3 World Generation

#### 2.3.1 Biome Pair Feature

```java
public class BiomePairFeature extends Feature<DefaultFeatureConfig> {
    private final BiomePairRegistry registry;

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        ServerWorld world = (ServerWorld) context.getWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();

        // Get biome-specific pair
        Biome biome = world.getBiome(pos).value();
        BiomePair pair = registry.getPairForBiome(biome);
        if (pair == null) return false;

        // Generate tree
        BlockPos treePos = findSuitableTreePosition(world, pos, random);
        if (treePos == null) return false;
        placeTree(world, treePos, pair.getTreeType());

        // Generate nest nearby (within 20 blocks)
        BlockPos nestPos = findSuitableNestPosition(world, treePos, random, 20);
        if (nestPos == null) return false;
        placeNest(world, nestPos, pair.getPollinatorType());

        // Link tree and nest via NBT
        linkTreeAndNest(world, treePos, nestPos);

        return true;
    }

    private void linkTreeAndNest(ServerWorld world, BlockPos treePos, BlockPos nestPos) {
        // Link tree to nest
        BlockEntity treeBe = world.getBlockEntity(treePos);
        if (treeBe instanceof SpecialTreeBlockEntity tree) {
            tree.linkToNest(nestPos);
        }

        // Link nest to tree
        BlockEntity nestBe = world.getBlockEntity(nestPos);
        if (nestBe instanceof PollinatorNestBlockEntity nest) {
            nest.linkToTree(treePos);
        }
    }
}
```

**Biome Registry:**
```java
public class BiomePairRegistry {
    private static final Map<RegistryKey<Biome>, BiomePair> PAIRS = new HashMap<>();

    public static void register() {
        PAIRS.put(BiomeKeys.JUNGLE, new BiomePair(
            ModBlocks.FIG_TREE,
            ModEntities.FIG_WASP,
            ModItems.FIG
        ));

        PAIRS.put(BiomeKeys.DESERT, new BiomePair(
            ModBlocks.YUCCA_PLANT,
            ModEntities.YUCCA_MOTH,
            ModItems.YUCCA_POD
        ));

        // ... 8 more pairs
    }

    public BiomePair getPairForBiome(Biome biome) {
        return PAIRS.get(world.getRegistryManager()
            .get(RegistryKeys.BIOME)
            .getKey(biome)
            .orElse(null));
    }
}

public record BiomePair(
    Block treeType,
    EntityType<? extends BasePollinatorEntity> pollinatorType,
    Item fruitItem
) {}
```

---

### 2.4 Bee-Flower Pollination System

#### 2.4.1 Flower Pollination Mixin

```java
@Mixin(FlowerBlock.class)
public class FlowerBlockMixin {

    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onFlowerBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof FlowerBlockEntity flower) {
                if (flower.isPollinated()) {
                    // Drop 1-2 seeds
                    dropSeeds(world, pos, state, world.random.nextBetween(1, 2));
                }
                // Else: no seeds dropped
            }
        }
    }
}

// Custom BlockEntity for flowers
public class FlowerBlockEntity extends BlockEntity {
    private boolean pollinated = false;
    private long pollinationTime = 0;

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("Pollinated", pollinated);
        nbt.putLong("PollinationTime", pollinationTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        pollinated = nbt.getBoolean("Pollinated");
        pollinationTime = nbt.getLong("PollinationTime");
    }

    public void markPollinated() {
        this.pollinated = true;
        this.pollinationTime = world.getTime();
        markDirty();
    }

    public boolean isPollinated() {
        // Check if pollination is still valid (within 24 MC days)
        if (!pollinated) return false;
        long currentTime = world.getTime();
        return (currentTime - pollinationTime) < 24000;
    }
}
```

#### 2.4.2 Bee Behavior Hook

```java
@Mixin(BeeEntity.class)
public class BeeEntityMixin {

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void onBeeVisitFlower(CallbackInfo ci) {
        BeeEntity bee = (BeeEntity) (Object) this;

        if (bee.hasFlower() && bee.isFlowerPos(bee.getBlockPos())) {
            BlockEntity be = bee.world.getBlockEntity(bee.getBlockPos());
            if (be instanceof FlowerBlockEntity flower) {
                flower.markPollinated();

                // Spawn particles
                if (bee.world.isClient) {
                    spawnPollinationParticles(bee.world, bee.getBlockPos());
                }
            }
        }
    }
}
```

---

## 3. PERFORMANCE OPTIMIZATIONS

### 3.1 Entity Caps

```java
public class EntitySpawnManager {
    private static final int MAX_HONEYGUIDES_PER_AREA = 5;
    private static final int MAX_POLLINATORS_PER_TREE = 2;

    public static boolean canSpawnHoneyguide(ServerWorld world, BlockPos pos) {
        Box area = Box.of(pos.toCenterPos(), 256, 256, 256);
        long count = world.getEntitiesByClass(HoneyguideEntity.class, area, e -> true).size();
        return count < MAX_HONEYGUIDES_PER_AREA;
    }

    public static boolean canSpawnPollinator(ServerWorld world, BlockPos treePos) {
        Box area = Box.of(treePos.toCenterPos(), 32, 32, 32);
        long count = world.getEntitiesByClass(BasePollinatorEntity.class, area,
            e -> e.linkedTree.equals(treePos)
        ).size();
        return count < MAX_POLLINATORS_PER_TREE;
    }
}
```

### 3.2 Lazy Pathfinding Updates

```java
public class PollinateTreeGoal extends Goal {
    private int updateInterval = 20; // Update every second instead of every tick
    private int tickCounter = 0;

    @Override
    public void tick() {
        tickCounter++;
        if (tickCounter >= updateInterval) {
            // Update pathfinding
            entity.getNavigation().startMovingTo(targetTree, 1.0);
            tickCounter = 0;
        }
    }
}
```

### 3.3 Chunk Loading Optimization

```java
// Only generate biome pairs in chunks with specific structures
public class BiomePairFeaturePlacement {
    public static boolean shouldGeneratePair(StructureWorldAccess world, ChunkPos chunkPos) {
        // Use chunk seed for deterministic placement
        long seed = ChunkPos.toLong(chunkPos.x, chunkPos.z);
        Random random = new Random(seed);

        // 10% chance per chunk
        return random.nextDouble() < 0.1;
    }
}
```

---

## 4. DATA PERSISTENCE

### 4.1 NBT Tag Structures

**Tree-Nest Linkage:**
```
SpecialTreeBlockEntity: {
    LinkedNest: long (BlockPos as long)
}

PollinatorNestBlockEntity: {
    LinkedTree: long (BlockPos as long)
}
```

**Pollinator Entity:**
```
BasePollinatorEntity: {
    LinkedNest: long (BlockPos as long)
    LinkedTree: long (BlockPos as long)
    PollinationCooldown: int
    NestDestroyed: boolean
}
```

**Honeyguide Entity:**
```
HoneyguideEntity: {
    TargetPlayer: UUID
    TargetNest: long (BlockPos as long)
    PlayerMemory: [
        {
            Player: UUID
            Timestamp: long
        },
        ...
    ]
}
```

**Flower Pollination:**
```
FlowerBlockEntity: {
    Pollinated: boolean
    PollinationTime: long
}
```

---

## 5. NETWORKING (Client-Server Sync)

### 5.1 Packet Definitions

**Pollination Event Packet:**
```java
public record PollinationPacket(BlockPos treePos) {
    public static void send(ServerWorld world, BlockPos pos) {
        // Send to all players tracking this chunk
        PlayerLookup.tracking(world, pos).forEach(player -> {
            ServerPlayNetworking.send(player, new PollinationPacket(pos));
        });
    }

    public static void handle(MinecraftClient client, PollinationPacket packet) {
        client.execute(() -> {
            // Spawn particles on client
            ClientWorld world = client.world;
            if (world != null) {
                spawnPollinationParticles(world, packet.treePos());
            }
        });
    }
}
```

**Honeyguide Call Packet:**
```java
public record HoneyguideCallPacket(int entityId, Vec3d position) {
    public static void send(ServerWorld world, HoneyguideEntity bird) {
        PlayerLookup.around(world, bird.getPos(), 64.0).forEach(player -> {
            ServerPlayNetworking.send(player, new HoneyguideCallPacket(
                bird.getId(),
                bird.getPos()
            ));
        });
    }

    public static void handle(MinecraftClient client, HoneyguideCallPacket packet) {
        client.execute(() -> {
            Entity entity = client.world.getEntityById(packet.entityId());
            if (entity instanceof HoneyguideEntity bird) {
                bird.playSound(SoundEvents.HONEYGUIDE_CALL, 1.0f, 1.0f);
                // Spawn visual indicators (particles, etc.)
            }
        });
    }
}
```

---

## 6. TESTING STRATEGY

### 6.1 Unit Tests

```java
public class TreeNestLinkageTest {
    @Test
    public void testLinkageCreation() {
        // Create mock world
        ServerWorld world = createMockWorld();

        // Place tree and nest
        BlockPos treePos = new BlockPos(0, 64, 0);
        BlockPos nestPos = new BlockPos(10, 64, 5);

        world.setBlockState(treePos, ModBlocks.FIG_TREE.getDefaultState());
        world.setBlockState(nestPos, ModBlocks.FIG_WASP_NEST.getDefaultState());

        // Link them
        linkTreeAndNest(world, treePos, nestPos);

        // Verify linkage
        SpecialTreeBlockEntity tree = (SpecialTreeBlockEntity) world.getBlockEntity(treePos);
        assertEquals(nestPos, tree.getLinkedNest());

        PollinatorNestBlockEntity nest = (PollinatorNestBlockEntity) world.getBlockEntity(nestPos);
        assertEquals(treePos, nest.getLinkedTree());
    }

    @Test
    public void testPollinationCycle() {
        ServerWorld world = createMockWorld();
        BlockPos treePos = new BlockPos(0, 64, 0);

        // Initial state: immature fruit
        BlockState initialState = ModBlocks.FIG_TREE.getDefaultState()
            .with(SpecialTreeBlock.FRUIT_STATE, 0);
        world.setBlockState(treePos, initialState);

        // Pollinate
        ((SpecialTreeBlock) ModBlocks.FIG_TREE).pollinate(world, treePos);

        // Verify state changed to pollinated
        BlockState afterPollination = world.getBlockState(treePos);
        assertEquals(1, afterPollination.get(SpecialTreeBlock.FRUIT_STATE));

        // Simulate random ticks until mature
        for (int i = 0; i < 100; i++) {
            world.getBlockState(treePos).randomTick(world, treePos, world.random);
        }

        // Verify maturation
        BlockState finalState = world.getBlockState(treePos);
        assertEquals(2, finalState.get(SpecialTreeBlock.FRUIT_STATE));
    }
}
```

### 6.2 Integration Tests

```java
public class HoneyguideIntegrationTest {
    @Test
    public void testLeadingBehavior() {
        ServerWorld world = createTestWorld();

        // Spawn honeyguide
        HoneyguideEntity bird = new HoneyguideEntity(ModEntities.HONEYGUIDE, world);
        bird.setPosition(0, 64, 0);
        world.spawnEntity(bird);

        // Spawn player
        ServerPlayerEntity player = createTestPlayer(world);
        player.setPosition(5, 64, 5);

        // Place bee nest
        BlockPos nestPos = new BlockPos(50, 64, 50);
        world.setBlockState(nestPos, Blocks.BEE_NEST.getDefaultState());

        // Tick AI until bird starts leading
        for (int i = 0; i < 100; i++) {
            bird.tick();
        }

        // Verify bird is pathfinding toward nest
        assertTrue(bird.getNavigation().isFollowingPath());
        Vec3d targetPos = bird.getNavigation().getTargetPos().toCenterPos();
        assertEquals(nestPos.toCenterPos(), targetPos);
    }
}
```

---

## 7. CONFIGURATION SYSTEM

### 7.1 Config File Implementation

```java
public class SymbioticConfig {
    public static SymbioticConfig INSTANCE;

    // Honeyguide settings
    public double honeyguideSpawnRate = 0.002;
    public int honeyguideLeadingRange = 128;
    public int honeyguideMemoryDuration = 24000;

    // Pollination settings
    public int pollinationIntervalMin = 2400;
    public int pollinationIntervalMax = 6000;
    public int fruitMaturationTime = 3600;
    public int nestGenerationRange = 20;

    // Bee-flower pollination
    public boolean enableFlowerPollination = true;
    public boolean enableCropPollination = false;
    public boolean pollinationParticleEffect = true;

    // Balance
    public float defensiveWaspDamage = 2.0f;
    public int poisonDuration = 60;

    public static void load() {
        // Load from config file using Fabric's config API
        // or a library like AutoConfig
    }

    public static void save() {
        // Save to config file
    }
}
```

---

This technical specification provides the complete implementation blueprint for Phase 1. All major systems are detailed with code structures, algorithms, and integration points.
