package com.overcontrol1.biomechanica.item;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.cca.BiomechanicaItemComponents;
import com.overcontrol1.biomechanica.cca.component.item.ItemCoreStorageComponent;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.item.util.DynamicModelItem;
import com.overcontrol1.biomechanica.registry.CoreTypeRegistry;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;

public class BiotechCoreItem extends Item implements DynamicModelItem {
    private static final String BASE_PATH = "item/core/";

    public BiotechCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient || hand != Hand.MAIN_HAND) {
            return super.use(world, user, hand);
        }

        ItemCoreStorageComponent component = BiomechanicaItemComponents.CORE_TYPE.get(user.getStackInHand(hand));

        if (component.getCoreType() != null) {
            int index = CustomRegistries.CORE_TYPES.getRawId(component.getCoreType());

            component.setCoreType(CustomRegistries.CORE_TYPES.get((index + 1) % CustomRegistries.CORE_TYPES.size()));
        } else {
            component.setCoreType(CoreTypeRegistry.CUSTOS);
        }


        return super.use(world, user, hand);
    }

    @Override
    public Text getName(ItemStack stack) {
        CoreType coreType = BiomechanicaItemComponents.CORE_TYPE.get(stack).getCoreType();
        if (coreType == null) {
            return super.getName(stack);
        }

        Identifier coreTypeId = CustomRegistries.CORE_TYPES.getId(coreType);

        if (coreTypeId == null) {
            throw new IllegalStateException("Unregistered CoreType: " + coreType + ", Translation Key: " + coreType.translationKey());
        }

        return Text.translatable(coreTypeId.toTranslationKey("coreType")).append(" ").append(super.getName());
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
}
