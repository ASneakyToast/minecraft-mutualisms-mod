package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.item.BaseFruitItem;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import java.util.function.Function;

public class ModItems {

    // Fruit Items
    public static final Item FIG = registerItem("fig", settings ->
        new BaseFruitItem(settings.food(BaseFruitItem.createFoodComponent(4, 0.5f)), "tropical") {});

    public static final Item YUCCA_POD = registerItem("yucca_pod", settings ->
        new BaseFruitItem(settings, "desert") {});

    public static final Item ACACIA_SEED_POD = registerItem("acacia_seed_pod", settings ->
        new BaseFruitItem(settings, "savanna") {});

    public static final Item PINE_CONE = registerItem("pine_cone", settings ->
        new BaseFruitItem(settings, "taiga") {});

    public static final Item MILKWEED_POD = registerItem("milkweed_pod", settings ->
        new BaseFruitItem(settings, "plains") {});

    public static final Item MANGROVE_FRUIT = registerItem("mangrove_fruit", settings ->
        new BaseFruitItem(settings.food(BaseFruitItem.createFoodComponent(3, 0.4f)), "swamp") {});

    public static final Item GLOWING_SPORE = registerItem("glowing_spore", settings ->
        new BaseFruitItem(settings, "mushroom") {});

    public static final Item BIRCH_CATKIN = registerItem("birch_catkin", settings ->
        new BaseFruitItem(settings, "birch_forest") {});

    public static final Item CHERRY = registerItem("cherry", settings ->
        new BaseFruitItem(settings.food(BaseFruitItem.createFoodComponent(2, 0.3f)), "cherry_grove") {});

    public static final Item WILLOW_CATKIN = registerItem("willow_catkin", settings ->
        new BaseFruitItem(settings, "snowy") {});

    // Spawn Eggs
    // TODO: Add custom colors when color provider API is clarified for 1.21.9
    public static final Item HONEYGUIDE_SPAWN_EGG = registerItem("honeyguide_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.HONEYGUIDE;
            }
        });

    public static final Item FIG_WASP_SPAWN_EGG = registerItem("fig_wasp_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.FIG_WASP;
            }
        });

    public static final Item YUCCA_MOTH_SPAWN_EGG = registerItem("yucca_moth_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.YUCCA_MOTH;
            }
        });

    public static final Item MONARCH_BUTTERFLY_SPAWN_EGG = registerItem("monarch_butterfly_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.MONARCH_BUTTERFLY;
            }
        });

    public static final Item MASON_WASP_SPAWN_EGG = registerItem("mason_wasp_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.MASON_WASP;
            }
        });

    public static final Item SAWFLY_SPAWN_EGG = registerItem("sawfly_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.SAWFLY;
            }
        });

    public static final Item MANGROVE_POLLINATOR_SPAWN_EGG = registerItem("mangrove_pollinator_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.MANGROVE_POLLINATOR;
            }
        });

    public static final Item FUNGUS_GNAT_SPAWN_EGG = registerItem("fungus_gnat_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.FUNGUS_GNAT;
            }
        });

    public static final Item BIRCH_POLLINATOR_SPAWN_EGG = registerItem("birch_pollinator_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.BIRCH_POLLINATOR;
            }
        });

    public static final Item ORCHARD_BEE_SPAWN_EGG = registerItem("orchard_bee_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.ORCHARD_BEE;
            }
        });

    public static final Item BUMBLEBEE_SPAWN_EGG = registerItem("bumblebee_spawn_egg", settings ->
        new SpawnEggItem(settings) {
            @Override
            public net.minecraft.entity.EntityType<?> getEntityType(net.minecraft.item.ItemStack stack) {
                return ModEntities.BUMBLEBEE;
            }
        });

    // Block Items - Special Trees
    public static final Item FIG_TREE = registerItem("fig_tree", settings ->
        new BlockItem(ModBlocks.FIG_TREE, settings));

    public static final Item YUCCA_PLANT = registerItem("yucca_plant", settings ->
        new BlockItem(ModBlocks.YUCCA_PLANT, settings));

    public static final Item ACACIA_VARIANT = registerItem("acacia_variant", settings ->
        new BlockItem(ModBlocks.ACACIA_VARIANT, settings));

    public static final Item CONIFER_VARIANT = registerItem("conifer_variant", settings ->
        new BlockItem(ModBlocks.CONIFER_VARIANT, settings));

    public static final Item MILKWEED = registerItem("milkweed", settings ->
        new BlockItem(ModBlocks.MILKWEED, settings));

    public static final Item MANGROVE_VARIANT = registerItem("mangrove_variant", settings ->
        new BlockItem(ModBlocks.MANGROVE_VARIANT, settings));

    public static final Item GLOWING_MUSHROOM = registerItem("glowing_mushroom", settings ->
        new BlockItem(ModBlocks.GLOWING_MUSHROOM, settings));

    public static final Item FLOWERING_BIRCH = registerItem("flowering_birch", settings ->
        new BlockItem(ModBlocks.FLOWERING_BIRCH, settings));

    public static final Item ENHANCED_CHERRY = registerItem("enhanced_cherry", settings ->
        new BlockItem(ModBlocks.ENHANCED_CHERRY, settings));

    public static final Item ARCTIC_WILLOW = registerItem("arctic_willow", settings ->
        new BlockItem(ModBlocks.ARCTIC_WILLOW, settings));

    // Block Items - Pollinator Nests
    public static final Item FIG_WASP_NEST = registerItem("fig_wasp_nest", settings ->
        new BlockItem(ModBlocks.FIG_WASP_NEST, settings));

    public static final Item YUCCA_COCOON = registerItem("yucca_cocoon", settings ->
        new BlockItem(ModBlocks.YUCCA_COCOON, settings));

    public static final Item MASON_WASP_NEST = registerItem("mason_wasp_nest", settings ->
        new BlockItem(ModBlocks.MASON_WASP_NEST, settings));

    public static final Item SAWFLY_COCOON = registerItem("sawfly_cocoon", settings ->
        new BlockItem(ModBlocks.SAWFLY_COCOON, settings));

    public static final Item MONARCH_CHRYSALIS = registerItem("monarch_chrysalis", settings ->
        new BlockItem(ModBlocks.MONARCH_CHRYSALIS, settings));

    public static final Item MANGROVE_BEE_HIVE = registerItem("mangrove_bee_hive", settings ->
        new BlockItem(ModBlocks.MANGROVE_BEE_HIVE, settings));

    public static final Item FUNGUS_GNAT_NEST = registerItem("fungus_gnat_nest", settings ->
        new BlockItem(ModBlocks.FUNGUS_GNAT_NEST, settings));

    public static final Item BIRCH_BEE_HIVE = registerItem("birch_bee_hive", settings ->
        new BlockItem(ModBlocks.BIRCH_BEE_HIVE, settings));

    public static final Item ORCHARD_BEE_NEST = registerItem("orchard_bee_nest", settings ->
        new BlockItem(ModBlocks.ORCHARD_BEE_NEST, settings));

    public static final Item BUMBLEBEE_NEST = registerItem("bumblebee_nest", settings ->
        new BlockItem(ModBlocks.BUMBLEBEE_NEST, settings));

    // Block Items - Leaves
    public static final Item FIG_LEAVES = registerItem("fig_leaves", settings ->
        new BlockItem(ModBlocks.FIG_LEAVES, settings));

    public static final Item YUCCA_LEAVES = registerItem("yucca_leaves", settings ->
        new BlockItem(ModBlocks.YUCCA_LEAVES, settings));

    public static final Item ACACIA_VARIANT_LEAVES = registerItem("acacia_variant_leaves", settings ->
        new BlockItem(ModBlocks.ACACIA_VARIANT_LEAVES, settings));

    public static final Item CONIFER_VARIANT_LEAVES = registerItem("conifer_variant_leaves", settings ->
        new BlockItem(ModBlocks.CONIFER_VARIANT_LEAVES, settings));

    public static final Item MILKWEED_LEAVES = registerItem("milkweed_leaves", settings ->
        new BlockItem(ModBlocks.MILKWEED_LEAVES, settings));

    public static final Item MANGROVE_VARIANT_LEAVES = registerItem("mangrove_variant_leaves", settings ->
        new BlockItem(ModBlocks.MANGROVE_VARIANT_LEAVES, settings));

    public static final Item GLOWING_MUSHROOM_LEAVES = registerItem("glowing_mushroom_leaves", settings ->
        new BlockItem(ModBlocks.GLOWING_MUSHROOM_LEAVES, settings));

    public static final Item FLOWERING_BIRCH_LEAVES = registerItem("flowering_birch_leaves", settings ->
        new BlockItem(ModBlocks.FLOWERING_BIRCH_LEAVES, settings));

    public static final Item ENHANCED_CHERRY_LEAVES = registerItem("enhanced_cherry_leaves", settings ->
        new BlockItem(ModBlocks.ENHANCED_CHERRY_LEAVES, settings));

    public static final Item ARCTIC_WILLOW_LEAVES = registerItem("arctic_willow_leaves", settings ->
        new BlockItem(ModBlocks.ARCTIC_WILLOW_LEAVES, settings));

    // Block Items - Saplings
    public static final Item FIG_SAPLING = registerItem("fig_sapling", settings ->
        new BlockItem(ModBlocks.FIG_SAPLING, settings));

    public static final Item YUCCA_SAPLING = registerItem("yucca_sapling", settings ->
        new BlockItem(ModBlocks.YUCCA_SAPLING, settings));

    public static final Item ACACIA_VARIANT_SAPLING = registerItem("acacia_variant_sapling", settings ->
        new BlockItem(ModBlocks.ACACIA_VARIANT_SAPLING, settings));

    public static final Item CONIFER_VARIANT_SAPLING = registerItem("conifer_variant_sapling", settings ->
        new BlockItem(ModBlocks.CONIFER_VARIANT_SAPLING, settings));

    public static final Item MILKWEED_SAPLING = registerItem("milkweed_sapling", settings ->
        new BlockItem(ModBlocks.MILKWEED_SAPLING, settings));

    public static final Item MANGROVE_VARIANT_SAPLING = registerItem("mangrove_variant_sapling", settings ->
        new BlockItem(ModBlocks.MANGROVE_VARIANT_SAPLING, settings));

    public static final Item GLOWING_MUSHROOM_SAPLING = registerItem("glowing_mushroom_sapling", settings ->
        new BlockItem(ModBlocks.GLOWING_MUSHROOM_SAPLING, settings));

    public static final Item FLOWERING_BIRCH_SAPLING = registerItem("flowering_birch_sapling", settings ->
        new BlockItem(ModBlocks.FLOWERING_BIRCH_SAPLING, settings));

    public static final Item ENHANCED_CHERRY_SAPLING = registerItem("enhanced_cherry_sapling", settings ->
        new BlockItem(ModBlocks.ENHANCED_CHERRY_SAPLING, settings));

    public static final Item ARCTIC_WILLOW_SAPLING = registerItem("arctic_willow_sapling", settings ->
        new BlockItem(ModBlocks.ARCTIC_WILLOW_SAPLING, settings));

    private static <T extends Item> T registerItem(String name, Function<Item.Settings, T> factory) {
        Identifier id = Identifier.of(SymbioticSurvival.MOD_ID, name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        Item.Settings settings = new Item.Settings().registryKey(key);
        T item = factory.apply(settings);
        return Registry.register(Registries.ITEM, key, item);
    }

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering items for " + SymbioticSurvival.MOD_ID);
        // Force static initialization of all items
    }
}
