package com.overcontrol1.biomechanica.client.renderer.feature;

import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.biotech.BiotechHolder;
import com.overcontrol1.biomechanica.client.renderer.BiotechRenderer;
import com.overcontrol1.biomechanica.registry.ItemRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
public class BiotechFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    private Identifier texture;
    private static final ItemStack dummyStack = new ItemStack(ItemRegistry.BIOTECH_ANIMATABLE);
    public BiotechFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!(entity instanceof BiotechHolder biotechHolder) || entity.isSpectator() || biotechHolder.getAttachedBiotech().isEmpty()) {
            return;
        }

        BiotechRenderer renderer = (BiotechRenderer) ((RenderProvider)((GeoItem) ItemRegistry.BIOTECH_ANIMATABLE).getRenderProvider().get())
                .getGenericArmorModel(entity, dummyStack, EquipmentSlot.CHEST, (BipedEntityModel<LivingEntity>) this.getContextModel());

        if (renderer != null) {
            for (Biotech biotech : biotechHolder.getAttachedBiotech()) {
                VertexConsumer vertices = vertexConsumers.getBuffer(biotech.getRenderLayer(renderer.getTextureLocation(renderer.getAnimatable()) != null ? renderer.getTextureLocation(renderer.getAnimatable()) : MissingSprite.getMissingSpriteId()));

                renderer.setCurrentTech(biotech);
                renderer.prepForRender(entity, dummyStack, biotech.getBodypart(), this.getContextModel());
                renderer.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            }
        }
    }
}
