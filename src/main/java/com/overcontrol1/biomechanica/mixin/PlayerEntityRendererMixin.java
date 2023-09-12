package com.overcontrol1.biomechanica.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void injectConstructor(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
////        PlayerEntityRenderer renderer = (PlayerEntityRenderer) (Object) this;
////        this.addFeature(new BiotechFeatureRenderer<>(renderer));
//    }
}
