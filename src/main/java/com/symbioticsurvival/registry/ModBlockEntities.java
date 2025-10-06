package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.block.entity.PollinatorNestBlockEntity;
import com.symbioticsurvival.block.entity.SpecialTreeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<SpecialTreeBlockEntity> SPECIAL_TREE = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Identifier.of(SymbioticSurvival.MOD_ID, "special_tree"),
        FabricBlockEntityTypeBuilder.create(
            SpecialTreeBlockEntity::new,
            ModBlocks.FIG_TREE,
            ModBlocks.YUCCA_PLANT,
            ModBlocks.ACACIA_VARIANT,
            ModBlocks.CONIFER_VARIANT,
            ModBlocks.MILKWEED,
            ModBlocks.MANGROVE_VARIANT,
            ModBlocks.GLOWING_MUSHROOM,
            ModBlocks.FLOWERING_BIRCH,
            ModBlocks.ENHANCED_CHERRY,
            ModBlocks.ARCTIC_WILLOW
        ).build()
    );

    public static final BlockEntityType<PollinatorNestBlockEntity> POLLINATOR_NEST = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        Identifier.of(SymbioticSurvival.MOD_ID, "pollinator_nest"),
        FabricBlockEntityTypeBuilder.create(
            PollinatorNestBlockEntity::new,
            ModBlocks.FIG_WASP_NEST,
            ModBlocks.YUCCA_COCOON,
            ModBlocks.MASON_WASP_NEST,
            ModBlocks.SAWFLY_COCOON,
            ModBlocks.MONARCH_CHRYSALIS,
            ModBlocks.MANGROVE_BEE_HIVE,
            ModBlocks.FUNGUS_GNAT_NEST,
            ModBlocks.BIRCH_BEE_HIVE,
            ModBlocks.ORCHARD_BEE_NEST,
            ModBlocks.BUMBLEBEE_NEST
        ).build()
    );

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering block entities for " + SymbioticSurvival.MOD_ID);
    }
}
