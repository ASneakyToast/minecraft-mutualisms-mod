package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.worldgen.BiomePairConfig;
import com.symbioticsurvival.worldgen.BiomePairFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;

/**
 * Registers custom world generation features.
 */
public class ModFeatures {

    // TODO: Feature registration API may have changed in 1.21.9, needs verification
    public static final Feature<BiomePairConfig> BIOME_PAIR_FEATURE =
        Registry.register(
            Registries.FEATURE,
            Identifier.of(SymbioticSurvival.MOD_ID, "biome_pair"),
            new BiomePairFeature(BiomePairConfig.CODEC)
        );

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering world features for " + SymbioticSurvival.MOD_ID);
    }
}
