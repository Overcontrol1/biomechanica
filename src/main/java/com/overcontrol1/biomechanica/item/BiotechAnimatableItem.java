package com.overcontrol1.biomechanica.item;

import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.client.animation.CustomDataTickets;
import com.overcontrol1.biomechanica.client.renderer.BiotechRenderer;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class BiotechAnimatableItem extends ArmorItem implements GeoItem {
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    public BiotechAnimatableItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private BiotechRenderer renderer;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if (this.renderer == null) {
                    this.renderer = new BiotechRenderer();
                }

                return this.renderer;
            }
        });
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.literal("You are not supposed to have this.").formatted(Formatting.RED));
        tooltip.add(Text.literal("It has no use.").formatted(Formatting.YELLOW));
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, 0, state -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null) {
                // ANIMATIONS HERE
                Biotech currentTech = state.getData(CustomDataTickets.BIOTECH);
                String path = Objects.requireNonNull(CustomRegistries.BIOTECH.getId(currentTech)).getPath();
                if (Arrays.stream(currentTech.getAnimationTypes())
                        .anyMatch(biotechAnimationState -> biotechAnimationState == state.getData(CustomDataTickets.BIOTECH_ANIMATION_STATE))) {
                    state.setAnimation(RawAnimation.begin()
                            .then("animation." + path + "." + state.getData(CustomDataTickets.BIOTECH_ANIMATION_STATE).animationName, Animation.LoopType.LOOP));
                }

                return PlayState.CONTINUE;
            }
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
