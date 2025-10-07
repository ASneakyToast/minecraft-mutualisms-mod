package com.symbioticsurvival.datagen;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

/**
 * Data generation provider for block tags.
 * Adds custom blocks to vanilla tags for proper game mechanics (leaf decay, tool effectiveness, etc.).
 */
public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        // Add all custom tree blocks to vanilla logs tag
        // This allows leaves to recognize them as valid supports for persistence
        // and makes axes effective against them
        getOrCreateTagBuilder(BlockTags.LOGS)
            .add(ModBlocks.FIG_TREE)
            .add(ModBlocks.YUCCA_PLANT)
            .add(ModBlocks.ACACIA_VARIANT)
            .add(ModBlocks.CONIFER_VARIANT)
            .add(ModBlocks.MILKWEED)
            .add(ModBlocks.MANGROVE_VARIANT)
            .add(ModBlocks.GLOWING_MUSHROOM)
            .add(ModBlocks.FLOWERING_BIRCH)
            .add(ModBlocks.ENHANCED_CHERRY)
            .add(ModBlocks.ARCTIC_WILLOW);

        // Add all custom leaves to vanilla leaves tag
        // Enables proper leaf decay mechanics and tool effectiveness
        getOrCreateTagBuilder(BlockTags.LEAVES)
            .add(ModBlocks.FIG_LEAVES)
            .add(ModBlocks.YUCCA_LEAVES)
            .add(ModBlocks.ACACIA_VARIANT_LEAVES)
            .add(ModBlocks.CONIFER_VARIANT_LEAVES)
            .add(ModBlocks.MILKWEED_LEAVES)
            .add(ModBlocks.MANGROVE_VARIANT_LEAVES)
            .add(ModBlocks.GLOWING_MUSHROOM_LEAVES)
            .add(ModBlocks.FLOWERING_BIRCH_LEAVES)
            .add(ModBlocks.ENHANCED_CHERRY_LEAVES)
            .add(ModBlocks.ARCTIC_WILLOW_LEAVES);

        // Add saplings to vanilla saplings tag
        // Enables proper placement mechanics and tool effectiveness
        getOrCreateTagBuilder(BlockTags.SAPLINGS)
            .add(ModBlocks.FIG_SAPLING)
            .add(ModBlocks.YUCCA_SAPLING)
            .add(ModBlocks.ACACIA_VARIANT_SAPLING)
            .add(ModBlocks.CONIFER_VARIANT_SAPLING)
            .add(ModBlocks.MILKWEED_SAPLING)
            .add(ModBlocks.MANGROVE_VARIANT_SAPLING)
            .add(ModBlocks.GLOWING_MUSHROOM_SAPLING)
            .add(ModBlocks.FLOWERING_BIRCH_SAPLING)
            .add(ModBlocks.ENHANCED_CHERRY_SAPLING)
            .add(ModBlocks.ARCTIC_WILLOW_SAPLING);

        SymbioticSurvival.LOGGER.info("Generated block tags for " + SymbioticSurvival.MOD_ID);
    }
}
