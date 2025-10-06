package com.symbioticsurvival.registry;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.entity.HoneyguideEntity;
import com.symbioticsurvival.entity.pollinator.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {

    // Companion Entity
    public static final RegistryKey<EntityType<?>> HONEYGUIDE_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "honeyguide"));
    public static final EntityType<HoneyguideEntity> HONEYGUIDE = Registry.register(
        Registries.ENTITY_TYPE,
        HONEYGUIDE_KEY,
        EntityType.Builder.create(HoneyguideEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.4f, 0.7f)
            .build(HONEYGUIDE_KEY)
    );

    // Pollinator Entities
    public static final RegistryKey<EntityType<?>> FIG_WASP_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "fig_wasp"));
    public static final EntityType<FigWaspEntity> FIG_WASP = Registry.register(
        Registries.ENTITY_TYPE,
        FIG_WASP_KEY,
        EntityType.Builder.create(FigWaspEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.3f, 0.3f)
            .build(FIG_WASP_KEY)
    );

    public static final RegistryKey<EntityType<?>> YUCCA_MOTH_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "yucca_moth"));
    public static final EntityType<YuccaMothEntity> YUCCA_MOTH = Registry.register(
        Registries.ENTITY_TYPE,
        YUCCA_MOTH_KEY,
        EntityType.Builder.create(YuccaMothEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.3f, 0.3f)
            .build(YUCCA_MOTH_KEY)
    );

    public static final RegistryKey<EntityType<?>> MONARCH_BUTTERFLY_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "monarch_butterfly"));
    public static final EntityType<MonarchButterflyEntity> MONARCH_BUTTERFLY = Registry.register(
        Registries.ENTITY_TYPE,
        MONARCH_BUTTERFLY_KEY,
        EntityType.Builder.create(MonarchButterflyEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.4f, 0.4f)
            .build(MONARCH_BUTTERFLY_KEY)
    );

    public static final RegistryKey<EntityType<?>> MASON_WASP_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "mason_wasp"));
    public static final EntityType<MasonWaspEntity> MASON_WASP = Registry.register(
        Registries.ENTITY_TYPE,
        MASON_WASP_KEY,
        EntityType.Builder.create(MasonWaspEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.3f, 0.3f)
            .build(MASON_WASP_KEY)
    );

    public static final RegistryKey<EntityType<?>> SAWFLY_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "sawfly"));
    public static final EntityType<SawflyEntity> SAWFLY = Registry.register(
        Registries.ENTITY_TYPE,
        SAWFLY_KEY,
        EntityType.Builder.create(SawflyEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.3f, 0.3f)
            .build(SAWFLY_KEY)
    );

    public static final RegistryKey<EntityType<?>> MANGROVE_POLLINATOR_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "mangrove_pollinator"));
    public static final EntityType<MangrovePollinatorEntity> MANGROVE_POLLINATOR = Registry.register(
        Registries.ENTITY_TYPE,
        MANGROVE_POLLINATOR_KEY,
        EntityType.Builder.create(MangrovePollinatorEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.3f, 0.3f)
            .build(MANGROVE_POLLINATOR_KEY)
    );

    public static final RegistryKey<EntityType<?>> FUNGUS_GNAT_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "fungus_gnat"));
    public static final EntityType<FungusGnatEntity> FUNGUS_GNAT = Registry.register(
        Registries.ENTITY_TYPE,
        FUNGUS_GNAT_KEY,
        EntityType.Builder.create(FungusGnatEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.3f, 0.3f)
            .build(FUNGUS_GNAT_KEY)
    );

    public static final RegistryKey<EntityType<?>> BIRCH_POLLINATOR_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "birch_pollinator"));
    public static final EntityType<BirchPollinatorEntity> BIRCH_POLLINATOR = Registry.register(
        Registries.ENTITY_TYPE,
        BIRCH_POLLINATOR_KEY,
        EntityType.Builder.create(BirchPollinatorEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.3f, 0.3f)
            .build(BIRCH_POLLINATOR_KEY)
    );

    public static final RegistryKey<EntityType<?>> ORCHARD_BEE_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "orchard_bee"));
    public static final EntityType<OrchardBeeEntity> ORCHARD_BEE = Registry.register(
        Registries.ENTITY_TYPE,
        ORCHARD_BEE_KEY,
        EntityType.Builder.create(OrchardBeeEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.3f, 0.3f)
            .build(ORCHARD_BEE_KEY)
    );

    public static final RegistryKey<EntityType<?>> BUMBLEBEE_KEY =
        RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SymbioticSurvival.MOD_ID, "bumblebee"));
    public static final EntityType<BumblebeeEntity> BUMBLEBEE = Registry.register(
        Registries.ENTITY_TYPE,
        BUMBLEBEE_KEY,
        EntityType.Builder.create(BumblebeeEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.3f, 0.3f)
            .build(BUMBLEBEE_KEY)
    );

    public static void register() {
        SymbioticSurvival.LOGGER.info("Registering entities for " + SymbioticSurvival.MOD_ID);
        // Force static initialization of all entities

        // Register entity attributes
        FabricDefaultAttributeRegistry.register(HONEYGUIDE, HoneyguideEntity.createHoneyguideAttributes());
        FabricDefaultAttributeRegistry.register(FIG_WASP, BasePollinatorEntity.createPollinatorAttributes());
        FabricDefaultAttributeRegistry.register(YUCCA_MOTH, BasePollinatorEntity.createPollinatorAttributes());
        FabricDefaultAttributeRegistry.register(MONARCH_BUTTERFLY, BasePollinatorEntity.createPollinatorAttributes());
        FabricDefaultAttributeRegistry.register(MASON_WASP, BasePollinatorEntity.createPollinatorAttributes());
        FabricDefaultAttributeRegistry.register(SAWFLY, BasePollinatorEntity.createPollinatorAttributes());
        FabricDefaultAttributeRegistry.register(MANGROVE_POLLINATOR, BasePollinatorEntity.createPollinatorAttributes());
        FabricDefaultAttributeRegistry.register(FUNGUS_GNAT, BasePollinatorEntity.createPollinatorAttributes());
        FabricDefaultAttributeRegistry.register(BIRCH_POLLINATOR, BasePollinatorEntity.createPollinatorAttributes());
        FabricDefaultAttributeRegistry.register(ORCHARD_BEE, BasePollinatorEntity.createPollinatorAttributes());
        FabricDefaultAttributeRegistry.register(BUMBLEBEE, BasePollinatorEntity.createPollinatorAttributes());
    }
}
