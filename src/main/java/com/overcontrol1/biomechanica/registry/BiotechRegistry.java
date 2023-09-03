package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.biotech.custom.BackClawBiotech;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BiotechRegistry {
    public static final Biotech BACK_CLAW = registerBiotech("back_claw", new BackClawBiotech());

    public static void register() {

    }

    public static Biotech registerBiotech(String name, Biotech biotech) {
        return Registry.register(CustomRegistries.BIOTECH, new Identifier(Biomechanica.MOD_ID, name), biotech);
    }
}
