package com.overcontrol1.biomechanica.item;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.cca.BiomechanicaItemComponents;
import com.overcontrol1.biomechanica.client.renderer.item.BiotechExoskeletonRenderer;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.item.util.DynamicModelItem;
import com.overcontrol1.biomechanica.registry.CoreTypeRegistry;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BiotechExoskeletonItem extends ArmorItem implements DynamicModelItem, GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    private static final String BASE_PATH = "item/exoskeleton/";

    public BiotechExoskeletonItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient || hand != Hand.MAIN_HAND) {
            return super.use(world, user, hand);
        }

        ItemStack stack = user.getMainHandStack();

        BiomechanicaItemComponents.CORE_TYPE.get(stack).setCoreType(CoreTypeRegistry.IGNIS);

        return super.use(world, user, hand);
    }

    @Override
    public Identifier getDynamicModel(ItemStack stack) {
        CoreType coreType = BiomechanicaItemComponents.CORE_TYPE.get(stack).getCoreType();
        if (coreType != null) {
            return Objects.requireNonNull(CustomRegistries.CORE_TYPES.getId(coreType)).withPath(BASE_PATH + coreType.biomeId());
        } else {
            return new Identifier(Biomechanica.MOD_ID, BASE_PATH + "default");
        }
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private BiotechExoskeletonRenderer renderer;

            @SuppressWarnings("unchecked")
            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if (this.renderer == null) {
                    this.renderer = new BiotechExoskeletonRenderer();
                }

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, state -> {
            state.setAnimation(RawAnimation.begin().then("animation.biotech_exoskeleton.idle", Animation.LoopType.LOOP));

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}