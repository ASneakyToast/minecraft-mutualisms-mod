package com.symbioticsurvival.worldgen;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.registry.ModFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

/**
 * Handles biome modifications to add custom features to world generation.
 */
public class ModWorldGen {

    // Configured feature keys for each biome
    private static final RegistryKey<net.minecraft.world.gen.feature.ConfiguredFeature<?, ?>> BIOME_PAIR_CONFIGURED =
        RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(SymbioticSurvival.MOD_ID, "biome_pair_configured"));

    private static final RegistryKey<net.minecraft.world.gen.feature.PlacedFeature> BIOME_PAIR_PLACED =
        RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(SymbioticSurvival.MOD_ID, "biome_pair_placed"));

    public static void initialize() {
        SymbioticSurvival.LOGGER.info("Initializing world generation for " + SymbioticSurvival.MOD_ID);

        // Add biome pair features to appropriate biomes
        BiomeModifications.addFeature(
            // Only add to biomes that have registered pairs
            BiomeSelectors.includeByKey(
                BiomeKeys.JUNGLE,
                BiomeKeys.DESERT,
                BiomeKeys.SAVANNA,
                BiomeKeys.TAIGA,
                BiomeKeys.PLAINS,
                BiomeKeys.SWAMP,
                BiomeKeys.DARK_FOREST,
                BiomeKeys.BIRCH_FOREST,
                BiomeKeys.CHERRY_GROVE,
                BiomeKeys.SNOWY_TAIGA
            ),
            GenerationStep.Feature.VEGETAL_DECORATION,
            BIOME_PAIR_PLACED
        );

        if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
            SymbioticSurvival.LOGGER.info("Added biome pair features to 10 biomes");
        }
    }
}
