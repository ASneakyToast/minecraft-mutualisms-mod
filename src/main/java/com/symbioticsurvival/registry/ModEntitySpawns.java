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
        // Small flying insects use AMBIENT spawn group (separate mob cap from animals)
        // Larger pollinators use CREATURE spawn group

        // Fig Wasp - Jungle biomes (small wasp = AMBIENT)
        if (SymbioticSurvival.CONFIG.worldGen.enableTropical) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE, BiomeKeys.BAMBOO_JUNGLE),
                SpawnGroup.AMBIENT,
                ModEntities.FIG_WASP,
                25,  // Higher weight for ambient spawns
                1,
                2
            );
        }

        // Yucca Moth - Desert biomes (small moth = AMBIENT)
        if (SymbioticSurvival.CONFIG.worldGen.enableDesert) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.DESERT),
                SpawnGroup.AMBIENT,
                ModEntities.YUCCA_MOTH,
                20,
                1,
                2
            );
        }

        // Monarch Butterfly - Plains biomes (butterfly = AMBIENT)
        if (SymbioticSurvival.CONFIG.worldGen.enablePlains) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.PLAINS, BiomeKeys.SUNFLOWER_PLAINS),
                SpawnGroup.AMBIENT,
                ModEntities.MONARCH_BUTTERFLY,
                30,  // Butterflies should be common in plains
                2,
                4
            );
        }

        // Mason Wasp - Savanna biomes (small wasp = AMBIENT)
        if (SymbioticSurvival.CONFIG.worldGen.enableSavanna) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.SAVANNA, BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_SAVANNA),
                SpawnGroup.AMBIENT,
                ModEntities.MASON_WASP,
                20,
                1,
                3
            );
        }

        // Sawfly - Taiga biomes (small fly = AMBIENT)
        if (SymbioticSurvival.CONFIG.worldGen.enableTaiga) {
            BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA),
                SpawnGroup.AMBIENT,
                ModEntities.SAWFLY,
                25,
                1,
                3
            );
        }

        // Mangrove Pollinator - Swamp biomes (medium pollinator = CREATURE)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP),
            SpawnGroup.CREATURE,
            ModEntities.MANGROVE_POLLINATOR,
            15,
            1,
            2
        );

        // Fungus Gnat - Dark Forest biomes (small gnat = AMBIENT)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST),
            SpawnGroup.AMBIENT,
            ModEntities.FUNGUS_GNAT,
            35,  // Should be plentiful in dark forests
            2,
            4
        );

        // Birch Pollinator - Birch Forest biomes (small pollinator = AMBIENT)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST),
            SpawnGroup.AMBIENT,
            ModEntities.BIRCH_POLLINATOR,
            25,
            1,
            3
        );

        // Orchard Bee - Cherry Grove biomes (bee = CREATURE, like vanilla bees)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.CHERRY_GROVE),
            SpawnGroup.CREATURE,
            ModEntities.ORCHARD_BEE,
            20,  // Should be common in cherry groves
            2,
            4
        );

        // Bumblebee - Snowy Taiga biomes (bee = CREATURE)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.SNOWY_TAIGA),
            SpawnGroup.CREATURE,
            ModEntities.BUMBLEBEE,
            12,
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
            SymbioticSurvival.LOGGER.info("Configured natural spawning: AMBIENT group (small insects) and CREATURE group (bees/birds)");
        }
    }
}
