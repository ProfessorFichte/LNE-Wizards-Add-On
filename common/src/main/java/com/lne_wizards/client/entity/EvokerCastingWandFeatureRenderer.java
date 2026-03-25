package com.lne_wizards.client.entity;

import com.lne_wizards.entity.mob.elemental_evokers.ElementalEvokerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class EvokerCastingWandFeatureRenderer extends FeatureRenderer<SpellcastingIllagerEntity, IllagerEntityModel<SpellcastingIllagerEntity>> {

    private final HeldItemRenderer heldItemRenderer;

    public EvokerCastingWandFeatureRenderer(
            FeatureRendererContext<SpellcastingIllagerEntity, IllagerEntityModel<SpellcastingIllagerEntity>> context,
            HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       SpellcastingIllagerEntity entity, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {

        if (!(entity instanceof ElementalEvokerEntity evoker) || !evoker.isSpellcasting()) return;

        Identifier wandId = evoker.getWandItemId();
        if (!Registries.ITEM.containsId(wandId)) return;

        matrices.push();
        ((ElementalEvokerRenderer.EvokerModel) getContextModel()).getRightArm().rotate(matrices);
        matrices.translate(-0.22F, 0.6285715F, -0.012F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(360.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-360.0F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        heldItemRenderer.renderItem(entity, new ItemStack(Registries.ITEM.get(wandId)),
                ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, false, matrices, vertexConsumers, light);
        matrices.pop();
    }
}
