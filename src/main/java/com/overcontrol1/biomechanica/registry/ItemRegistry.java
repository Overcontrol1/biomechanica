package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.item.BiotechAnimatableItem;
import com.overcontrol1.biomechanica.item.BiotechCoreItem;
import com.overcontrol1.biomechanica.item.BiotechExoskeletonItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {
    public static final Item BIOTECH_ANIMATABLE = registerItem("biotech_animatable",
            new BiotechAnimatableItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE,new FabricItemSettings().maxCount(1)));

    public static final Item BIOTECH_EXOSKELETON = registerItem("biotech_exoskeleton",
            new BiotechExoskeletonItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxCount(1).maxDamage(2048)));

    public static final Item BIOTECH_CORE = registerItem("biotech_core",
            new BiotechCoreItem(new FabricItemSettings().maxCount(1)));


    public static void register() {
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Biomechanica.MOD_ID, name), item);
    }
}
