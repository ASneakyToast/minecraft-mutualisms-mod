package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

/**
 * Configures natural spawning for all custom entities.
 */
public class ModEntitySpawns {

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering entity spawns for " + SymbioticSurvival.MOD_ID);

        // Configure spawn restrictions (WHERE entities can spawn)
        registerSpawnRestrictions();

        // Configure biome spawning (WHICH biomes entities spawn in)
        registerBiomeSpawns();
    }

    private static void registerSpawnRestrictions() {
        // All pollinators spawn on ground during daylight (like vanilla animals)
        SpawnRestriction.register(
            ModEntities.FIG_WASP,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.YUCCA_MOTH,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.MONARCH_BUTTERFLY,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.MASON_WASP,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.SAWFLY,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.MANGROVE_POLLINATOR,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.FUNGUS_GNAT,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.BIRCH_POLLINATOR,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.ORCHARD_BEE,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.BUMBLEBEE,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
            ModEntities.HONEYGUIDE,
            SpawnLocationTypes.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AnimalEntity::isValidNaturalSpawn
        );
    }

    private static void registerBiomeSpawns() {
        // All pollinators use CREATURE spawn group to prevent over-spawning

        // Fig Wasp - Jungle biomes (small wasp = CREATURE)
        if (SymbioticSurvival.CONFIG.worldGen.enableTropical) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE, BiomeKeys.BAMBOO_JUNGLE),
                SpawnGroup.CREATURE,
                ModEntities.FIG_WASP,
                10,  // Reduced from 25 for performance
                1,
                2
            );
        }

        // Yucca Moth - Desert biomes (small moth = CREATURE)
        if (SymbioticSurvival.CONFIG.worldGen.enableDesert) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.DESERT),
                SpawnGroup.CREATURE,
                ModEntities.YUCCA_MOTH,
                8,  // Reduced from 20 for performance
                1,
                2
            );
        }

        // Monarch Butterfly - Plains biomes (butterfly = CREATURE)
        if (SymbioticSurvival.CONFIG.worldGen.enablePlains) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS),
                SpawnGroup.CREATURE,
                ModEntities.MONARCH_BUTTERFLY,
                12,  // Reduced from 30 for performance
                1,
                2
            );
        }

        // Mason Wasp - Savanna biomes (small wasp = CREATURE)
        if (SymbioticSurvival.CONFIG.worldGen.enableSavanna) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_SAVANNA),
                SpawnGroup.CREATURE,
                ModEntities.MASON_WASP,
                8,  // Reduced from 20 for performance
                1,
                2
            );
        }

        // Sawfly - Taiga biomes (small fly = CREATURE)
        if (SymbioticSurvival.CONFIG.worldGen.enableTaiga) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA),
                SpawnGroup.CREATURE,
                ModEntities.SAWFLY,
                10,  // Reduced from 25 for performance
                1,
                2
            );
        }

        // Mangrove Pollinator - Swamp biomes (medium pollinator = CREATURE)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP),
            SpawnGroup.CREATURE,
            ModEntities.MANGROVE_POLLINATOR,
            6,  // Reduced from 15 for performance
            1,
            2
        );

        // Fungus Gnat - Dark Forest biomes (small gnat = CREATURE)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST),
            SpawnGroup.CREATURE,
            ModEntities.FUNGUS_GNAT,
            14,  // Reduced from 35 for performance
            1,
            2
        );

        // Birch Pollinator - Birch Forest biomes (small pollinator = CREATURE)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST),
            SpawnGroup.CREATURE,
            ModEntities.BIRCH_POLLINATOR,
            10,  // Reduced from 25 for performance
            1,
            2
        );

        // Orchard Bee - Cherry Grove biomes (bee = CREATURE, like vanilla bees)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.CHERRY_GROVE),
            SpawnGroup.CREATURE,
            ModEntities.ORCHARD_BEE,
            8,  // Reduced from 20 for performance
            1,
            2
        );

        // Bumblebee - Snowy Taiga biomes (bee = CREATURE)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.SNOWY_TAIGA),
            SpawnGroup.CREATURE,
            ModEntities.BUMBLEBEE,
            5,  // Reduced from 12 for performance
            1,
            2
        );

        // Honeyguide - Multiple biomes where bee nests naturally spawn (bird = CREATURE)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(
                BiomeKeys.PLAINS,
                BiomeKeys.SUNFLOWER_PLAINS,
                BiomeKeys.SAVANNA,
                BiomeKeys.SAVANNA_PLATEAU,
                BiomeKeys.WINDSWEPT_SAVANNA,
                BiomeKeys.FOREST,
                BiomeKeys.FLOWER_FOREST,
                BiomeKeys.BIRCH_FOREST,
                BiomeKeys.OLD_GROWTH_BIRCH_FOREST
            ),
            SpawnGroup.CREATURE,
            ModEntities.HONEYGUIDE,
            8,  // Moderately rare
            1,
            1
        );

        if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
            SymbioticSurvival.LOGGER.info("Configured natural spawning: All pollinators use CREATURE group");
        }
    }
}
