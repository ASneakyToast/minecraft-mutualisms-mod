package com.symbioticsurvival.worldgen;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

/**
 * Defines placed features for custom trees with placement modifiers.
 */
public class ModPlacedFeatures {

    // Placed feature keys
    public static final RegistryKey<PlacedFeature> FIG_TREE =
        registerKey("fig_tree_placed");
    public static final RegistryKey<PlacedFeature> YUCCA_PLANT =
        registerKey("yucca_plant_placed");
    public static final RegistryKey<PlacedFeature> ACACIA_VARIANT =
        registerKey("acacia_variant_placed");
    public static final RegistryKey<PlacedFeature> CONIFER_VARIANT =
        registerKey("conifer_variant_placed");
    public static final RegistryKey<PlacedFeature> MILKWEED =
        registerKey("milkweed_placed");
    public static final RegistryKey<PlacedFeature> MANGROVE_VARIANT =
        registerKey("mangrove_variant_placed");
    public static final RegistryKey<PlacedFeature> GLOWING_MUSHROOM =
        registerKey("glowing_mushroom_placed");
    public static final RegistryKey<PlacedFeature> FLOWERING_BIRCH =
        registerKey("flowering_birch_placed");
    public static final RegistryKey<PlacedFeature> ENHANCED_CHERRY =
        registerKey("enhanced_cherry_placed");
    public static final RegistryKey<PlacedFeature> ARCTIC_WILLOW =
        registerKey("arctic_willow_placed");

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(SymbioticSurvival.MOD_ID, name));
    }

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        // Common tree placement modifiers
        List<PlacementModifier> commonTreePlacement = List.of(
            RarityFilterPlacementModifier.of(10), // 1 in 10 chunks
            SquarePlacementModifier.of(),
            PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
            SurfaceWaterDepthFilterPlacementModifier.of(0), // Prevent underwater placement
            BiomePlacementModifier.of()
        );

        // Register all tree placed features
        register(context, FIG_TREE, configuredFeatures.getOrThrow(ModConfiguredFeatures.FIG_TREE),
            commonTreePlacement);

        register(context, YUCCA_PLANT, configuredFeatures.getOrThrow(ModConfiguredFeatures.YUCCA_PLANT),
            commonTreePlacement);

        register(context, ACACIA_VARIANT, configuredFeatures.getOrThrow(ModConfiguredFeatures.ACACIA_VARIANT),
            commonTreePlacement);

        register(context, CONIFER_VARIANT, configuredFeatures.getOrThrow(ModConfiguredFeatures.CONIFER_VARIANT),
            commonTreePlacement);

        register(context, MILKWEED, configuredFeatures.getOrThrow(ModConfiguredFeatures.MILKWEED),
            List.of(
                RarityFilterPlacementModifier.of(5), // More common (1 in 5 chunks)
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                SurfaceWaterDepthFilterPlacementModifier.of(0), // Prevent underwater placement
                BiomePlacementModifier.of()
            ));

        register(context, MANGROVE_VARIANT, configuredFeatures.getOrThrow(ModConfiguredFeatures.MANGROVE_VARIANT),
            List.of(
                RarityFilterPlacementModifier.of(10), // 1 in 10 chunks
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                SurfaceWaterDepthFilterPlacementModifier.of(5), // Allow shallow water (like vanilla mangroves)
                BiomePlacementModifier.of()
            ));

        register(context, GLOWING_MUSHROOM, configuredFeatures.getOrThrow(ModConfiguredFeatures.GLOWING_MUSHROOM),
            List.of(
                RarityFilterPlacementModifier.of(15), // Rarer (1 in 15 chunks)
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                SurfaceWaterDepthFilterPlacementModifier.of(0), // Prevent underwater placement
                BiomePlacementModifier.of()
            ));

        register(context, FLOWERING_BIRCH, configuredFeatures.getOrThrow(ModConfiguredFeatures.FLOWERING_BIRCH),
            commonTreePlacement);

        register(context, ENHANCED_CHERRY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ENHANCED_CHERRY),
            commonTreePlacement);

        register(context, ARCTIC_WILLOW, configuredFeatures.getOrThrow(ModConfiguredFeatures.ARCTIC_WILLOW),
            commonTreePlacement);
    }

    private static void register(
        Registerable<PlacedFeature> context,
        RegistryKey<PlacedFeature> key,
        RegistryEntry<ConfiguredFeature<?, ?>> configuration,
        List<PlacementModifier> modifiers
    ) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
