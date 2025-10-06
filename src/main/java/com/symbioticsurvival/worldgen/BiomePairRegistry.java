package com.symbioticsurvival.worldgen;

import com.symbioticsurvival.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps biomes to their corresponding tree-nest pairs.
 */
public class BiomePairRegistry {

    private static final Map<RegistryKey<Biome>, BiomePair> BIOME_PAIRS = new HashMap<>();

    static {
        // Tropical: Jungle -> Fig Tree + Fig Wasp Nest
        registerPair(BiomeKeys.JUNGLE, new BiomePair(
            ModBlocks.FIG_TREE,
            ModBlocks.FIG_WASP_NEST,
            "tropical"
        ));

        // Desert: Desert -> Yucca Plant + Yucca Cocoon
        registerPair(BiomeKeys.DESERT, new BiomePair(
            ModBlocks.YUCCA_PLANT,
            ModBlocks.YUCCA_COCOON,
            "desert"
        ));

        // Savanna: Savanna -> Acacia Variant + Mason Wasp Nest
        registerPair(BiomeKeys.SAVANNA, new BiomePair(
            ModBlocks.ACACIA_VARIANT,
            ModBlocks.MASON_WASP_NEST,
            "savanna"
        ));

        // Taiga: Taiga -> Conifer Variant + Sawfly Cocoon
        registerPair(BiomeKeys.TAIGA, new BiomePair(
            ModBlocks.CONIFER_VARIANT,
            ModBlocks.SAWFLY_COCOON,
            "taiga"
        ));

        // Plains: Plains -> Milkweed + Monarch Chrysalis
        registerPair(BiomeKeys.PLAINS, new BiomePair(
            ModBlocks.MILKWEED,
            ModBlocks.MONARCH_CHRYSALIS,
            "plains"
        ));

        // Swamp: Swamp -> Mangrove Variant + Mangrove Bee Hive
        registerPair(BiomeKeys.SWAMP, new BiomePair(
            ModBlocks.MANGROVE_VARIANT,
            ModBlocks.MANGROVE_BEE_HIVE,
            "swamp"
        ));

        // Dark Forest: Dark Forest -> Glowing Mushroom + Fungus Gnat Nest
        registerPair(BiomeKeys.DARK_FOREST, new BiomePair(
            ModBlocks.GLOWING_MUSHROOM,
            ModBlocks.FUNGUS_GNAT_NEST,
            "mushroom"
        ));

        // Birch Forest: Birch Forest -> Flowering Birch + Birch Bee Hive
        registerPair(BiomeKeys.BIRCH_FOREST, new BiomePair(
            ModBlocks.FLOWERING_BIRCH,
            ModBlocks.BIRCH_BEE_HIVE,
            "birch_forest"
        ));

        // Cherry Grove: Cherry Grove -> Enhanced Cherry + Orchard Bee Nest
        registerPair(BiomeKeys.CHERRY_GROVE, new BiomePair(
            ModBlocks.ENHANCED_CHERRY,
            ModBlocks.ORCHARD_BEE_NEST,
            "cherry_grove"
        ));

        // Snowy Taiga: Snowy Taiga -> Arctic Willow + Bumblebee Nest
        registerPair(BiomeKeys.SNOWY_TAIGA, new BiomePair(
            ModBlocks.ARCTIC_WILLOW,
            ModBlocks.BUMBLEBEE_NEST,
            "snowy"
        ));
    }

    private static void registerPair(RegistryKey<Biome> biome, BiomePair pair) {
        BIOME_PAIRS.put(biome, pair);
    }

    public static BiomePair getPairForBiome(RegistryKey<Biome> biome) {
        return BIOME_PAIRS.get(biome);
    }

    public static boolean hasPair(RegistryKey<Biome> biome) {
        return BIOME_PAIRS.containsKey(biome);
    }

    /**
     * Data class holding a biome's tree and nest blocks
     */
    public static class BiomePair {
        public final Block treeBlock;
        public final Block nestBlock;
        public final String biomeType;

        public BiomePair(Block treeBlock, Block nestBlock, String biomeType) {
            this.treeBlock = treeBlock;
            this.nestBlock = nestBlock;
            this.biomeType = biomeType;
        }
    }
}
