package com.symbioticsurvival.block;

import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Optional;

/**
 * Custom sapling block that can grow into special trees with pollinator nests.
 */
public class ModSaplingBlock extends SaplingBlock {

    public ModSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
    }

    /**
     * Creates a SaplingGenerator for a tree feature with optional mega variant.
     * In 1.21, SaplingGenerator is final and uses a different constructor pattern.
     */
    public static SaplingGenerator createGenerator(
        String name,
        RegistryKey<ConfiguredFeature<?, ?>> regularVariant,
        RegistryKey<ConfiguredFeature<?, ?>> megaVariant
    ) {
        return new SaplingGenerator(
            name,
            Optional.of(megaVariant),     // megaVariant - for 2x2 sapling arrangements
            Optional.of(regularVariant),  // regularVariant - for single saplings
            Optional.empty()              // beesVariant - variant with bees
        );
    }

    /**
     * Creates a SaplingGenerator for a tree feature without mega variant.
     * Use this for trees that don't have a 2x2 variant.
     */
    public static SaplingGenerator createGenerator(String name, RegistryKey<ConfiguredFeature<?, ?>> regularVariant) {
        return new SaplingGenerator(
            name,
            Optional.empty(),             // megaVariant - for 2x2 sapling arrangements
            Optional.of(regularVariant),  // regularVariant - for single saplings
            Optional.empty()              // beesVariant - variant with bees
        );
    }
}
