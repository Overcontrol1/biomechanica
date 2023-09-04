package com.overcontrol1.biomechanica.client.renderer;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.biotech.BiotechHolder;
import com.overcontrol1.biomechanica.client.animation.CustomDataTickets;
import com.overcontrol1.biomechanica.client.model.DefaultedBiotechModel;
import com.overcontrol1.biomechanica.item.BiotechAnimatableItem;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Map;

public class BiotechRenderer extends GeoArmorRenderer<BiotechAnimatableItem> {
    private static final GeoModel<BiotechAnimatableItem> DEFAULT =
            new DefaultedBiotechModel<>(new Identifier(Biomechanica.MOD_ID, "biotech_default"));
    private static final Map<Biotech, GeoModel<BiotechAnimatableItem>> MODEL_MAP = new Object2ObjectArrayMap<>();
    private Biotech currentTech;

    public static void addModel(Biotech biotech, GeoModel<BiotechAnimatableItem> model) {
        MODEL_MAP.put(biotech, model);
    }

    public void setCurrentTech(Biotech tech) {
        this.currentTech = tech;
    }

    public EquipmentSlot getCurrentBiotechEquipmentSlot() {
        return this.currentTech.getBodypart();
    }

    public BiotechRenderer() {
        super(DEFAULT);
    }

    @Override
    public GeoModel<BiotechAnimatableItem> getGeoModel() {
        return MODEL_MAP.getOrDefault(this.currentTech, DEFAULT);
    }

    @Override
    @Nullable
    public GeoBone getHeadBone() {
        return this.getGeoModel().getBone("armorHead").orElse(null);
    }

    @Override
    @Nullable
    public GeoBone getBodyBone() {
        return this.getGeoModel().getBone("armorBody").orElse(null);
    }

    @Override
    @Nullable
    public GeoBone getRightArmBone() {
        return this.getGeoModel().getBone("armorRightArm").orElse(null);
    }

    @Override
    @Nullable
    public GeoBone getLeftArmBone() {
        return this.getGeoModel().getBone("armorLeftArm").orElse(null);
    }

    @Override
    @Nullable
    public GeoBone getRightLegBone() {
        return this.getGeoModel().getBone("armorRightLeg").orElse(null);
    }

    @Override
    @Nullable
    public GeoBone getLeftLegBone() {
        return this.getGeoModel().getBone("armorLeftLeg").orElse(null);
    }

    @Override
    @Nullable
    public GeoBone getRightBootBone() {
        return this.getGeoModel().getBone("armorRightBoot").orElse(null);
    }

    @Override
    @Nullable
    public GeoBone getLeftBootBone() {
        return this.getGeoModel().getBone("armorLeftBoot").orElse(null);
    }

    @Override
    public void render(MatrixStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.currentTech == null) {
            return;
        }

        super.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, BiotechAnimatableItem animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.push();
        poseStack.translate(0, 24 / 16f, 0);
        poseStack.scale(-1, -1, 1);

        if (!isReRender) {
            AnimationState<BiotechAnimatableItem> animationState = new AnimationState<>(animatable, 0, 0, partialTick, false);
            long instanceId = getInstanceId(animatable);

            animationState.setData(DataTickets.TICK, animatable.getTick(this.currentEntity));
            animationState.setData(DataTickets.ITEMSTACK, this.currentStack);
            animationState.setData(DataTickets.ENTITY, this.currentEntity);
            animationState.setData(DataTickets.EQUIPMENT_SLOT, this.currentSlot);
            animationState.setData(CustomDataTickets.BIOTECH, this.currentTech);
            animationState.setData(CustomDataTickets.BIOTECH_ANIMATION_STATE, ((BiotechHolder) this.currentEntity).getAnimationState());
            this.getGeoModel().addAdditionalStateData(animatable, instanceId, animationState::setData);
            this.getGeoModel().handleAnimations(animatable, instanceId, animationState);
        }

        this.modelRenderTranslations = new Matrix4f(poseStack.peek().getPositionMatrix());

        updateAnimatedTextureFrame(animatable);

        for (GeoBone group : model.topLevelBones()) {
            renderRecursively(poseStack, animatable, group, renderType, bufferSource, buffer, isReRender, partialTick, packedLight,
                    packedOverlay, red, green, blue, alpha);
        }

        poseStack.pop();
    }
}
