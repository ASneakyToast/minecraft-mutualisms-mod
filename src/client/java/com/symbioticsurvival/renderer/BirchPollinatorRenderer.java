package com.symbioticsurvival.renderer;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.entity.pollinator.BirchPollinatorEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.BeeEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Renderer for the Birch Pollinator entity.
 * Uses the bee model as a placeholder for the pollinator.
 */
public class BirchPollinatorRenderer extends MobEntityRenderer<BirchPollinatorEntity, BeeEntityRenderState, BeeEntityModel> {

    private static final Identifier TEXTURE = Identifier.of(SymbioticSurvival.MOD_ID, "textures/entity/birch_pollinator.png");

    public BirchPollinatorRenderer(EntityRendererFactory.Context context) {
        super(context, new BeeEntityModel(context.getPart(EntityModelLayers.BEE)), 0.4f);
    }

    @Override
    public BeeEntityRenderState createRenderState() {
        return new BeeEntityRenderState();
    }

    @Override
    public Identifier getTexture(BeeEntityRenderState state) {
        return TEXTURE;
    }
}
