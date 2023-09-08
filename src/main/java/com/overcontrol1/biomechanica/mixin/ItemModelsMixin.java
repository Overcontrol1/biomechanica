package com.overcontrol1.biomechanica.mixin;

import com.overcontrol1.biomechanica.item.util.DynamicModelItem;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModels.class)
public class ItemModelsMixin {
    @Shadow @Final private BakedModelManager modelManager;

    @Inject(method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;", at = @At("HEAD"), cancellable = true)
    public void overrideDynamicModels(ItemStack stack, CallbackInfoReturnable<BakedModel> cir) {
        if (stack.getItem() instanceof DynamicModelItem modelItem) {
            BakedModel model = this.modelManager.getModel(modelItem.getDynamicModel(stack));
            cir.setReturnValue(model != null ? model : this.modelManager.getMissingModel());
        }
    }
}
