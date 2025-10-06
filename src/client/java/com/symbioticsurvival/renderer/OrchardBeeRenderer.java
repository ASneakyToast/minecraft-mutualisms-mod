package com.symbioticsurvival.renderer;

import com.symbioticsurvival.SymbioticSurvival;
import com.symbioticsurvival.entity.pollinator.OrchardBeeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.BeeEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Renderer for the Orchard Bee entity.
 * Uses the bee model as a placeholder for the bee.
 */
public class OrchardBeeRenderer extends MobEntityRenderer<OrchardBeeEntity, BeeEntityRenderState, BeeEntityModel> {

    private static final Identifier TEXTURE = Identifier.of(SymbioticSurvival.MOD_ID, "textures/entity/orchard_bee.png");

    public OrchardBeeRenderer(EntityRendererFactory.Context context) {
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
