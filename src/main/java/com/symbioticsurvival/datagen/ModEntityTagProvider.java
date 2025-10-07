package com.symbioticsurvival.datagen;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.registry.ModEntities;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

/**
 * Data generation provider for entity type tags.
 * Adds custom entities to vanilla tags for proper game mechanics (fall damage immunity, etc.).
 */
public class ModEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    public ModEntityTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        // Add all flying pollinators to fall damage immune tag
        // This prevents them from dying when descending from trees
        getOrCreateTagBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE)
            .add(ModEntities.FIG_WASP)
            .add(ModEntities.YUCCA_MOTH)
            .add(ModEntities.MONARCH_BUTTERFLY)
            .add(ModEntities.MASON_WASP)
            .add(ModEntities.SAWFLY)
            .add(ModEntities.MANGROVE_POLLINATOR)
            .add(ModEntities.FUNGUS_GNAT)
            .add(ModEntities.BIRCH_POLLINATOR)
            .add(ModEntities.ORCHARD_BEE)
            .add(ModEntities.BUMBLEBEE);

        SymbioticSurvival.LOGGER.info("Generated entity tags for " + SymbioticSurvival.MOD_ID);
    }
}
