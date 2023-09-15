package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.cca.BiomechanicaItemComponents;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupRegistry {
    public static final ItemGroup MAIN = registerItemGroup("main", FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.biomechanica.main"))
            .icon(() -> new ItemStack(BlockRegistry.BIOTECH_CRAFTING_STATION)).entries((displayContext, entries) -> {
                entries.add(BlockRegistry.BIOTECH_CRAFTING_STATION);
                entries.add(BlockRegistry.BIOTECH_CORE_INSERTER);
                entries.add(ItemRegistry.BIOTECH_CORE);
                entries.add(ItemRegistry.BIOTECH_EXOSKELETON);

                for (CoreType coreType : CustomRegistries.CORE_TYPES) {
                    ItemStack core = new ItemStack(ItemRegistry.BIOTECH_CORE);
                    BiomechanicaItemComponents.CORE_TYPE.get(core).setCoreType(coreType);
                    entries.add(core);

                    ItemStack exoskeleton = new ItemStack(ItemRegistry.BIOTECH_EXOSKELETON);
                    BiomechanicaItemComponents.CORE_TYPE.get(exoskeleton).setCoreType(coreType);
                    entries.add(exoskeleton);
                }
            }).build());

    public static void register() {

    }

    private static ItemGroup registerItemGroup(String name, ItemGroup group) {
        return Registry.register(Registries.ITEM_GROUP, new Identifier(Biomechanica.MOD_ID, name), group);
    }
}
