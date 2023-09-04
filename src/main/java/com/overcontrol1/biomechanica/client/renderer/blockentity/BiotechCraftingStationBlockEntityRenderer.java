package com.overcontrol1.biomechanica.client.renderer.blockentity;

import com.overcontrol1.biomechanica.block.entity.BiotechCraftingStationBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class BiotechCraftingStationBlockEntityRenderer implements BlockEntityRenderer<BiotechCraftingStationBlockEntity> {
    private final ItemRenderer itemRenderer;

    public BiotechCraftingStationBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }
    @Override
    public void render(BiotechCraftingStationBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        int lightAbove = WorldRenderer.getLightmapCoordinates(Objects.requireNonNull(entity.getWorld()), entity.getPos().up());

        if (!entity.getCachedResult().isEmpty()) {
            matrices.push();
            matrices.translate(0.5f, 1.02f, 0.4f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotation(MathHelper.PI / 2));
            this.itemRenderer.renderItem(entity.getCachedResult(), ModelTransformationMode.GROUND,
                    lightAbove, overlay, matrices, vertexConsumers,  MinecraftClient.getInstance().world, 0);
            matrices.pop();
        }

    }
}
