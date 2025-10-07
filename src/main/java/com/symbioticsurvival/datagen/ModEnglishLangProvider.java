package com.symbioticsurvival.datagen;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.registry.ModBlocks;
import com.symbioticsurvival.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
 * Data generation provider for English (US) language translations.
 * Provides display names for all blocks, items, and UI elements.
 */
public class ModEnglishLangProvider extends FabricLanguageProvider {

    public ModEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup lookup, TranslationBuilder builder) {
        // Item group
        builder.add("itemGroup.symbioticsurvival.symbiotic_survival", "Symbiotic Survival");

        // Tree blocks
        builder.add(ModBlocks.FIG_TREE, "Fig Tree Log");
        builder.add(ModBlocks.YUCCA_PLANT, "Yucca Plant Stalk");
        builder.add(ModBlocks.ACACIA_VARIANT, "Acacia Variant Log");
        builder.add(ModBlocks.CONIFER_VARIANT, "Conifer Variant Log");
        builder.add(ModBlocks.MILKWEED, "Milkweed Stalk");
        builder.add(ModBlocks.MANGROVE_VARIANT, "Mangrove Variant Log");
        builder.add(ModBlocks.GLOWING_MUSHROOM, "Glowing Mushroom Stalk");
        builder.add(ModBlocks.FLOWERING_BIRCH, "Flowering Birch Log");
        builder.add(ModBlocks.ENHANCED_CHERRY, "Enhanced Cherry Log");
        builder.add(ModBlocks.ARCTIC_WILLOW, "Arctic Willow Log");

        // Leaves
        builder.add(ModBlocks.FIG_LEAVES, "Fig Leaves");
        builder.add(ModBlocks.YUCCA_LEAVES, "Yucca Leaves");
        builder.add(ModBlocks.ACACIA_VARIANT_LEAVES, "Acacia Variant Leaves");
        builder.add(ModBlocks.CONIFER_VARIANT_LEAVES, "Conifer Needles");
        builder.add(ModBlocks.MILKWEED_LEAVES, "Milkweed Leaves");
        builder.add(ModBlocks.MANGROVE_VARIANT_LEAVES, "Mangrove Variant Leaves");
        builder.add(ModBlocks.GLOWING_MUSHROOM_LEAVES, "Glowing Mushroom Cap");
        builder.add(ModBlocks.FLOWERING_BIRCH_LEAVES, "Flowering Birch Leaves");
        builder.add(ModBlocks.ENHANCED_CHERRY_LEAVES, "Enhanced Cherry Blossoms");
        builder.add(ModBlocks.ARCTIC_WILLOW_LEAVES, "Arctic Willow Leaves");

        // Saplings
        builder.add(ModBlocks.FIG_SAPLING, "Fig Sapling");
        builder.add(ModBlocks.YUCCA_SAPLING, "Yucca Sapling");
        builder.add(ModBlocks.ACACIA_VARIANT_SAPLING, "Acacia Variant Sapling");
        builder.add(ModBlocks.CONIFER_VARIANT_SAPLING, "Conifer Variant Sapling");
        builder.add(ModBlocks.MILKWEED_SAPLING, "Milkweed Seedling");
        builder.add(ModBlocks.MANGROVE_VARIANT_SAPLING, "Mangrove Variant Propagule");
        builder.add(ModBlocks.GLOWING_MUSHROOM_SAPLING, "Glowing Mushroom Spores");
        builder.add(ModBlocks.FLOWERING_BIRCH_SAPLING, "Flowering Birch Sapling");
        builder.add(ModBlocks.ENHANCED_CHERRY_SAPLING, "Enhanced Cherry Sapling");
        builder.add(ModBlocks.ARCTIC_WILLOW_SAPLING, "Arctic Willow Cutting");

        // Pollinator nests
        builder.add(ModBlocks.FIG_WASP_NEST, "Fig Wasp Nest");
        builder.add(ModBlocks.YUCCA_COCOON, "Yucca Moth Cocoon");
        builder.add(ModBlocks.MASON_WASP_NEST, "Mason Wasp Nest");
        builder.add(ModBlocks.SAWFLY_COCOON, "Sawfly Cocoon");
        builder.add(ModBlocks.MONARCH_CHRYSALIS, "Monarch Chrysalis");
        builder.add(ModBlocks.MANGROVE_BEE_HIVE, "Mangrove Bee Hive");
        builder.add(ModBlocks.FUNGUS_GNAT_NEST, "Fungus Gnat Nest");
        builder.add(ModBlocks.BIRCH_BEE_HIVE, "Birch Bee Hive");
        builder.add(ModBlocks.ORCHARD_BEE_NEST, "Orchard Bee Nest");
        builder.add(ModBlocks.BUMBLEBEE_NEST, "Bumblebee Nest");

        // Fruit items
        builder.add(ModItems.FIG, "Fig");
        builder.add(ModItems.YUCCA_POD, "Yucca Pod");
        builder.add(ModItems.ACACIA_SEED_POD, "Acacia Seed Pod");
        builder.add(ModItems.PINE_CONE, "Pine Cone");
        builder.add(ModItems.MILKWEED_POD, "Milkweed Pod");
        builder.add(ModItems.MANGROVE_FRUIT, "Mangrove Fruit");
        builder.add(ModItems.GLOWING_SPORE, "Glowing Spore");
        builder.add(ModItems.BIRCH_CATKIN, "Birch Catkin");
        builder.add(ModItems.CHERRY, "Cherry");
        builder.add(ModItems.WILLOW_CATKIN, "Willow Catkin");

        // Spawn eggs
        builder.add(ModItems.HONEYGUIDE_SPAWN_EGG, "Honeyguide Spawn Egg");
        builder.add(ModItems.FIG_WASP_SPAWN_EGG, "Fig Wasp Spawn Egg");
        builder.add(ModItems.YUCCA_MOTH_SPAWN_EGG, "Yucca Moth Spawn Egg");
        builder.add(ModItems.MONARCH_BUTTERFLY_SPAWN_EGG, "Monarch Butterfly Spawn Egg");
        builder.add(ModItems.MASON_WASP_SPAWN_EGG, "Mason Wasp Spawn Egg");
        builder.add(ModItems.SAWFLY_SPAWN_EGG, "Sawfly Spawn Egg");
        builder.add(ModItems.MANGROVE_POLLINATOR_SPAWN_EGG, "Mangrove Pollinator Spawn Egg");
        builder.add(ModItems.FUNGUS_GNAT_SPAWN_EGG, "Fungus Gnat Spawn Egg");
        builder.add(ModItems.BIRCH_POLLINATOR_SPAWN_EGG, "Birch Pollinator Spawn Egg");
        builder.add(ModItems.ORCHARD_BEE_SPAWN_EGG, "Orchard Bee Spawn Egg");
        builder.add(ModItems.BUMBLEBEE_SPAWN_EGG, "Bumblebee Spawn Egg");

        // Entity names
        builder.add("entity.symbioticsurvival.honeyguide", "Honeyguide");
        builder.add("entity.symbioticsurvival.fig_wasp", "Fig Wasp");
        builder.add("entity.symbioticsurvival.yucca_moth", "Yucca Moth");
        builder.add("entity.symbioticsurvival.monarch_butterfly", "Monarch Butterfly");
        builder.add("entity.symbioticsurvival.mason_wasp", "Mason Wasp");
        builder.add("entity.symbioticsurvival.sawfly", "Sawfly");
        builder.add("entity.symbioticsurvival.mangrove_pollinator", "Mangrove Pollinator");
        builder.add("entity.symbioticsurvival.fungus_gnat", "Fungus Gnat");
        builder.add("entity.symbioticsurvival.birch_pollinator", "Birch Pollinator");
        builder.add("entity.symbioticsurvival.orchard_bee", "Orchard Bee");
        builder.add("entity.symbioticsurvival.bumblebee", "Bumblebee");

        SymbioticSurvival.LOGGER.info("Generated English translations for " + SymbioticSurvival.MOD_ID);
    }
}
