package com.symbioticsurvival.worldgen;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.registry.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.GiantTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

/**
 * Configures features for custom trees using vanilla tree generation system.
 */
public class ModConfiguredFeatures {

    // Tree configured feature keys - Regular (1x1)
    public static final RegistryKey<ConfiguredFeature<?, ?>> FIG_TREE =
        registerKey("fig_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> YUCCA_PLANT =
        registerKey("yucca_plant");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ACACIA_VARIANT =
        registerKey("acacia_variant");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CONIFER_VARIANT =
        registerKey("conifer_variant");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MILKWEED =
        registerKey("milkweed");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MANGROVE_VARIANT =
        registerKey("mangrove_variant");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GLOWING_MUSHROOM =
        registerKey("glowing_mushroom");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWERING_BIRCH =
        registerKey("flowering_birch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ENHANCED_CHERRY =
        registerKey("enhanced_cherry");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ARCTIC_WILLOW =
        registerKey("arctic_willow");

    // Tree configured feature keys - Mega (2x2)
    public static final RegistryKey<ConfiguredFeature<?, ?>> FIG_TREE_MEGA =
        registerKey("fig_tree_mega");
    public static final RegistryKey<ConfiguredFeature<?, ?>> YUCCA_PLANT_MEGA =
        registerKey("yucca_plant_mega");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ACACIA_VARIANT_MEGA =
        registerKey("acacia_variant_mega");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CONIFER_VARIANT_MEGA =
        registerKey("conifer_variant_mega");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MILKWEED_MEGA =
        registerKey("milkweed_mega");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MANGROVE_VARIANT_MEGA =
        registerKey("mangrove_variant_mega");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GLOWING_MUSHROOM_MEGA =
        registerKey("glowing_mushroom_mega");
    public static final RegistryKey<ConfiguredFeature<?, ?>> FLOWERING_BIRCH_MEGA =
        registerKey("flowering_birch_mega");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ENHANCED_CHERRY_MEGA =
        registerKey("enhanced_cherry_mega");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ARCTIC_WILLOW_MEGA =
        registerKey("arctic_willow_mega");

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(SymbioticSurvival.MOD_ID, name));
    }

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        // Fig Tree - Tall with wide canopy (tropical)
        register(context, FIG_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.FIG_TREE),
            new StraightTrunkPlacer(5, 2, 0), // Base height 5, random add 0-2
            BlockStateProvider.of(ModBlocks.FIG_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.FIG_WASP_NEST, "tropical", 12, 8)
        )).build());

        // Yucca Plant - Short, thin plant (desert)
        register(context, YUCCA_PLANT, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.YUCCA_PLANT),
            new StraightTrunkPlacer(2, 1, 0), // Base height 2, random add 0-1
            BlockStateProvider.of(ModBlocks.YUCCA_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
            new TwoLayersFeatureSize(0, 0, 0)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.YUCCA_COCOON, "desert", 10, 8)
        )).build());

        // Acacia Variant - Medium height, wide canopy (savanna)
        register(context, ACACIA_VARIANT, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.ACACIA_VARIANT),
            new StraightTrunkPlacer(4, 1, 0), // Base height 4, random add 0-1
            BlockStateProvider.of(ModBlocks.ACACIA_VARIANT_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.MASON_WASP_NEST, "savanna", 12, 8)
        )).build());

        // Conifer Variant - Tall and narrow (taiga)
        register(context, CONIFER_VARIANT, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.CONIFER_VARIANT),
            new StraightTrunkPlacer(6, 2, 0), // Base height 6, random add 0-2
            BlockStateProvider.of(ModBlocks.CONIFER_VARIANT_LEAVES),
            new SpruceFoliagePlacer(
                UniformIntProvider.create(2, 3), // Radius
                UniformIntProvider.create(0, 1), // Offset
                UniformIntProvider.create(1, 2)  // Height
            ),
            new TwoLayersFeatureSize(1, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.SAWFLY_COCOON, "taiga", 12, 8)
        )).build());

        // Milkweed - Very short, more like a tall flower (plains)
        register(context, MILKWEED, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.MILKWEED),
            new StraightTrunkPlacer(1, 1, 0), // Base height 1, random add 0-1
            BlockStateProvider.of(ModBlocks.MILKWEED_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0), 1),
            new TwoLayersFeatureSize(0, 0, 0)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.MONARCH_CHRYSALIS, "plains", 8, 8)
        )).build());

        // Mangrove Variant - Medium with hanging leaves (swamp)
        register(context, MANGROVE_VARIANT, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.MANGROVE_VARIANT),
            new StraightTrunkPlacer(4, 1, 0), // Base height 4, random add 0-1
            BlockStateProvider.of(ModBlocks.MANGROVE_VARIANT_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.MANGROVE_BEE_HIVE, "swamp", 12, 8)
        )).build());

        // Glowing Mushroom - Short, wide cap (mushroom)
        register(context, GLOWING_MUSHROOM, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.GLOWING_MUSHROOM),
            new StraightTrunkPlacer(3, 1, 0), // Base height 3, random add 0-1
            BlockStateProvider.of(ModBlocks.GLOWING_MUSHROOM_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
            new TwoLayersFeatureSize(0, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.FUNGUS_GNAT_NEST, "mushroom", 12, 8)
        )).build());

        // Flowering Birch - Tall, narrow (birch forest)
        register(context, FLOWERING_BIRCH, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.FLOWERING_BIRCH),
            new StraightTrunkPlacer(5, 1, 0), // Base height 5, random add 0-1
            BlockStateProvider.of(ModBlocks.FLOWERING_BIRCH_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.BIRCH_BEE_HIVE, "birch_forest", 12, 8)
        )).build());

        // Enhanced Cherry - Medium with pink leaves (cherry grove)
        register(context, ENHANCED_CHERRY, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.ENHANCED_CHERRY),
            new StraightTrunkPlacer(4, 1, 0), // Base height 4, random add 0-1
            BlockStateProvider.of(ModBlocks.ENHANCED_CHERRY_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.ORCHARD_BEE_NEST, "cherry_grove", 12, 8)
        )).build());

        // Arctic Willow - Short, bushy (snowy)
        register(context, ARCTIC_WILLOW, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.ARCTIC_WILLOW),
            new StraightTrunkPlacer(3, 1, 0), // Base height 3, random add 0-1
            BlockStateProvider.of(ModBlocks.ARCTIC_WILLOW_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
            new TwoLayersFeatureSize(0, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.BUMBLEBEE_NEST, "snowy", 12, 8)
        )).build());

        // ===== MEGA (2x2) TREE VARIANTS =====

        // Fig Tree Mega - Very tall with wide canopy (tropical)
        register(context, FIG_TREE_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.FIG_TREE),
            new GiantTrunkPlacer(12, 4, 0), // Base height 12, random add 0-4 (12-16 blocks)
            BlockStateProvider.of(ModBlocks.FIG_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), 4),
            new TwoLayersFeatureSize(1, 0, 2)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.FIG_WASP_NEST, "tropical", 16, 12)
        )).build());

        // Yucca Plant Mega - Taller desert plant (desert)
        register(context, YUCCA_PLANT_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.YUCCA_PLANT),
            new GiantTrunkPlacer(6, 3, 0), // Base height 6, random add 0-3 (6-9 blocks)
            BlockStateProvider.of(ModBlocks.YUCCA_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 4),
            new TwoLayersFeatureSize(0, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.YUCCA_COCOON, "desert", 14, 10)
        )).build());

        // Acacia Variant Mega - Tall with wide canopy (savanna)
        register(context, ACACIA_VARIANT_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.ACACIA_VARIANT),
            new GiantTrunkPlacer(10, 3, 0), // Base height 10, random add 0-3 (10-13 blocks)
            BlockStateProvider.of(ModBlocks.ACACIA_VARIANT_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), 4),
            new TwoLayersFeatureSize(1, 0, 2)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.MASON_WASP_NEST, "savanna", 16, 12)
        )).build());

        // Conifer Variant Mega - Very tall and narrow (taiga)
        register(context, CONIFER_VARIANT_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.CONIFER_VARIANT),
            new GiantTrunkPlacer(15, 5, 0), // Base height 15, random add 0-5 (15-20 blocks)
            BlockStateProvider.of(ModBlocks.CONIFER_VARIANT_LEAVES),
            new SpruceFoliagePlacer(
                UniformIntProvider.create(3, 4), // Radius
                UniformIntProvider.create(0, 2), // Offset
                UniformIntProvider.create(2, 3)  // Height
            ),
            new TwoLayersFeatureSize(1, 0, 2)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.SAWFLY_COCOON, "taiga", 16, 12)
        )).build());

        // Milkweed Mega - Tall flower cluster (plains)
        register(context, MILKWEED_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.MILKWEED),
            new GiantTrunkPlacer(4, 2, 0), // Base height 4, random add 0-2 (4-6 blocks)
            BlockStateProvider.of(ModBlocks.MILKWEED_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(0), 3),
            new TwoLayersFeatureSize(0, 0, 1)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.MONARCH_CHRYSALIS, "plains", 12, 10)
        )).build());

        // Mangrove Variant Mega - Tall with extensive roots (swamp)
        register(context, MANGROVE_VARIANT_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.MANGROVE_VARIANT),
            new GiantTrunkPlacer(10, 3, 0), // Base height 10, random add 0-3 (10-13 blocks)
            BlockStateProvider.of(ModBlocks.MANGROVE_VARIANT_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), 4),
            new TwoLayersFeatureSize(1, 0, 2)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.MANGROVE_BEE_HIVE, "swamp", 16, 12)
        )).build());

        // Glowing Mushroom Mega - Large mushroom cap (mushroom)
        register(context, GLOWING_MUSHROOM_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.GLOWING_MUSHROOM),
            new GiantTrunkPlacer(8, 3, 0), // Base height 8, random add 0-3 (8-11 blocks)
            BlockStateProvider.of(ModBlocks.GLOWING_MUSHROOM_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), 4),
            new TwoLayersFeatureSize(0, 0, 2)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.FUNGUS_GNAT_NEST, "mushroom", 16, 12)
        )).build());

        // Flowering Birch Mega - Very tall and narrow (birch forest)
        register(context, FLOWERING_BIRCH_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.FLOWERING_BIRCH),
            new GiantTrunkPlacer(12, 3, 0), // Base height 12, random add 0-3 (12-15 blocks)
            BlockStateProvider.of(ModBlocks.FLOWERING_BIRCH_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), 4),
            new TwoLayersFeatureSize(1, 0, 2)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.BIRCH_BEE_HIVE, "birch_forest", 16, 12)
        )).build());

        // Enhanced Cherry Mega - Tall with expansive pink canopy (cherry grove)
        register(context, ENHANCED_CHERRY_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.ENHANCED_CHERRY),
            new GiantTrunkPlacer(10, 3, 0), // Base height 10, random add 0-3 (10-13 blocks)
            BlockStateProvider.of(ModBlocks.ENHANCED_CHERRY_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), 4),
            new TwoLayersFeatureSize(1, 0, 2)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.ORCHARD_BEE_NEST, "cherry_grove", 16, 12)
        )).build());

        // Arctic Willow Mega - Tall, bushy arctic tree (snowy)
        register(context, ARCTIC_WILLOW_MEGA, Feature.TREE, new TreeFeatureConfig.Builder(
            BlockStateProvider.of(ModBlocks.ARCTIC_WILLOW),
            new GiantTrunkPlacer(8, 2, 0), // Base height 8, random add 0-2 (8-10 blocks)
            BlockStateProvider.of(ModBlocks.ARCTIC_WILLOW_LEAVES),
            new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), 4),
            new TwoLayersFeatureSize(0, 0, 2)
        ).decorators(List.of(
            new NestLinkingDecorator(ModBlocks.BUMBLEBEE_NEST, "snowy", 16, 12)
        )).build());
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(
        Registerable<ConfiguredFeature<?, ?>> context,
        RegistryKey<ConfiguredFeature<?, ?>> key,
        F feature,
        FC configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
