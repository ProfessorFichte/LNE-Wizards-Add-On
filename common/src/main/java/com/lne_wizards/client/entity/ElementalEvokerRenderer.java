package com.lne_wizards.client.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.util.Identifier;

public class ElementalEvokerRenderer extends IllagerEntityRenderer<SpellcastingIllagerEntity> {

    private final Identifier texture;

    public ElementalEvokerRenderer(EntityRendererFactory.Context context, ElementType elementType) {
        super(context, new EvokerModel(context.getPart(EntityModelLayers.EVOKER)), 0.5F);
        this.texture = getTextureForElement(elementType);
        this.addFeature(new EvokerCastingWandFeatureRenderer(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(SpellcastingIllagerEntity entity) {
        return texture;
    }

    private static Identifier getTextureForElement(ElementType elementType) {
        return Identifier.of("lne_wizards", "textures/entity/evoker/" + elementType.name().toLowerCase() + "_evoker.png");
    }

    static final class EvokerModel extends IllagerEntityModel<SpellcastingIllagerEntity> {
        private final ModelPart root;

        EvokerModel(ModelPart root) {
            super(root);
            this.root = root;
        }

        ModelPart getRightArm() {
            return root.getChild(EntityModelPartNames.RIGHT_ARM);
        }
    }

    public enum ElementType {
        AIR,
        ARCANE,
        EARTH,
        FIRE,
        FROST,
        WATER
    }

    public static class Air extends ElementalEvokerRenderer {
        public Air(EntityRendererFactory.Context context) {
            super(context, ElementType.AIR);
        }
    }

    public static class Arcane extends ElementalEvokerRenderer {
        public Arcane(EntityRendererFactory.Context context) {
            super(context, ElementType.ARCANE);
        }
    }

    public static class Earth extends ElementalEvokerRenderer {
        public Earth(EntityRendererFactory.Context context) {
            super(context, ElementType.EARTH);
        }
    }

    public static class Fire extends ElementalEvokerRenderer {
        public Fire(EntityRendererFactory.Context context) {
            super(context, ElementType.FIRE);
        }
    }

    public static class Frost extends ElementalEvokerRenderer {
        public Frost(EntityRendererFactory.Context context) {
            super(context, ElementType.FROST);
        }
    }

    public static class Water extends ElementalEvokerRenderer {
        public Water(EntityRendererFactory.Context context) {
            super(context, ElementType.WATER);
        }
    }
}
