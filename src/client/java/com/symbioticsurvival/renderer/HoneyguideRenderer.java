package com.symbioticsurvival.renderer;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.entity.HoneyguideEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.ChickenEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Renderer for the Honeyguide entity.
 * Uses the chicken model as a placeholder for the bird-like entity.
 */
public class HoneyguideRenderer extends MobEntityRenderer<HoneyguideEntity, ChickenEntityRenderState, ChickenEntityModel> {

    private static final Identifier TEXTURE = Identifier.of(SymbioticSurvival.MOD_ID, "textures/entity/honeyguide.png");

    public HoneyguideRenderer(EntityRendererFactory.Context context) {
        super(context, new ChickenEntityModel(context.getPart(EntityModelLayers.CHICKEN)), 0.3f);
    }

    @Override
    public ChickenEntityRenderState createRenderState() {
        return new ChickenEntityRenderState();
    }

    @Override
    public Identifier getTexture(ChickenEntityRenderState state) {
        return TEXTURE;
    }
}
