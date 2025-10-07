package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

/**
 * Registers custom blocks with vanilla Minecraft systems:
 * - Flammability (for fire spread)
 * - Composting (for composter blocks)
 * - Fuel (for furnaces)
 */
public class ModVanillaIntegration {

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering vanilla integration for " + SymbioticSurvival.MOD_ID);

        registerFlammableBlocks();
        registerCompostableItems();
        registerFuels();
    }

    /**
     * Register blocks that can catch fire and burn.
     * Format: add(block, burnChance, spreadChance)
     * - burnChance: how likely the block is to be destroyed by fire (5 = like logs)
     * - spreadChance: how likely fire is to spread to this block (5 = like logs, 60 = like leaves)
     */
    private static void registerFlammableBlocks() {
        FlammableBlockRegistry flammable = FlammableBlockRegistry.getDefaultInstance();

        // Tree blocks - burn like vanilla logs (5, 5)
        flammable.add(ModBlocks.FIG_TREE, 5, 5);
        flammable.add(ModBlocks.YUCCA_PLANT, 5, 5);
        flammable.add(ModBlocks.ACACIA_VARIANT, 5, 5);
        flammable.add(ModBlocks.CONIFER_VARIANT, 5, 5);
        flammable.add(ModBlocks.MILKWEED, 5, 5);
        flammable.add(ModBlocks.MANGROVE_VARIANT, 5, 5);
        flammable.add(ModBlocks.GLOWING_MUSHROOM, 5, 5);
        flammable.add(ModBlocks.FLOWERING_BIRCH, 5, 5);
        flammable.add(ModBlocks.ENHANCED_CHERRY, 5, 5);
        flammable.add(ModBlocks.ARCTIC_WILLOW, 5, 5);

        // Leaf blocks - burn like vanilla leaves (30, 60)
        flammable.add(ModBlocks.FIG_LEAVES, 30, 60);
        flammable.add(ModBlocks.YUCCA_LEAVES, 30, 60);
        flammable.add(ModBlocks.ACACIA_VARIANT_LEAVES, 30, 60);
        flammable.add(ModBlocks.CONIFER_VARIANT_LEAVES, 30, 60);
        flammable.add(ModBlocks.MILKWEED_LEAVES, 30, 60);
        flammable.add(ModBlocks.MANGROVE_VARIANT_LEAVES, 30, 60);
        flammable.add(ModBlocks.GLOWING_MUSHROOM_LEAVES, 30, 60);
        flammable.add(ModBlocks.FLOWERING_BIRCH_LEAVES, 30, 60);
        flammable.add(ModBlocks.ENHANCED_CHERRY_LEAVES, 30, 60);
        flammable.add(ModBlocks.ARCTIC_WILLOW_LEAVES, 30, 60);

        // Nest blocks - vary based on material
        flammable.add(ModBlocks.FIG_WASP_NEST, 5, 5);           // Wood-like
        flammable.add(ModBlocks.YUCCA_COCOON, 15, 60);          // Silk-like (more flammable)
        flammable.add(ModBlocks.SAWFLY_COCOON, 5, 5);           // Wood-like
        flammable.add(ModBlocks.MONARCH_CHRYSALIS, 15, 60);     // Silk-like
        flammable.add(ModBlocks.MANGROVE_BEE_HIVE, 5, 5);       // Wood-like
        flammable.add(ModBlocks.BIRCH_BEE_HIVE, 5, 5);          // Wood-like
        flammable.add(ModBlocks.ORCHARD_BEE_NEST, 5, 5);        // Wood-like
        flammable.add(ModBlocks.BUMBLEBEE_NEST, 15, 60);        // Wool-like (more flammable)
        // Note: Mason wasp nest and fungus gnat nest are not flammable (stone/fungus materials)
    }

    /**
     * Register items that can be composted.
     * Values: 0.3 for leaves (like vanilla), 0.5 for saplings, 0.65 for flowers, etc.
     */
    private static void registerCompostableItems() {
        CompostingChanceRegistry composting = CompostingChanceRegistry.INSTANCE;

        // All leaves are compostable at 0.3 (same as vanilla leaves)
        composting.add(ModBlocks.FIG_LEAVES, 0.3f);
        composting.add(ModBlocks.YUCCA_LEAVES, 0.3f);
        composting.add(ModBlocks.ACACIA_VARIANT_LEAVES, 0.3f);
        composting.add(ModBlocks.CONIFER_VARIANT_LEAVES, 0.3f);
        composting.add(ModBlocks.MILKWEED_LEAVES, 0.3f);
        composting.add(ModBlocks.MANGROVE_VARIANT_LEAVES, 0.3f);
        composting.add(ModBlocks.GLOWING_MUSHROOM_LEAVES, 0.3f);
        composting.add(ModBlocks.FLOWERING_BIRCH_LEAVES, 0.3f);
        composting.add(ModBlocks.ENHANCED_CHERRY_LEAVES, 0.3f);
        composting.add(ModBlocks.ARCTIC_WILLOW_LEAVES, 0.3f);
    }

    /**
     * Register items that can be used as furnace fuel.
     * Vanilla log = 300 ticks (1.5 items), planks = 300 ticks, sticks = 100 ticks
     * Uses the new FuelRegistryEvents API for Minecraft 1.21+
     */
    private static void registerFuels() {
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            // All tree blocks can be used as fuel (300 ticks = 1.5 items, same as vanilla logs)
            builder.add(ModBlocks.FIG_TREE, 300);
            builder.add(ModBlocks.YUCCA_PLANT, 300);
            builder.add(ModBlocks.ACACIA_VARIANT, 300);
            builder.add(ModBlocks.CONIFER_VARIANT, 300);
            builder.add(ModBlocks.MILKWEED, 300);
            builder.add(ModBlocks.MANGROVE_VARIANT, 300);
            builder.add(ModBlocks.GLOWING_MUSHROOM, 300);
            builder.add(ModBlocks.FLOWERING_BIRCH, 300);
            builder.add(ModBlocks.ENHANCED_CHERRY, 300);
            builder.add(ModBlocks.ARCTIC_WILLOW, 300);
        });
    }
}
