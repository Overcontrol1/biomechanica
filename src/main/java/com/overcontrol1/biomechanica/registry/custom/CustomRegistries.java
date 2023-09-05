package com.overcontrol1.biomechanica.registry.custom;

import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.item.util.CoreType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;

public class CustomRegistries {
    public static final Registry<Biotech> BIOTECH = FabricRegistryBuilder.createSimple(CustomRegistryKeys.BIOTECH).buildAndRegister();
    public static final Registry<CoreType> CORE_TYPES = FabricRegistryBuilder.createSimple(CustomRegistryKeys.CORE_TYPES).buildAndRegister();
}
