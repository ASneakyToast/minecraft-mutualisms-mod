package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.PollinatorNestBlock;
import com.symbioticsurvival.block.SpecialLeavesBlock;
import com.symbioticsurvival.block.SpecialTreeBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    // Special Tree Blocks
    public static final Block FIG_TREE = registerBlock("fig_tree",
        new SpecialTreeBlock(settingsWithKey("fig_tree")
            .mapColor(MapColor.BROWN)
            .sounds(BlockSoundGroup.WOOD)
            .strength(2.0f)
            .requiresTool(), "tropical"));

    public static final Block YUCCA_PLANT = registerBlock("yucca_plant",
        new SpecialTreeBlock(settingsWithKey("yucca_plant")
            .mapColor(MapColor.GREEN)
            .sounds(BlockSoundGroup.GRASS)
            .strength(1.5f), "desert"));

    public static final Block ACACIA_VARIANT = registerBlock("acacia_variant",
        new SpecialTreeBlock(settingsWithKey("acacia_variant")
            .mapColor(MapColor.ORANGE)
            .sounds(BlockSoundGroup.WOOD)
            .strength(2.0f)
            .requiresTool(), "savanna"));

    public static final Block CONIFER_VARIANT = registerBlock("conifer_variant",
        new SpecialTreeBlock(settingsWithKey("conifer_variant")
            .mapColor(MapColor.SPRUCE_BROWN)
            .sounds(BlockSoundGroup.WOOD)
            .strength(2.0f)
            .requiresTool(), "taiga"));

    public static final Block MILKWEED = registerBlock("milkweed",
        new SpecialTreeBlock(settingsWithKey("milkweed")
            .mapColor(MapColor.LIME)
            .sounds(BlockSoundGroup.GRASS)
            .strength(0.5f), "plains"));

    public static final Block MANGROVE_VARIANT = registerBlock("mangrove_variant",
        new SpecialTreeBlock(settingsWithKey("mangrove_variant")
            .mapColor(MapColor.DARK_GREEN)
            .sounds(BlockSoundGroup.WOOD)
            .strength(2.0f)
            .requiresTool(), "swamp"));

    public static final Block GLOWING_MUSHROOM = registerBlock("glowing_mushroom",
        new SpecialTreeBlock(settingsWithKey("glowing_mushroom")
            .mapColor(MapColor.PURPLE)
            .sounds(BlockSoundGroup.FUNGUS)
            .strength(0.5f)
            .luminance(state -> 10), "mushroom"));

    public static final Block FLOWERING_BIRCH = registerBlock("flowering_birch",
        new SpecialTreeBlock(settingsWithKey("flowering_birch")
            .mapColor(MapColor.WHITE)
            .sounds(BlockSoundGroup.WOOD)
            .strength(2.0f)
            .requiresTool(), "birch_forest"));

    public static final Block ENHANCED_CHERRY = registerBlock("enhanced_cherry",
        new SpecialTreeBlock(settingsWithKey("enhanced_cherry")
            .mapColor(MapColor.PINK)
            .sounds(BlockSoundGroup.CHERRY_WOOD)
            .strength(2.0f)
            .requiresTool(), "cherry_grove"));

    public static final Block ARCTIC_WILLOW = registerBlock("arctic_willow",
        new SpecialTreeBlock(settingsWithKey("arctic_willow")
            .mapColor(MapColor.CYAN)
            .sounds(BlockSoundGroup.WOOD)
            .strength(2.0f)
            .requiresTool(), "snowy"));

    // Pollinator Nest Blocks
    public static final Block FIG_WASP_NEST = registerBlock("fig_wasp_nest",
        new PollinatorNestBlock(settingsWithKey("fig_wasp_nest")
            .mapColor(MapColor.BROWN)
            .sounds(BlockSoundGroup.WOOD)
            .strength(0.5f), "tropical", true));

    public static final Block YUCCA_COCOON = registerBlock("yucca_cocoon",
        new PollinatorNestBlock(settingsWithKey("yucca_cocoon")
            .mapColor(MapColor.WHITE)
            .sounds(BlockSoundGroup.WOOL)
            .strength(0.3f), "desert", false));

    public static final Block MASON_WASP_NEST = registerBlock("mason_wasp_nest",
        new PollinatorNestBlock(settingsWithKey("mason_wasp_nest")
            .mapColor(MapColor.STONE_GRAY)
            .sounds(BlockSoundGroup.STONE)
            .strength(1.0f), "savanna", true));

    public static final Block SAWFLY_COCOON = registerBlock("sawfly_cocoon",
        new PollinatorNestBlock(settingsWithKey("sawfly_cocoon")
            .mapColor(MapColor.TERRACOTTA_BROWN)
            .sounds(BlockSoundGroup.WOOD)
            .strength(0.5f), "taiga", false));

    public static final Block MONARCH_CHRYSALIS = registerBlock("monarch_chrysalis",
        new PollinatorNestBlock(settingsWithKey("monarch_chrysalis")
            .mapColor(MapColor.ORANGE)
            .sounds(BlockSoundGroup.WOOL)
            .strength(0.2f), "plains", false));

    public static final Block MANGROVE_BEE_HIVE = registerBlock("mangrove_bee_hive",
        new PollinatorNestBlock(settingsWithKey("mangrove_bee_hive")
            .mapColor(MapColor.BROWN)
            .sounds(BlockSoundGroup.WOOD)
            .strength(0.5f), "swamp", true));

    public static final Block FUNGUS_GNAT_NEST = registerBlock("fungus_gnat_nest",
        new PollinatorNestBlock(settingsWithKey("fungus_gnat_nest")
            .mapColor(MapColor.PURPLE)
            .sounds(BlockSoundGroup.FUNGUS)
            .strength(0.3f), "mushroom", false));

    public static final Block BIRCH_BEE_HIVE = registerBlock("birch_bee_hive",
        new PollinatorNestBlock(settingsWithKey("birch_bee_hive")
            .mapColor(MapColor.WHITE_GRAY)
            .sounds(BlockSoundGroup.WOOD)
            .strength(0.5f), "birch_forest", true));

    public static final Block ORCHARD_BEE_NEST = registerBlock("orchard_bee_nest",
        new PollinatorNestBlock(settingsWithKey("orchard_bee_nest")
            .mapColor(MapColor.PINK)
            .sounds(BlockSoundGroup.WOOD)
            .strength(0.5f), "cherry_grove", false));

    public static final Block BUMBLEBEE_NEST = registerBlock("bumblebee_nest",
        new PollinatorNestBlock(settingsWithKey("bumblebee_nest")
            .mapColor(MapColor.YELLOW)
            .sounds(BlockSoundGroup.WOOL)
            .strength(0.4f), "snowy", false));

    // Special Leaf Blocks
    public static final Block FIG_LEAVES = registerBlock("fig_leaves",
        new SpecialLeavesBlock(settingsWithKey("fig_leaves")
            .mapColor(MapColor.DARK_GREEN)
            .sounds(BlockSoundGroup.AZALEA_LEAVES)
            .strength(0.2f)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    public static final Block YUCCA_LEAVES = registerBlock("yucca_leaves",
        new SpecialLeavesBlock(settingsWithKey("yucca_leaves")
            .mapColor(MapColor.GREEN)
            .sounds(BlockSoundGroup.AZALEA_LEAVES)
            .strength(0.2f)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    public static final Block ACACIA_VARIANT_LEAVES = registerBlock("acacia_variant_leaves",
        new SpecialLeavesBlock(settingsWithKey("acacia_variant_leaves")
            .mapColor(MapColor.ORANGE)
            .sounds(BlockSoundGroup.AZALEA_LEAVES)
            .strength(0.2f)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    public static final Block CONIFER_VARIANT_LEAVES = registerBlock("conifer_variant_leaves",
        new SpecialLeavesBlock(settingsWithKey("conifer_variant_leaves")
            .mapColor(MapColor.SPRUCE_BROWN)
            .sounds(BlockSoundGroup.AZALEA_LEAVES)
            .strength(0.2f)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    public static final Block MILKWEED_LEAVES = registerBlock("milkweed_leaves",
        new SpecialLeavesBlock(settingsWithKey("milkweed_leaves")
            .mapColor(MapColor.LIME)
            .sounds(BlockSoundGroup.AZALEA_LEAVES)
            .strength(0.2f)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    public static final Block MANGROVE_VARIANT_LEAVES = registerBlock("mangrove_variant_leaves",
        new SpecialLeavesBlock(settingsWithKey("mangrove_variant_leaves")
            .mapColor(MapColor.DARK_GREEN)
            .sounds(BlockSoundGroup.AZALEA_LEAVES)
            .strength(0.2f)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    public static final Block GLOWING_MUSHROOM_LEAVES = registerBlock("glowing_mushroom_leaves",
        new SpecialLeavesBlock(settingsWithKey("glowing_mushroom_leaves")
            .mapColor(MapColor.PURPLE)
            .sounds(BlockSoundGroup.FUNGUS)
            .strength(0.2f)
            .luminance(state -> 7)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    public static final Block FLOWERING_BIRCH_LEAVES = registerBlock("flowering_birch_leaves",
        new SpecialLeavesBlock(settingsWithKey("flowering_birch_leaves")
            .mapColor(MapColor.WHITE)
            .sounds(BlockSoundGroup.AZALEA_LEAVES)
            .strength(0.2f)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    public static final Block ENHANCED_CHERRY_LEAVES = registerBlock("enhanced_cherry_leaves",
        new SpecialLeavesBlock(settingsWithKey("enhanced_cherry_leaves")
            .mapColor(MapColor.PINK)
            .sounds(BlockSoundGroup.CHERRY_LEAVES)
            .strength(0.2f)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    public static final Block ARCTIC_WILLOW_LEAVES = registerBlock("arctic_willow_leaves",
        new SpecialLeavesBlock(settingsWithKey("arctic_willow_leaves")
            .mapColor(MapColor.CYAN)
            .sounds(BlockSoundGroup.AZALEA_LEAVES)
            .strength(0.2f)
            .nonOpaque()
            .allowsSpawning((state, world, pos, type) -> false)
            .suffocates((state, world, pos) -> false)
            .blockVision((state, world, pos) -> false)));

    private static RegistryKey<Block> keyOf(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(SymbioticSurvival.MOD_ID, name));
    }

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, keyOf(name), block);
    }

    private static AbstractBlock.Settings settingsWithKey(String name) {
        return AbstractBlock.Settings.create().registryKey(keyOf(name));
    }

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering blocks for " + SymbioticSurvival.MOD_ID);
    }
}
