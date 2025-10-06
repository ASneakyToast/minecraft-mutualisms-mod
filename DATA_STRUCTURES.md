# Data Structures Reference
## Symbiotic Survival Mod

---

## 1. NBT TAG STRUCTURES

### 1.1 Block Entities

#### SpecialTreeBlockEntity
**Description:** Stores pollination state and linkage for special trees

```nbt
{
    id: "symbiotic:special_tree",
    x: int,
    y: int,
    z: int,

    // Custom data
    LinkedNest: long,              // BlockPos.asLong() of linked nest
    PollinationState: byte,        // 0=immature, 1=pollinated, 2=mature
    LastPollinationTime: long,     // World time of last pollination
    SchemaVersion: int             // For future migrations
}
```

**Field Details:**
- `LinkedNest`: `null` if no nest linked (orphaned tree). When set, points to nest within ~20 blocks
- `PollinationState`:
  - `0` (Immature): Default state, waiting for pollination
  - `1` (Pollinated): Pollinator visited, fruit maturing
  - `2` (Mature): Ready for harvest
- `LastPollinationTime`: Used to calculate maturation progress
- `SchemaVersion`: Current version is `1`

---

#### PollinatorNestBlockEntity
**Description:** Stores tree linkage and spawned pollinator reference

```nbt
{
    id: "symbiotic:pollinator_nest",
    x: int,
    y: int,
    z: int,

    // Custom data
    LinkedTree: long,              // BlockPos.asLong() of linked tree
    PollinatorUUID: [I; int, int, int, int],  // UUID of spawned pollinator (optional)
    PollinatorType: string,        // "fig_wasp", "yucca_moth", etc.
    SchemaVersion: int
}
```

**Field Details:**
- `LinkedTree`: `null` if no tree linked. Points to special tree within ~20 blocks
- `PollinatorUUID`: UUID of the pollinator entity spawned from this nest. `null` if no pollinator currently active
- `PollinatorType`: Identifier for which pollinator species this nest houses

---

#### FlowerBlockEntity
**Description:** Tracks pollination state for vanilla flowers (added via mixin)

```nbt
{
    id: "symbiotic:pollinated_flower",
    x: int,
    y: int,
    z: int,

    // Custom data
    Pollinated: boolean,           // true if bee has visited
    PollinationTime: long,         // World time when pollinated
    SchemaVersion: int
}
```

**Field Details:**
- `Pollinated`: `true` = flower will drop seeds when broken
- `PollinationTime`: Used to calculate expiry (pollination valid for 24 MC days)
- Pollination expires if `(currentTime - PollinationTime) > 24000`

---

### 1.2 Entities

#### HoneyguideEntity
**Description:** Stores AI state and player interaction memory

```nbt
{
    // Vanilla entity data
    id: "symbiotic:honeyguide",
    UUID: [I; int, int, int, int],
    Pos: [double, double, double],
    Motion: [double, double, double],
    Rotation: [float, float],
    Health: float,

    // Custom data
    TargetPlayer: [I; int, int, int, int],  // UUID of player being led (optional)
    TargetNest: long,                       // BlockPos of bee nest target (optional)
    State: string,                          // "IDLE", "CALLING", "LEADING", "WAITING", "FEEDING"
    LeadingTimeout: int,                    // Ticks remaining before abandoning lead

    PlayerMemory: [                         // List of players who "betrayed" the bird
        {
            Player: [I; int, int, int, int],      // Player UUID
            BetrayalTimestamp: long,              // World time of betrayal
            MemoryExpiry: long                    // BetrayalTimestamp + 24000 (20 min)
        },
        ...
    ],

    SchemaVersion: int
}
```

**State Machine:**
- `IDLE`: Default state, wandering
- `CALLING`: Detected player, chirping to get attention
- `LEADING`: Actively leading player toward nest
- `WAITING`: At nest, waiting for player to catch up
- `FEEDING`: Eating larvae after player breaks nest

**Player Memory:**
- Bird won't lead a player in memory until `currentTime >= MemoryExpiry`
- Memory added when player collects all larvae without sharing

---

#### BasePollinatorEntity
**Description:** Stores linkage and pollination state for all pollinators

```nbt
{
    // Vanilla entity data
    id: "symbiotic:fig_wasp",  // or yucca_moth, etc.
    UUID: [I; int, int, int, int],
    Pos: [double, double, double],
    Motion: [double, double, double],
    Rotation: [float, float],
    Health: float,

    // Custom data
    LinkedNest: long,                       // BlockPos of home nest
    LinkedTree: long,                       // BlockPos of tree to pollinate
    PollinationCooldown: int,               // Ticks until next pollination
    NestDestroyed: boolean,                 // true if nest was broken
    DespawnTimer: int,                      // Ticks until despawn (passive only)

    // Defensive-specific (only for defensive types)
    AggroTarget: [I; int, int, int, int],   // UUID of entity that broke nest
    AggroTimeout: int,                      // Ticks remaining in aggro state

    SchemaVersion: int
}
```

**Field Details:**
- `PollinationCooldown`: Decrements each tick. When reaches 0, pollinator flies to tree
- `NestDestroyed`: Set to `true` when linked nest is broken. Triggers flee/attack behavior
- `DespawnTimer`: (Passive only) Set to 6000 ticks (5 minutes) when nest destroyed

---

### 1.3 Items

#### FruitItemStack
**Description:** Standard item NBT (no custom data currently, but reserved for future)

```nbt
{
    id: "symbiotic:fig",
    Count: byte,
    tag: {
        // Reserved for future use
        // Potential: Ripeness, Quality, etc.
    }
}
```

---

## 2. CONFIGURATION DATA STRUCTURES

### 2.1 Config File (TOML)

**File Location:** `config/symbiotic-survival.toml`

```toml
# Symbiotic Survival Configuration
# Version: 1.0.0

[honeyguide]
# Spawn rate probability per chunk per attempt
spawn_rate = 0.002

# Maximum distance honeyguide will lead player (in blocks)
leading_range = 128

# How long honeyguide remembers player betrayal (in ticks, 20 ticks = 1 second)
memory_duration = 24000  # 20 minutes

# Maximum number of honeyguides per 256-block radius
max_entities_per_area = 5

[pollination]
# Minimum ticks between pollination attempts
pollination_interval_min = 2400  # 2 minutes

# Maximum ticks between pollination attempts
pollination_interval_max = 6000  # 5 minutes

# Ticks for fruit to mature after pollination
fruit_maturation_time = 3600  # 3 minutes

# Maximum distance nest can be from tree during world gen
nest_generation_range = 20

# Maximum pollinators per tree
max_pollinators_per_tree = 2

[bees]
# Require bee pollination for flower seeds
enable_flower_pollination = true

# Require bee pollination for melon/pumpkin fruit (EXPERIMENTAL)
enable_crop_pollination = false

# Show particle effects when flowers are pollinated
pollination_particle_effect = true

# Duration pollination remains valid (in Minecraft days)
pollination_expiry_days = 1

[balance]
# Damage dealt by defensive pollinators (in half-hearts)
defensive_wasp_damage = 2.0

# Poison effect duration (in ticks)
poison_duration = 60  # 3 seconds

# Poison effect level (0 = Poison I, 1 = Poison II, etc.)
poison_level = 0

# Range at which defensive pollinators aggro (in blocks)
aggro_range = 8

# Time defensive pollinators stay aggressive (in ticks)
aggro_timeout = 600  # 30 seconds

[worldgen]
# Probability of biome pair spawning per chunk
biome_pair_spawn_chance = 0.1

# Enable/disable specific biome pairs
enable_jungle_fig = true
enable_desert_yucca = true
enable_savanna_acacia = true
enable_taiga_conifer = true
enable_plains_milkweed = true
enable_swamp_mangrove = true
enable_dark_forest_mushroom = true
enable_birch_forest_birch = true
enable_cherry_grove_cherry = true
enable_snowy_taiga_willow = true

[debug]
# Enable debug logging
enable_debug_logging = false

# Show linkage visualization (particles connecting tree and nest)
show_linkage_particles = false

# Disable entity caps (for testing)
ignore_entity_caps = false
```

---

### 2.2 In-Memory Config Structure

```java
public class SymbioticConfig {
    // Honeyguide
    public double honeyguideSpawnRate;
    public int honeyguideLeadingRange;
    public int honeyguideMemoryDuration;
    public int maxHoneyguidesPerArea;

    // Pollination
    public int pollinationIntervalMin;
    public int pollinationIntervalMax;
    public int fruitMaturationTime;
    public int nestGenerationRange;
    public int maxPollinatorsPerTree;

    // Bees
    public boolean enableFlowerPollination;
    public boolean enableCropPollination;
    public boolean pollinationParticleEffect;
    public int pollinationExpiryDays;

    // Balance
    public float defensiveWaspDamage;
    public int poisonDuration;
    public int poisonLevel;
    public int aggroRange;
    public int aggroTimeout;

    // World Gen
    public double biomePairSpawnChance;
    public Map<BiomeType, Boolean> enabledBiomePairs;

    // Debug
    public boolean enableDebugLogging;
    public boolean showLinkageParticles;
    public boolean ignoreEntityCaps;
}
```

---

## 3. REGISTRY DATA STRUCTURES

### 3.1 Biome Pair Registry

```java
public class BiomePairRegistry {
    private static final Map<RegistryKey<Biome>, BiomePair> PAIRS = new HashMap<>();

    public record BiomePair(
        Block treeBlock,                            // e.g., ModBlocks.FIG_TREE
        EntityType<? extends BasePollinatorEntity> pollinatorType,  // e.g., ModEntities.FIG_WASP
        Item fruitItem,                             // e.g., ModItems.FIG
        Block nestBlock,                            // e.g., ModBlocks.FIG_WASP_NEST
        boolean isDefensive                         // true = attacks, false = flees
    ) {}

    static {
        PAIRS.put(BiomeKeys.JUNGLE, new BiomePair(
            ModBlocks.FIG_TREE,
            ModEntities.FIG_WASP,
            ModItems.FIG,
            ModBlocks.FIG_WASP_NEST,
            true  // defensive
        ));

        PAIRS.put(BiomeKeys.DESERT, new BiomePair(
            ModBlocks.YUCCA_PLANT,
            ModEntities.YUCCA_MOTH,
            ModItems.YUCCA_POD,
            ModBlocks.YUCCA_MOTH_NEST,
            false  // passive
        ));

        // ... 8 more entries
    }
}
```

---

### 3.2 Entity Type Registry

```java
public class ModEntities {
    // Honeyguide
    public static final EntityType<HoneyguideEntity> HONEYGUIDE = register(
        "honeyguide",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, HoneyguideEntity::new)
            .dimensions(EntityDimensions.fixed(0.4f, 0.6f))
            .build()
    );

    // Defensive Pollinators
    public static final EntityType<DefensivePollinatorEntity> FIG_WASP = register(
        "fig_wasp",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE,
            (type, world) -> new DefensivePollinatorEntity(type, world, PollinatorType.FIG_WASP))
            .dimensions(EntityDimensions.fixed(0.3f, 0.3f))
            .build()
    );

    // Passive Pollinators
    public static final EntityType<PassivePollinatorEntity> YUCCA_MOTH = register(
        "yucca_moth",
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE,
            (type, world) -> new PassivePollinatorEntity(type, world, PollinatorType.YUCCA_MOTH))
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .build()
    );

    // ... 8 more pollinators
}
```

---

### 3.3 Block Registry

```java
public class ModBlocks {
    // Trees (10 types)
    public static final Block FIG_TREE = register("fig_tree",
        new SpecialTreeBlock(FabricBlockSettings.of(Material.WOOD)
            .strength(2.0f)
            .sounds(BlockSoundGroup.WOOD)));

    public static final Block YUCCA_PLANT = register("yucca_plant",
        new SpecialTreeBlock(FabricBlockSettings.of(Material.PLANT)
            .strength(1.0f)
            .sounds(BlockSoundGroup.GRASS)));

    // ... 8 more trees

    // Nests (10 types)
    public static final Block FIG_WASP_NEST = register("fig_wasp_nest",
        new PollinatorNestBlock(FabricBlockSettings.of(Material.WOOD)
            .strength(0.5f)
            .sounds(BlockSoundGroup.WOOD),
            PollinatorType.FIG_WASP));

    // ... 9 more nests
}
```

---

### 3.4 Item Registry

```java
public class ModItems {
    // Fruits (10 types)
    public static final Item FIG = register("fig",
        new FoodFruitItem(new FabricItemSettings()
            .food(new FoodComponent.Builder()
                .hunger(4)
                .saturationModifier(5.0f)
                .build()),
            BiomeType.JUNGLE));

    public static final Item YUCCA_POD = register("yucca_pod",
        new UtilityFruitItem(new FabricItemSettings(),
            BiomeType.DESERT));

    // ... 8 more fruits

    // Saplings (10 types)
    public static final Item FIG_SAPLING = register("fig_sapling",
        new BlockItem(ModBlocks.FIG_TREE, new FabricItemSettings()));

    // ... 9 more saplings
}
```

---

## 4. NETWORKING DATA STRUCTURES

### 4.1 Packet Definitions

#### S2C_PollinationEvent
**Description:** Server notifies client of pollination for particle effects

```java
public record S2C_PollinationEvent(BlockPos treePos, PollinatorType pollinatorType)
    implements FabricPacket {

    public static final PacketType<S2C_PollinationEvent> TYPE =
        PacketType.create(new Identifier("symbiotic", "pollination_event"),
            S2C_PollinationEvent::new);

    // Serialization
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(treePos);
        buf.writeEnumConstant(pollinatorType);
    }

    // Deserialization
    public S2C_PollinationEvent(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readEnumConstant(PollinatorType.class));
    }
}
```

---

#### S2C_HoneyguideCall
**Description:** Server notifies client to play honeyguide sound

```java
public record S2C_HoneyguideCall(int entityId, Vec3d position, CallType callType)
    implements FabricPacket {

    public enum CallType {
        ATTENTION,  // Initial call to attract player
        LEADING,    // Call while leading
        WAITING     // Call while waiting at nest
    }

    public static final PacketType<S2C_HoneyguideCall> TYPE =
        PacketType.create(new Identifier("symbiotic", "honeyguide_call"),
            S2C_HoneyguideCall::new);

    public void write(PacketByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeEnumConstant(callType);
    }

    public S2C_HoneyguideCall(PacketByteBuf buf) {
        this(
            buf.readVarInt(),
            new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()),
            buf.readEnumConstant(CallType.class)
        );
    }
}
```

---

#### S2C_NestDestroyed
**Description:** Server notifies client of nest destruction for effects

```java
public record S2C_NestDestroyed(BlockPos nestPos, PollinatorType pollinatorType)
    implements FabricPacket {

    public static final PacketType<S2C_NestDestroyed> TYPE =
        PacketType.create(new Identifier("symbiotic", "nest_destroyed"),
            S2C_NestDestroyed::new);

    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(nestPos);
        buf.writeEnumConstant(pollinatorType);
    }

    public S2C_NestDestroyed(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readEnumConstant(PollinatorType.class));
    }
}
```

---

## 5. ENUMERATIONS

### 5.1 PollinatorType

```java
public enum PollinatorType {
    FIG_WASP(true, "Fig Wasp", BiomeKeys.JUNGLE),
    YUCCA_MOTH(false, "Yucca Moth", BiomeKeys.DESERT),
    MASON_WASP(true, "Mason Wasp", BiomeKeys.SAVANNA),
    SAWFLY(false, "Sawfly", BiomeKeys.TAIGA),
    MONARCH(false, "Monarch Butterfly", BiomeKeys.PLAINS),
    MANGROVE_BEE(true, "Mangrove Bee", BiomeKeys.SWAMP),
    FUNGUS_GNAT(false, "Fungus Gnat", BiomeKeys.DARK_FOREST),
    BIRCH_BEE(true, "Birch Bee", BiomeKeys.BIRCH_FOREST),
    ORCHARD_BEE(true, "Orchard Bee", BiomeKeys.CHERRY_GROVE),
    ARCTIC_BUMBLEBEE(false, "Arctic Bumblebee", BiomeKeys.SNOWY_TAIGA);

    private final boolean defensive;
    private final String displayName;
    private final RegistryKey<Biome> biome;

    PollinatorType(boolean defensive, String displayName, RegistryKey<Biome> biome) {
        this.defensive = defensive;
        this.displayName = displayName;
        this.biome = biome;
    }

    public boolean isDefensive() { return defensive; }
    public String getDisplayName() { return displayName; }
    public RegistryKey<Biome> getBiome() { return biome; }
}
```

---

### 5.2 FruitState

```java
public enum FruitState {
    IMMATURE(0, "immature", 0xA0A080),      // Dull greenish-gray
    POLLINATED(1, "pollinated", 0xC0C090),  // Slightly brighter
    MATURE(2, "mature", 0xFFD080);          // Vibrant color

    private final int id;
    private final String name;
    private final int color;  // RGB color for rendering

    FruitState(int id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getColor() { return color; }

    public static FruitState fromId(int id) {
        return Arrays.stream(values())
            .filter(state -> state.id == id)
            .findFirst()
            .orElse(IMMATURE);
    }
}
```

---

## 6. WORLD SAVE DATA

### 6.1 Level Data (Optional Global State)

**File Location:** `world/data/symbiotic.dat`

```nbt
{
    // Global statistics (optional feature)
    Statistics: {
        TotalNestsGenerated: long,
        TotalNestsDestroyed: long,
        TotalNestsPreserved: long,
        TotalPollinationEvents: long,
        TotalFruitsHarvested: long
    },

    // Chunk generation tracking (prevent duplicate generation)
    GeneratedChunks: [
        {
            ChunkX: int,
            ChunkZ: int,
            BiomePairsGenerated: int
        },
        ...
    ],

    SchemaVersion: int
}
```

---

This comprehensive data structures reference covers all NBT formats, config schemas, registries, and networking packets used in the mod. Use this as a reference when implementing persistence and serialization logic.
