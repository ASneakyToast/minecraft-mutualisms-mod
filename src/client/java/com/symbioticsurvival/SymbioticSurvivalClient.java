package com.symbioticsurvival;

import com.symbioticsurvival.registry.ModBlocks;
import com.symbioticsurvival.registry.ModEntities;
import com.symbioticsurvival.renderer.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
// TODO: Re-enable when Fabric API is updated for 1.21.9
// import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
// import net.minecraft.client.render.RenderLayer;

public class SymbioticSurvivalClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SymbioticSurvival.LOGGER.info("Initializing Symbiotic Survival client...");

        // Register block render layers for transparency
        // TODO: Re-enable when Fabric API is updated
        // registerBlockRenderLayers();

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

    // TODO: Re-enable when Fabric API is updated for 1.21.9
    // Leaves should render with transparency by default since they extend LeavesBlock
    /*
    private void registerBlockRenderLayers() {
        // Register all leaf blocks with cutout rendering for transparency
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FIG_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.YUCCA_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ACACIA_VARIANT_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CONIFER_VARIANT_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MILKWEED_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MANGROVE_VARIANT_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GLOWING_MUSHROOM_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FLOWERING_BIRCH_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENHANCED_CHERRY_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ARCTIC_WILLOW_LEAVES, RenderLayer.getCutout());

        // Register all nest and cocoon blocks with cutout rendering for transparency
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FIG_WASP_NEST, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.YUCCA_COCOON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MASON_WASP_NEST, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SAWFLY_COCOON, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MONARCH_CHRYSALIS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MANGROVE_BEE_HIVE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FUNGUS_GNAT_NEST, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BIRCH_BEE_HIVE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ORCHARD_BEE_NEST, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BUMBLEBEE_NEST, RenderLayer.getCutout());
    }
    */
}
