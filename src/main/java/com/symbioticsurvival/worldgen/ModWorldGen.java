package com.symbioticsurvival.worldgen;

import com.symbioticsurvival.SymbioticSurvival;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

/**
 * Handles biome modifications to add custom features to world generation.
 */
public class ModWorldGen {

    public static void initialize() {
        SymbioticSurvival.LOGGER.info("Initializing world generation for " + SymbioticSurvival.MOD_ID);

        // Add tree features to their respective biomes
        // Each tree includes a NestLinkingDecorator that places and links nests automatically

        // Jungle - Fig Tree
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.JUNGLE),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.FIG_TREE
        );

        // Desert - Yucca Plant
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.DESERT),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.YUCCA_PLANT
        );

        // Savanna - Acacia Variant
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.SAVANNA),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.ACACIA_VARIANT
        );

        // Taiga - Conifer Variant
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.TAIGA),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.CONIFER_VARIANT
        );

        // Plains - Milkweed
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.PLAINS),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.MILKWEED
        );

        // Swamp - Mangrove Variant
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.SWAMP),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.MANGROVE_VARIANT
        );

        // Dark Forest - Glowing Mushroom
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.GLOWING_MUSHROOM
        );

        // Birch Forest - Flowering Birch
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.BIRCH_FOREST),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.FLOWERING_BIRCH
        );

        // Cherry Grove - Enhanced Cherry
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.CHERRY_GROVE),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.ENHANCED_CHERRY
        );

        // Snowy Taiga - Arctic Willow
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.SNOWY_TAIGA),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.ARCTIC_WILLOW
        );

        if (SymbioticSurvival.CONFIG.debug.enableDebugLogging) {
            SymbioticSurvival.LOGGER.info("Added 10 custom tree features to biomes with nest linking");
        }
    }
}
