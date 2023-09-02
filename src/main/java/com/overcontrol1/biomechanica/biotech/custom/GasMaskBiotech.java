package com.overcontrol1.biomechanica.biotech.custom;

import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.registry.DamageTypeRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;

import java.util.List;

public class GasMaskBiotech extends Biotech {
    @Override
    public EquipmentSlot getBodypart() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public List<RegistryKey<DamageType>> getInvulnerabilities() {
        return List.of(DamageTypeRegistry.BIO_VIRUS);
    }
}
