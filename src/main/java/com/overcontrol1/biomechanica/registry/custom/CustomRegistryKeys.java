package com.overcontrol1.biomechanica.registry.custom;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.biotech.Biotech;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomRegistryKeys {
    public static final RegistryKey<Registry<Biotech>> BIOTECH = RegistryKey.ofRegistry(new Identifier(Biomechanica.MOD_ID, "biotech"));
}
