package com.overcontrol1.biomechanica.item;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.cca.BiomechanicaItemComponents;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.item.util.DynamicModelItem;
import com.overcontrol1.biomechanica.registry.CoreTypeRegistry;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;

public class BiotechExoskeletonItem extends Item implements DynamicModelItem {
    private static final String BASE_PATH = "item/exoskeleton/";
    public BiotechExoskeletonItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient || hand != Hand.MAIN_HAND) {
            return super.use(world, user, hand);
        }

        ItemStack stack = user.getMainHandStack();

        BiomechanicaItemComponents.CORE_TYPE.get(stack).setCoreType(CoreTypeRegistry.IGNIS);

        return super.use(world, user, hand);
    }

    @Override
    public Identifier getModel(ItemStack stack) {
        CoreType coreType = BiomechanicaItemComponents.CORE_TYPE.get(stack).getCoreType();
        if (coreType != null) {
            return Objects.requireNonNull(CustomRegistries.CORE_TYPES.getId(coreType)).withPrefixedPath(BASE_PATH);
        } else {
            return new Identifier(Biomechanica.MOD_ID, BASE_PATH + "default");
        }
    }
}