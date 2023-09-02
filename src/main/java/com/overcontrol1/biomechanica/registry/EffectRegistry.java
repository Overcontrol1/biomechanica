package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.effect.BioVirusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EffectRegistry {
    public static final StatusEffect BIO_VIRUS = registerEffect("bio_virus", new BioVirusEffect());

    public static void register() {

    }

    private static StatusEffect registerEffect(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Biomechanica.MOD_ID, name), effect);
    }
}
