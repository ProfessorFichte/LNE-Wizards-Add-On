package com.lne_wizards.client.entity;

import com.lne_wizards.entity.spell_spawned.ExplosiveBubbleEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.spell_engine.api.render.CustomModels;

public class ExplosiveBubbleRenderer extends EntityRenderer<ExplosiveBubbleEntity> {

    private final ItemRenderer itemRenderer;
    private static final Identifier MODEL_ID = Identifier.of("elemental_wizards_rpg", "spell_projectile/big_bubble");
    private static final RenderLayer RENDER_LAYER = RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

    public ExplosiveBubbleRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ExplosiveBubbleEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        matrices.push();

        float floatOffset = MathHelper.sin((entity.age + tickDelta) * 0.05F) * 0.2F;
        matrices.translate(0.0, 1.1 + floatOffset, 0.0);

        float spinAngle = (entity.age + tickDelta) * 3.0F;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(spinAngle));

        matrices.scale(2.5F, 2.5F, 2.5F);

        CustomModels.render(RENDER_LAYER, itemRenderer, MODEL_ID, matrices, vertexConsumers, light, entity.getId());

        matrices.pop();
    }

    @Override
    public Identifier getTexture(ExplosiveBubbleEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
