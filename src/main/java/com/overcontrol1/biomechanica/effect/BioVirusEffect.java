package com.overcontrol1.biomechanica.effect;

import com.overcontrol1.biomechanica.registry.DamageTypeRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BioVirusEffect extends StatusEffect {
    public static final int DELAY = 30;
    public BioVirusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x005E13);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = DELAY >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.damage(entity.getDamageSources().create(DamageTypeRegistry.BIO_VIRUS), (amplifier + 1) * 0.5f);
    }
}
