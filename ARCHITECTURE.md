# System Architecture Document
## Symbiotic Survival Mod

---

## 1. HIGH-LEVEL ARCHITECTURE

### 1.1 System Overview

The Symbiotic Survival mod is built as a Fabric mod for Minecraft 1.21+, designed with a modular architecture that separates concerns across entities, blocks, world generation, and game mechanics.

**Core Principles:**
- **Separation of Concerns:** Entities, blocks, and world gen are independent modules
- **Data-Driven Design:** NBT tags store all persistent state
- **Event-Driven:** Block breaks, entity spawns trigger cascading behaviors
- **Performance-First:** Entity caps, lazy updates, efficient pathfinding

---

## 2. MODULE BREAKDOWN

### 2.1 Module Dependency Graph

```
┌─────────────────────────────────────────────────────────┐
│                    Minecraft Core                       │
│                    (Fabric API)                         │
└────────────────────┬────────────────────────────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
┌───────▼────────┐       ┌────────▼────────┐
│   World Gen    │       │   Game Events   │
│    Module      │       │     Module      │
└───────┬────────┘       └────────┬────────┘
        │                         │
        │  ┌──────────────────────┘
        │  │
┌───────▼──▼────────┐
│  Entity Module    │
│  - Honeyguide     │
│  - Pollinators    │
└───────┬───────────┘
        │
┌───────▼───────────┐
│  Block Module     │
│  - Trees          │
│  - Nests          │
│  - Flowers        │
└───────┬───────────┘
        │
┌───────▼───────────┐
│  Item Module      │
│  - Fruits         │
│  - Crafting       │
└───────────────────┘
        │
┌───────▼───────────┐
│  Config Module    │
└───────────────────┘
```

---

### 2.2 Module Specifications

#### 2.2.1 World Generation Module

**Responsibility:** Generate biome-specific tree-nest pairs during world creation

**Components:**
- `BiomePairFeature` - Main feature generator
- `BiomePairRegistry` - Maps biomes to tree/pollinator types
- `TreeStructure` - Defines tree shape and placement
- `NestStructure` - Defines nest placement relative to trees

**Interfaces:**
```java
public interface IWorldGenFeature {
    boolean generate(FeatureContext context);
    boolean canGenerateInBiome(Biome biome);
}

public interface IBiomePairProvider {
    BiomePair getPairForBiome(Biome biome);
    List<Biome> getSupportedBiomes();
}
```

**Data Flow:**
1. Chunk generation triggers feature placement
2. `BiomePairFeature` checks if biome supports pairs
3. Tree is placed at suitable location
4. Nest is placed within configured range
5. Both blocks' BlockEntities are linked via NBT

---

#### 2.2.2 Entity Module

**Responsibility:** Implement AI behaviors for honeyguides and pollinators

**Components:**

**Honeyguide Subsystem:**
- `HoneyguideEntity` - Main entity class
- `FindNestGoal` - AI goal to locate bee nests
- `CallPlayerGoal` - AI goal to attract player attention
- `LeadToNestGoal` - AI goal to pathfind toward nest
- `FeedOnLarvaeGoal` - AI goal to collect dropped larvae
- `PlayerMemoryComponent` - Tracks player "betrayals"

**Pollinator Subsystem:**
- `BasePollinatorEntity` - Abstract base class
- `DefensivePollinatorEntity` - Wasps/bees that attack
- `PassivePollinatorEntity` - Moths/butterflies that flee
- `PollinateTreeGoal` - AI goal to visit linked tree
- `ReturnToNestGoal` - AI goal to return to nest
- `DefendNestGoal` - AI goal to attack nest destroyers

**Interfaces:**
```java
public interface ILinkableEntity {
    void setLinkedNest(BlockPos nest);
    void setLinkedTree(BlockPos tree);
    BlockPos getLinkedNest();
    BlockPos getLinkedTree();
}

public interface IPollinatorBehavior {
    void pollinateTree();
    void onNestDestroyed();
    int getPollinationCooldown();
}

public interface IPlayerMemory {
    void recordBetrayal(UUID player);
    boolean canLeadPlayer(UUID player);
    void clearMemory(UUID player);
}
```

**State Machine (Honeyguide):**
```
IDLE → (sees player near nest) → CALLING
CALLING → (player approaches) → LEADING
LEADING → (reaches nest) → WAITING
WAITING → (player breaks nest) → FEEDING
FEEDING → (all larvae eaten) → IDLE

LEADING → (player betrays) → REMEMBER_BETRAYAL → IDLE
```

---

#### 2.2.3 Block Module

**Responsibility:** Define special trees, nests, and flower behaviors

**Components:**

**Tree Subsystem:**
- `SpecialTreeBlock` - Block with fruit states
- `SpecialTreeBlockEntity` - Stores linkage and pollination state
- `FruitStateProperty` - Block property for immature/pollinated/mature

**Nest Subsystem:**
- `PollinatorNestBlock` - Nest block for each pollinator type
- `PollinatorNestBlockEntity` - Stores linkage to tree
- `NestDestructionEvent` - Fired when nest is broken

**Flower Subsystem:**
- `FlowerBlockEntity` - Added to vanilla flowers via mixin
- `PollinationTracker` - Tracks which flowers have been pollinated

**Interfaces:**
```java
public interface ILinkableBlock {
    void linkToNest(BlockPos nestPos);
    void linkToTree(BlockPos treePos);
    BlockPos getLinkedNest();
    BlockPos getLinkedTree();
}

public interface IPollinatableBlock {
    void pollinate(World world, BlockPos pos);
    boolean isPollinated();
    int getFruitState();
}

public interface INestBlock {
    void onNestBroken(World world, BlockPos pos, PlayerEntity breaker);
    PollinatorType getPollinatorType();
}
```

**Fruit State Transitions:**
```
IMMATURE (0) → [pollination event] → POLLINATED (1) → [random tick] → MATURE (2)
                                                                        ↓
                                                                   [player breaks]
                                                                        ↓
                                                                  Drop fruit item
```

---

#### 2.2.4 Item Module

**Responsibility:** Define fruit items and their crafting uses

**Components:**
- `BaseFruitItem` - Abstract base class for all fruits
- `FoodFruitItem` - Fruits that are food (figs, cherries, etc.)
- `UtilityFruitItem` - Fruits for crafting (yucca pods, pine cones, etc.)
- `CraftingRecipeRegistry` - Registers all fruit-related recipes

**Item Properties:**
```java
public abstract class BaseFruitItem extends Item {
    protected final BiomeType sourcebiome;
    protected final int nutrition;
    protected final float saturation;

    public abstract List<Recipe> getCraftingRecipes();
}
```

---

#### 2.2.5 Config Module

**Responsibility:** Provide runtime configuration for all features

**Components:**
- `SymbioticConfig` - Main config class
- `ConfigScreen` - In-game config UI (via Cloth Config or similar)
- `ConfigSerializer` - Save/load TOML files

**Config Categories:**
```toml
[honeyguide]
spawn_rate = 0.002
leading_range = 128
memory_duration = 24000

[pollination]
pollination_interval_min = 2400
pollination_interval_max = 6000
fruit_maturation_time = 3600

[bees]
enable_flower_pollination = true
enable_crop_pollination = false

[balance]
defensive_wasp_damage = 2.0
poison_duration = 60

[worldgen]
biome_pair_spawn_chance = 0.1
nest_placement_range = 20
```

---

## 3. DATA ARCHITECTURE

### 3.1 NBT Schema

**SpecialTreeBlockEntity:**
```nbt
{
    LinkedNest: long,           // BlockPos as long
    PollinationState: byte,     // 0=immature, 1=pollinated, 2=mature
    LastPollinationTime: long   // World time of last pollination
}
```

**PollinatorNestBlockEntity:**
```nbt
{
    LinkedTree: long,           // BlockPos as long
    PollinatorUUID: UUID        // UUID of spawned pollinator (if any)
}
```

**BasePollinatorEntity:**
```nbt
{
    LinkedNest: long,
    LinkedTree: long,
    PollinationCooldown: int,
    NestDestroyed: boolean
}
```

**HoneyguideEntity:**
```nbt
{
    TargetPlayer: UUID,
    TargetNest: long,
    PlayerMemory: [
        {
            Player: UUID,
            BetrayalTimestamp: long
        }
    ],
    State: string               // IDLE, CALLING, LEADING, WAITING, FEEDING
}
```

**FlowerBlockEntity:**
```nbt
{
    Pollinated: boolean,
    PollinationTime: long       // World time when pollinated
}
```

---

### 3.2 Data Synchronization

**Client-Server Sync Strategy:**

1. **Block States (Trees, Nests):**
   - Server is authoritative
   - Block state changes synced via vanilla mechanisms
   - BlockEntity data synced on chunk load and update

2. **Entity Data (Pollinators, Honeyguides):**
   - Entity data trackers for visual state (AI state, target, etc.)
   - NBT data sent on entity spawn
   - Custom packets for particle effects and sounds

3. **Pollination Events:**
   - Server broadcasts custom packet to nearby clients
   - Clients spawn particles and play sounds
   - Server updates block state

**Packet Types:**
```java
// S2C: Server to Client
S2C_PollinationEvent (BlockPos treePos)
S2C_HoneyguideCall (int entityId, Vec3d position)
S2C_NestDestroyed (BlockPos nestPos, PollinatorType type)

// C2S: Client to Server (minimal - server is authoritative)
C2S_ConfigUpdate (ConfigData newConfig) // Admin only
```

---

## 4. PERFORMANCE ARCHITECTURE

### 4.1 Entity Performance

**Strategies:**
1. **Entity Caps:** Limit active entities per area
   - Max 5 honeyguides per 256-block radius
   - Max 2 pollinators per tree

2. **Lazy Updates:** Pathfinding updates every 20 ticks (1 second)

3. **Entity Pooling:** Reuse entity instances when possible

4. **Despawn Logic:** Passive pollinators despawn 5 minutes after nest destruction

**Implementation:**
```java
public class EntityPerformanceManager {
    private static final LoadingCache<ChunkPos, EntityCount> ENTITY_COUNTS =
        CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                public EntityCount load(ChunkPos chunk) {
                    return new EntityCount();
                }
            });

    public static boolean canSpawn(EntityType type, ServerWorld world, BlockPos pos) {
        ChunkPos chunk = new ChunkPos(pos);
        EntityCount count = ENTITY_COUNTS.get(chunk);
        return count.canSpawn(type);
    }
}
```

---

### 4.2 Block Performance

**Strategies:**
1. **Random Tick Optimization:** Fruit maturation uses random tick (like crops)
2. **Lazy NBT Lookups:** Cache linked positions in memory
3. **Chunk Unload Handling:** Properly save/load BlockEntity data

**Tick Budget:**
- Tree fruit maturation: Random tick (1/4096 blocks per chunk per tick)
- Pollination checks: Every 100 ticks (5 seconds)
- Flower spreading: Random tick

---

### 4.3 World Generation Performance

**Strategies:**
1. **Probabilistic Generation:** 10% chance per chunk (configurable)
2. **Structure Caching:** Cache generated structures for reuse
3. **Async Generation:** Use vanilla async chunk generation

**Generation Budget:**
- Max 1 biome pair per chunk
- Tree placement: <100ms per structure
- Total generation time: <500ms per chunk

---

## 5. EXTENSIBILITY ARCHITECTURE

### 5.1 Plugin Points

**Adding New Biome Pairs:**
```java
public class CustomBiomePairAddon {
    public static void register() {
        BiomePairRegistry.register(
            CustomBiomes.TROPICAL_FOREST,
            new BiomePair(
                CustomBlocks.CACAO_TREE,
                CustomEntities.CACAO_MIDGE,
                CustomItems.CACAO_POD
            )
        );
    }
}
```

**Adding New Pollinator Behaviors:**
```java
public class CustomPollinatorEntity extends BasePollinatorEntity {
    @Override
    protected void onNestDestroyedBehavior() {
        // Custom behavior: summon reinforcements!
        spawnReinforcements();
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new CustomSwarmGoal(this));
    }
}
```

**Adding New Fruit Properties:**
```java
public class CustomFruitItem extends BaseFruitItem {
    @Override
    public List<Recipe> getCraftingRecipes() {
        return List.of(
            new ShapelessRecipeJsonBuilder()
                .ingredient(this)
                .ingredient(Items.SUGAR)
                .output(CustomItems.FRUIT_JAM)
                .build()
        );
    }
}
```

---

### 5.2 API Surface

**Public APIs for Other Mods:**
```java
public interface SymbioticAPI {
    // Pollination
    void registerPollinatable(Block block, IPollinatableBlock behavior);
    void triggerPollination(World world, BlockPos pos);

    // Biome Pairs
    void registerBiomePair(Biome biome, BiomePair pair);
    BiomePair getBiomePair(Biome biome);

    // Entity Behaviors
    void registerCustomPollinator(EntityType type, PollinatorBehavior behavior);

    // Config
    SymbioticConfig getConfig();
    void updateConfig(SymbioticConfig newConfig);
}
```

---

## 6. ERROR HANDLING & RESILIENCE

### 6.1 Fault Tolerance

**Missing Linkages:**
```java
public class LinkageValidator {
    public static boolean validateTreeNestLink(World world, BlockPos treePos) {
        BlockEntity be = world.getBlockEntity(treePos);
        if (!(be instanceof SpecialTreeBlockEntity tree)) return false;

        BlockPos nestPos = tree.getLinkedNest();
        if (nestPos == null) {
            // Nest was never linked - log warning
            LOGGER.warn("Tree at {} has no linked nest", treePos);
            return false;
        }

        // Verify nest still exists
        BlockState nestState = world.getBlockState(nestPos);
        if (!(nestState.getBlock() instanceof PollinatorNestBlock)) {
            // Nest was destroyed - mark tree as unpollinated
            tree.onNestDestroyed();
            return false;
        }

        return true;
    }
}
```

**Entity Cleanup:**
```java
public class PollinatorCleanupTask {
    public static void cleanupOrphanedPollinators(ServerWorld world) {
        List<BasePollinatorEntity> orphans = world.getEntitiesByClass(
            BasePollinatorEntity.class,
            world.getWorldBorder().asBox(),
            pollinator -> {
                BlockPos nest = pollinator.getLinkedNest();
                return nest == null ||
                    !(world.getBlockState(nest).getBlock() instanceof PollinatorNestBlock);
            }
        );

        orphans.forEach(pollinator -> {
            LOGGER.info("Removing orphaned pollinator at {}", pollinator.getPos());
            pollinator.onNestDestroyed(); // Trigger cleanup behavior
        });
    }
}
```

---

### 6.2 Version Migration

**NBT Schema Versioning:**
```java
public class NBTMigration {
    private static final int CURRENT_VERSION = 1;

    public static NbtCompound migrateTreeBlockEntity(NbtCompound nbt) {
        int version = nbt.getInt("SchemaVersion");

        if (version < 1) {
            // Migration from pre-versioned schema
            // Old: "NestPos" was a string
            // New: "LinkedNest" is a long
            if (nbt.contains("NestPos")) {
                BlockPos oldPos = BlockPos.fromString(nbt.getString("NestPos"));
                nbt.putLong("LinkedNest", oldPos.asLong());
                nbt.remove("NestPos");
            }
        }

        nbt.putInt("SchemaVersion", CURRENT_VERSION);
        return nbt;
    }
}
```

---

## 7. SECURITY & ANTI-CHEAT

### 7.1 Server Authority

**All gameplay-critical logic runs server-side:**
- Pollination state transitions
- Fruit drops
- Entity spawning
- Memory/betrayal tracking

**Client is only trusted for:**
- Rendering
- Particle effects
- Sound playback

### 7.2 Validation

**Input Validation:**
```java
public class ConfigValidator {
    public static SymbioticConfig validate(SymbioticConfig config) {
        // Clamp values to reasonable ranges
        config.honeyguideSpawnRate = MathHelper.clamp(config.honeyguideSpawnRate, 0.0, 1.0);
        config.honeyguideLeadingRange = MathHelper.clamp(config.honeyguideLeadingRange, 16, 512);
        config.defensiveWaspDamage = MathHelper.clamp(config.defensiveWaspDamage, 0.5f, 20.0f);

        return config;
    }
}
```

**Entity Spawn Validation:**
```java
public class SpawnValidator {
    public static boolean canSpawnPollinator(ServerWorld world, BlockPos nestPos) {
        // Verify nest exists
        if (!(world.getBlockState(nestPos).getBlock() instanceof PollinatorNestBlock)) {
            return false;
        }

        // Verify not already spawned
        PollinatorNestBlockEntity nest = (PollinatorNestBlockEntity) world.getBlockEntity(nestPos);
        if (nest.hasActivepolinator()) {
            return false;
        }

        // Verify entity cap
        return EntityPerformanceManager.canSpawn(ModEntities.POLLINATOR, world, nestPos);
    }
}
```

---

## 8. MONITORING & DEBUGGING

### 8.1 Debug Commands

```java
@Command("symbiotic")
public class SymbioticCommands {

    @SubCommand("status")
    public void showStatus(ServerCommandSource source) {
        ServerWorld world = source.getWorld();

        // Count active entities
        int honeyguides = world.getEntitiesByType(ModEntities.HONEYGUIDE, e -> true).size();
        int pollinators = world.getEntitiesByClass(BasePollinatorEntity.class,
            world.getWorldBorder().asBox(), e -> true).size();

        source.sendFeedback(Text.literal(
            "Active Honeyguides: " + honeyguides + "\n" +
            "Active Pollinators: " + pollinators
        ), false);
    }

    @SubCommand("validate-links")
    public void validateLinks(ServerCommandSource source) {
        ServerWorld world = source.getWorld();

        // Find all trees and validate linkages
        int valid = 0;
        int broken = 0;

        for (BlockEntity be : world.blockEntities) {
            if (be instanceof SpecialTreeBlockEntity tree) {
                if (LinkageValidator.validateTreeNestLink(world, be.getPos())) {
                    valid++;
                } else {
                    broken++;
                }
            }
        }

        source.sendFeedback(Text.literal(
            "Valid links: " + valid + "\n" +
            "Broken links: " + broken
        ), false);
    }

    @SubCommand("spawn-pair")
    public void spawnPair(ServerCommandSource source, BiomeType biome) {
        // Manually spawn a biome pair at player's location (admin only)
        ServerPlayerEntity player = source.getPlayer();
        BiomePair pair = BiomePairRegistry.getPair(biome);

        BlockPos playerPos = player.getBlockPos();
        // Place tree
        BlockPos treePos = findSuitableTreePosition(source.getWorld(), playerPos);
        source.getWorld().setBlockState(treePos, pair.tree().getDefaultState());

        // Place nest
        BlockPos nestPos = findSuitableNestPosition(source.getWorld(), treePos, 20);
        source.getWorld().setBlockState(nestPos, pair.nest().getDefaultState());

        // Link
        linkTreeAndNest(source.getWorld(), treePos, nestPos);

        source.sendFeedback(Text.literal("Spawned " + biome + " pair"), true);
    }
}
```

---

### 8.2 Logging Strategy

**Log Levels:**
- **ERROR:** Game-breaking issues (corrupted NBT, missing required data)
- **WARN:** Recoverable issues (broken linkages, orphaned entities)
- **INFO:** Normal operations (biome pair spawned, pollination occurred)
- **DEBUG:** Detailed AI behavior (pathfinding, goal transitions)

**Example:**
```java
private static final Logger LOGGER = LoggerFactory.getLogger("SymbioticSurvival");

public void pollinateTree() {
    if (linkedTree == null) {
        LOGGER.error("Pollinator {} attempted to pollinate but has no linked tree", this.getUuid());
        return;
    }

    BlockState treeState = world.getBlockState(linkedTree);
    if (!(treeState.getBlock() instanceof SpecialTreeBlock)) {
        LOGGER.warn("Linked tree at {} is no longer a SpecialTreeBlock", linkedTree);
        onNestDestroyed();
        return;
    }

    LOGGER.debug("Pollinator {} pollinating tree at {}", this.getUuid(), linkedTree);
    ((SpecialTreeBlock) treeState.getBlock()).pollinate(world, linkedTree);
}
```

---

## 9. DEPLOYMENT ARCHITECTURE

### 9.1 Build Pipeline

```
Source Code → Gradle Build → JAR Artifact → CurseForge/Modrinth
                    ↓
                Unit Tests
                    ↓
            Integration Tests
                    ↓
            Performance Tests
```

### 9.2 Distribution

**Platforms:**
- CurseForge (primary)
- Modrinth (secondary)
- GitHub Releases (source + binary)

**Dependencies:**
- Fabric Loader >= 0.15.0
- Fabric API >= 0.92.0
- Minecraft 1.21.x

---

This architecture document provides a complete blueprint for how all systems interact, how data flows through the mod, and how to extend/maintain the codebase.
