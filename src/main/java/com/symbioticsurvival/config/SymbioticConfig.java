package com.symbioticsurvival.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "symbioticsurvival")
public class SymbioticConfig implements ConfigData {

    @Comment("Honeyguide Bird Settings")
    @ConfigEntry.Gui.CollapsibleObject
    public HoneyguideConfig honeyguide = new HoneyguideConfig();

    @Comment("Pollination System Settings")
    @ConfigEntry.Gui.CollapsibleObject
    public PollinationConfig pollination = new PollinationConfig();

    @Comment("Bee-Flower Pollination Settings")
    @ConfigEntry.Gui.CollapsibleObject
    public BeePollinationConfig beePollination = new BeePollinationConfig();

    @Comment("Balance & Combat Settings")
    @ConfigEntry.Gui.CollapsibleObject
    public BalanceConfig balance = new BalanceConfig();

    @Comment("World Generation Settings")
    @ConfigEntry.Gui.CollapsibleObject
    public WorldGenConfig worldGen = new WorldGenConfig();

    @Comment("Debug Settings")
    @ConfigEntry.Gui.CollapsibleObject
    public DebugConfig debug = new DebugConfig();

    public static class HoneyguideConfig {
        @Comment("Spawn rate (probability per chunk, 0-100)")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int spawnRate = 2;

        @Comment("Leading range in blocks")
        @ConfigEntry.BoundedDiscrete(min = 32, max = 512)
        public int leadingRange = 128;

        @Comment("Memory duration in ticks (20 ticks = 1 second)")
        @ConfigEntry.BoundedDiscrete(min = 6000, max = 72000)
        public int memoryDuration = 24000;

        @Comment("Max honeyguides per area")
        @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
        public int maxPerArea = 5;
    }

    public static class PollinationConfig {
        @Comment("Min pollination interval in ticks")
        @ConfigEntry.BoundedDiscrete(min = 600, max = 12000)
        public int intervalMin = 2400;

        @Comment("Max pollination interval in ticks")
        @ConfigEntry.BoundedDiscrete(min = 1200, max = 24000)
        public int intervalMax = 6000;

        @Comment("Fruit maturation time in ticks")
        @ConfigEntry.BoundedDiscrete(min = 1200, max = 24000)
        public int fruitMaturationTime = 3600;

        @Comment("Nest generation range in blocks")
        @ConfigEntry.BoundedDiscrete(min = 5, max = 64)
        public int nestGenerationRange = 20;

        @Comment("Max pollinators per tree")
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
        public int maxPollinatorsPerTree = 2;
    }

    public static class BeePollinationConfig {
        @Comment("Enable flower pollination by bees")
        public boolean enableFlowerPollination = true;

        @Comment("Enable crop pollination by bees")
        public boolean enableCropPollination = false;

        @Comment("Show pollination particle effects")
        public boolean pollinationParticleEffect = true;

        @Comment("Pollination expiry time in days")
        @ConfigEntry.BoundedDiscrete(min = 1, max = 30)
        public int pollinationExpiryDays = 3;
    }

    public static class BalanceConfig {
        @Comment("Defensive wasp damage")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
        public int defensiveWaspDamage = 2;

        @Comment("Poison duration in ticks")
        @ConfigEntry.BoundedDiscrete(min = 20, max = 600)
        public int poisonDuration = 60;

        @Comment("Poison effect level (0-4)")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 4)
        public int poisonLevel = 1;

        @Comment("Aggro range in blocks")
        @ConfigEntry.BoundedDiscrete(min = 5, max = 32)
        public int aggroRange = 16;

        @Comment("Aggro timeout in ticks")
        @ConfigEntry.BoundedDiscrete(min = 100, max = 6000)
        public int aggroTimeout = 600;
    }

    public static class WorldGenConfig {
        @Comment("Biome pair spawn chance (0.0-1.0)")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int biomePairSpawnChance = 30; // Stored as percentage

        @Comment("Enable tropical biome pairs (Fig/Wasp)")
        public boolean enableTropical = true;

        @Comment("Enable desert biome pairs (Yucca/Moth)")
        public boolean enableDesert = true;

        @Comment("Enable savanna biome pairs (Acacia/Wasp)")
        public boolean enableSavanna = true;

        @Comment("Enable taiga biome pairs (Conifer/Sawfly)")
        public boolean enableTaiga = true;

        @Comment("Enable plains biome pairs (Milkweed/Monarch)")
        public boolean enablePlains = true;
    }

    public static class DebugConfig {
        @Comment("Enable debug logging")
        public boolean enableDebugLogging = false;

        @Comment("Show linkage particles")
        public boolean showLinkageParticles = false;

        @Comment("Ignore entity spawn caps (for testing)")
        public boolean ignoreEntityCaps = false;
    }
}
