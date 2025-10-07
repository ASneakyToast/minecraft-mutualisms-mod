package com.symbioticsurvival.datagen;

import com.symbioticsurvival.worldgen.ModConfiguredFeatures;
import com.symbioticsurvival.worldgen.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
 * Data generation entry point for worldgen features.
 */
public class ModDataGeneration implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // Add dynamic registry provider for worldgen features
        pack.addProvider((FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) ->
            new FabricDynamicRegistryProvider(output, registriesFuture) {
                @Override
                protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
                    entries.addAll(registries.getOrThrow(RegistryKeys.CONFIGURED_FEATURE));
                    entries.addAll(registries.getOrThrow(RegistryKeys.PLACED_FEATURE));
                }

                @Override
                public String getName() {
                    return "Worldgen Features";
                }
            }
        );
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
    }
}
