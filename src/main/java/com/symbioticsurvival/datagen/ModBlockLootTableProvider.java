package com.symbioticsurvival.datagen;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.SpecialLeavesBlock;
import com.symbioticsurvival.registry.ModBlocks;
import com.symbioticsurvival.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
 * Data generation provider for block loot tables.
 * Defines what items are dropped when blocks are broken.
 */
public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        // Tree blocks - drop themselves
        addDrop(ModBlocks.FIG_TREE);
        addDrop(ModBlocks.YUCCA_PLANT);
        addDrop(ModBlocks.ACACIA_VARIANT);
        addDrop(ModBlocks.CONIFER_VARIANT);
        addDrop(ModBlocks.MILKWEED);
        addDrop(ModBlocks.MANGROVE_VARIANT);
        addDrop(ModBlocks.GLOWING_MUSHROOM);
        addDrop(ModBlocks.FLOWERING_BIRCH);
        addDrop(ModBlocks.ENHANCED_CHERRY);
        addDrop(ModBlocks.ARCTIC_WILLOW);

        // Leaves - custom loot with fruit drops when FRUIT_STATE = 2 (ripe)
        addLeavesWithFruitDrop(ModBlocks.FIG_LEAVES, ModBlocks.FIG_SAPLING, ModItems.FIG);
        addLeavesWithFruitDrop(ModBlocks.YUCCA_LEAVES, ModBlocks.YUCCA_SAPLING, ModItems.YUCCA_POD);
        addLeavesWithFruitDrop(ModBlocks.ACACIA_VARIANT_LEAVES, ModBlocks.ACACIA_VARIANT_SAPLING, ModItems.ACACIA_SEED_POD);
        addLeavesWithFruitDrop(ModBlocks.CONIFER_VARIANT_LEAVES, ModBlocks.CONIFER_VARIANT_SAPLING, ModItems.PINE_CONE);
        addLeavesWithFruitDrop(ModBlocks.MILKWEED_LEAVES, ModBlocks.MILKWEED_SAPLING, ModItems.MILKWEED_POD);
        addLeavesWithFruitDrop(ModBlocks.MANGROVE_VARIANT_LEAVES, ModBlocks.MANGROVE_VARIANT_SAPLING, ModItems.MANGROVE_FRUIT);
        addLeavesWithFruitDrop(ModBlocks.GLOWING_MUSHROOM_LEAVES, ModBlocks.GLOWING_MUSHROOM_SAPLING, ModItems.GLOWING_SPORE);
        addLeavesWithFruitDrop(ModBlocks.FLOWERING_BIRCH_LEAVES, ModBlocks.FLOWERING_BIRCH_SAPLING, ModItems.BIRCH_CATKIN);
        addLeavesWithFruitDrop(ModBlocks.ENHANCED_CHERRY_LEAVES, ModBlocks.ENHANCED_CHERRY_SAPLING, ModItems.CHERRY);
        addLeavesWithFruitDrop(ModBlocks.ARCTIC_WILLOW_LEAVES, ModBlocks.ARCTIC_WILLOW_SAPLING, ModItems.WILLOW_CATKIN);

        // Pollinator nests - drop themselves
        addDrop(ModBlocks.FIG_WASP_NEST);
        addDrop(ModBlocks.YUCCA_COCOON);
        addDrop(ModBlocks.MASON_WASP_NEST);
        addDrop(ModBlocks.SAWFLY_COCOON);
        addDrop(ModBlocks.MONARCH_CHRYSALIS);
        addDrop(ModBlocks.MANGROVE_BEE_HIVE);
        addDrop(ModBlocks.FUNGUS_GNAT_NEST);
        addDrop(ModBlocks.BIRCH_BEE_HIVE);
        addDrop(ModBlocks.ORCHARD_BEE_NEST);
        addDrop(ModBlocks.BUMBLEBEE_NEST);

        // Saplings - drop themselves
        addDrop(ModBlocks.FIG_SAPLING);
        addDrop(ModBlocks.YUCCA_SAPLING);
        addDrop(ModBlocks.ACACIA_VARIANT_SAPLING);
        addDrop(ModBlocks.CONIFER_VARIANT_SAPLING);
        addDrop(ModBlocks.MILKWEED_SAPLING);
        addDrop(ModBlocks.MANGROVE_VARIANT_SAPLING);
        addDrop(ModBlocks.GLOWING_MUSHROOM_SAPLING);
        addDrop(ModBlocks.FLOWERING_BIRCH_SAPLING);
        addDrop(ModBlocks.ENHANCED_CHERRY_SAPLING);
        addDrop(ModBlocks.ARCTIC_WILLOW_SAPLING);

        SymbioticSurvival.LOGGER.info("Generated block loot tables for " + SymbioticSurvival.MOD_ID);
    }

    /**
     * Custom loot table for leaves that drop fruit when ripe (FRUIT_STATE = 2).
     * Also drops saplings with a chance, similar to vanilla leaves.
     *
     * @param leaves The leaves block
     * @param sapling The sapling to drop
     * @param fruit The fruit item to drop when FRUIT_STATE = 2
     */
    private void addLeavesWithFruitDrop(Block leaves, Block sapling, Item fruit) {
        // Main pool: Always drop the leaves block itself
        LootPool.Builder mainPool = LootPool.builder()
            .rolls(ConstantLootNumberProvider.create(1))
            .with(ItemEntry.builder(leaves));

        // Sapling pool: Drop sapling with a small chance (5%)
        LootPool.Builder saplingPool = LootPool.builder()
            .rolls(ConstantLootNumberProvider.create(1))
            .with(ItemEntry.builder(sapling))
            .conditionally(this.createSilkTouchCondition().invert()); // No sapling when silk touched

        // Fruit pool: Drop 1-3 fruit items ONLY when FRUIT_STATE = 2 (ripe)
        LootPool.Builder fruitPool = LootPool.builder()
            .rolls(ConstantLootNumberProvider.create(1))
            .with(ItemEntry.builder(fruit)
                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)))
            )
            .conditionally(
                BlockStatePropertyLootCondition.builder(leaves)
                    .properties(StatePredicate.Builder.create()
                        .exactMatch(SpecialLeavesBlock.FRUIT_STATE, 2)
                    )
            );

        // Build the complete loot table
        addDrop(leaves, LootTable.builder()
            .pool(mainPool)
            .pool(saplingPool)
            .pool(fruitPool)
        );
    }
}
