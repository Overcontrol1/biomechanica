package com.overcontrol1.biomechanica.biotech;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;

import java.util.List;

public class Biotech {
    public Biotech() {

    }
    public EquipmentSlot getBodypart() {
        return EquipmentSlot.CHEST;
    }

    public List<RegistryKey<DamageType>> getInvulnerabilities() {
        return List.of();
    }
}
