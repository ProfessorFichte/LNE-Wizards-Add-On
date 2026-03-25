package com.lne_wizards.client.entity;

import com.lne_wizards.entity.spell_spawned.IceWallEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.render.CustomModels;

public class IceWallRenderer extends EntityRenderer<IceWallEntity> {

    private final ItemRenderer itemRenderer;
    private static final Identifier MODEL_ID = Identifier.of("lne_wizards", "spell_effect/ice_wall");
    private static final RenderLayer RENDER_LAYER = RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    private static final float WALL_HEIGHT = 3.0F;

    public IceWallRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(IceWallEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float emergeProgress = entity.getEmergeProgress(tickDelta);
        if (emergeProgress <= 0.0F) return;

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        matrices.push();

        float yOffset = -(WALL_HEIGHT * (1.0F - emergeProgress));
        matrices.translate(0.0, yOffset, 0.0);

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(yaw));

        CustomModels.render(RENDER_LAYER, itemRenderer, MODEL_ID, matrices, vertexConsumers, light, entity.getId());

        matrices.pop();
    }

    @Override
    public Identifier getTexture(IceWallEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
