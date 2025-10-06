package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> SYMBIOTIC_SURVIVAL_GROUP = RegistryKey.of(
        RegistryKeys.ITEM_GROUP,
        Identifier.of(SymbioticSurvival.MOD_ID, "symbiotic_survival")
    );

    public static final ItemGroup SYMBIOTIC_SURVIVAL = FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModItems.FIG_TREE))
        .displayName(Text.translatable("itemGroup.symbioticsurvival.symbiotic_survival"))
        .entries((context, entries) -> {
            // Special Trees
            entries.add(ModItems.FIG_TREE);
            entries.add(ModItems.YUCCA_PLANT);
            entries.add(ModItems.ACACIA_VARIANT);
            entries.add(ModItems.CONIFER_VARIANT);
            entries.add(ModItems.MILKWEED);
            entries.add(ModItems.MANGROVE_VARIANT);
            entries.add(ModItems.GLOWING_MUSHROOM);
            entries.add(ModItems.FLOWERING_BIRCH);
            entries.add(ModItems.ENHANCED_CHERRY);
            entries.add(ModItems.ARCTIC_WILLOW);

            // Pollinator Nests
            entries.add(ModItems.FIG_WASP_NEST);
            entries.add(ModItems.YUCCA_COCOON);
            entries.add(ModItems.MASON_WASP_NEST);
            entries.add(ModItems.SAWFLY_COCOON);
            entries.add(ModItems.MONARCH_CHRYSALIS);
            entries.add(ModItems.MANGROVE_BEE_HIVE);
            entries.add(ModItems.FUNGUS_GNAT_NEST);
            entries.add(ModItems.BIRCH_BEE_HIVE);
            entries.add(ModItems.ORCHARD_BEE_NEST);
            entries.add(ModItems.BUMBLEBEE_NEST);

            // Fruits
            entries.add(ModItems.FIG);
            entries.add(ModItems.YUCCA_POD);
            entries.add(ModItems.ACACIA_SEED_POD);
            entries.add(ModItems.PINE_CONE);
            entries.add(ModItems.MILKWEED_POD);
            entries.add(ModItems.MANGROVE_FRUIT);
            entries.add(ModItems.GLOWING_SPORE);
            entries.add(ModItems.BIRCH_CATKIN);
            entries.add(ModItems.CHERRY);
            entries.add(ModItems.WILLOW_CATKIN);

            // Spawn Eggs
            entries.add(ModItems.HONEYGUIDE_SPAWN_EGG);
            entries.add(ModItems.FIG_WASP_SPAWN_EGG);
            entries.add(ModItems.YUCCA_MOTH_SPAWN_EGG);
            entries.add(ModItems.MONARCH_BUTTERFLY_SPAWN_EGG);
            entries.add(ModItems.MASON_WASP_SPAWN_EGG);
            entries.add(ModItems.SAWFLY_SPAWN_EGG);
            entries.add(ModItems.MANGROVE_POLLINATOR_SPAWN_EGG);
            entries.add(ModItems.FUNGUS_GNAT_SPAWN_EGG);
            entries.add(ModItems.BIRCH_POLLINATOR_SPAWN_EGG);
            entries.add(ModItems.ORCHARD_BEE_SPAWN_EGG);
            entries.add(ModItems.BUMBLEBEE_SPAWN_EGG);
        })
        .build();

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, SYMBIOTIC_SURVIVAL_GROUP, SYMBIOTIC_SURVIVAL);
        SymbioticSurvival.LOGGER.info("Registering item groups for " + SymbioticSurvival.MOD_ID);
    }
}
