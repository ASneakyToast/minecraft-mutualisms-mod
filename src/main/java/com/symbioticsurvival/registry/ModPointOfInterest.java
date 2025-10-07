package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

/**
 * Registers Points of Interest for pollinator nests.
 * This allows entities (like Honeyguides) to efficiently search for nests
 * without needing to iterate through every block in a radius.
 */
public class ModPointOfInterest {

    /**
     * POI type for all pollinator nests.
     * Similar to how vanilla bees use BEE_HOME for beehives and bee nests.
     */
    public static final RegistryKey<PointOfInterestType> POLLINATOR_NEST_KEY =
        RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE,
            Identifier.of(SymbioticSurvival.MOD_ID, "pollinator_nest"));

    public static final PointOfInterestType POLLINATOR_NEST = PointOfInterestHelper.register(
        Identifier.of(SymbioticSurvival.MOD_ID, "pollinator_nest"),
        1,  // ticketCount: how many entities can claim this POI
        48, // searchDistance: how far entities will search for this POI (in blocks)
        // All pollinator nest blocks
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
    );

    /**
     * Register all POI types.
     * Must be called during mod initialization, AFTER blocks are registered.
     */
    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering Points of Interest for " + SymbioticSurvival.MOD_ID);
    }
}
