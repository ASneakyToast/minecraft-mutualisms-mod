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
     * Creates a SaplingGenerator for a tree feature.
     * In 1.21, SaplingGenerator is final and uses a different constructor pattern.
     */
    public static SaplingGenerator createGenerator(String name, RegistryKey<ConfiguredFeature<?, ?>> treeFeature) {
        return new SaplingGenerator(
            name,
            Optional.of(treeFeature),
            Optional.empty(),
            Optional.empty()
        );
    }
}
