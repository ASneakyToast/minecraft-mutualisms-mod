package com.symbioticsurvival.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Configuration for BiomePairFeature.
 * Controls placement parameters for tree-nest pairs.
 */
public record BiomePairConfig(
    int nestMaxDistance,
    int placementAttempts
) implements FeatureConfig {

    public static final Codec<BiomePairConfig> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.fieldOf("nest_max_distance").forGetter(BiomePairConfig::nestMaxDistance),
            Codec.INT.fieldOf("placement_attempts").forGetter(BiomePairConfig::placementAttempts)
        ).apply(instance, BiomePairConfig::new)
    );

    /**
     * Default configuration: nests within 20 blocks, 8 placement attempts
     */
    public static final BiomePairConfig DEFAULT = new BiomePairConfig(20, 8);
}
