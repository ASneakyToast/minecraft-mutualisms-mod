package com.symbioticsurvival;

import com.symbioticsurvival.registry.ModEntities;
import com.symbioticsurvival.renderer.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class SymbioticSurvivalClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SymbioticSurvival.LOGGER.info("Initializing Symbiotic Survival client...");

        // TODO: Register spawn egg colors when API is clarified for 1.21.9
        // Spawn eggs may handle colors automatically through SpawnEggItem base class

        // Register entity renderers
        EntityRendererRegistry.register(ModEntities.HONEYGUIDE, HoneyguideRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIG_WASP, FigWaspRenderer::new);
        EntityRendererRegistry.register(ModEntities.YUCCA_MOTH, YuccaMothRenderer::new);
        EntityRendererRegistry.register(ModEntities.MONARCH_BUTTERFLY, MonarchButterflyRenderer::new);
        EntityRendererRegistry.register(ModEntities.MASON_WASP, MasonWaspRenderer::new);
        EntityRendererRegistry.register(ModEntities.SAWFLY, SawflyRenderer::new);
        EntityRendererRegistry.register(ModEntities.MANGROVE_POLLINATOR, MangrovePollinatorRenderer::new);
        EntityRendererRegistry.register(ModEntities.FUNGUS_GNAT, FungusGnatRenderer::new);
        EntityRendererRegistry.register(ModEntities.BIRCH_POLLINATOR, BirchPollinatorRenderer::new);
        EntityRendererRegistry.register(ModEntities.ORCHARD_BEE, OrchardBeeRenderer::new);
        EntityRendererRegistry.register(ModEntities.BUMBLEBEE, BumblebeeRenderer::new);

        // TODO: Register client-side packet handlers
        // ModPackets.registerC2SPackets();

        SymbioticSurvival.LOGGER.info("Symbiotic Survival client initialized!");
    }
}
