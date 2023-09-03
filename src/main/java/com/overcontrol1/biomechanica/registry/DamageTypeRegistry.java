package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class DamageTypeRegistry {
    public static final RegistryKey<DamageType> BIO_VIRUS = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(Biomechanica.MOD_ID, "bio_virus"));
}
