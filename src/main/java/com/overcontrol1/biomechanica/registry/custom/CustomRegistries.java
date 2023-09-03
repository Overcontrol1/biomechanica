package com.overcontrol1.biomechanica.registry.custom;

import com.overcontrol1.biomechanica.biotech.Biotech;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;

public class CustomRegistries {
    public static final Registry<Biotech> BIOTECH = FabricRegistryBuilder.createSimple(CustomRegistryKeys.BIOTECH).buildAndRegister();
}
