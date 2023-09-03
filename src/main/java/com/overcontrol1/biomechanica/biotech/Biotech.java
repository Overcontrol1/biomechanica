package com.overcontrol1.biomechanica.biotech;

import com.overcontrol1.biomechanica.client.animation.BiotechAnimationState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.List;

public class Biotech {
    public Biotech() {

    }

    public BiotechAnimationState[] getAnimationTypes() {
        return new BiotechAnimationState[] {};
    }

    public EquipmentSlot getBodypart() {
        return EquipmentSlot.CHEST;
    }

    public List<RegistryKey<DamageType>> getInvulnerabilities() {
        return List.of();
    }

    public RenderLayer getRenderLayer(Identifier texture) {
        return RenderLayer.getArmorCutoutNoCull(texture);
    }
}
