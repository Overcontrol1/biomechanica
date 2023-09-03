package com.overcontrol1.biomechanica.biotech.custom;

import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.client.animation.BiotechAnimationState;

public class BackClawBiotech extends Biotech {
    private static final BiotechAnimationState[] animationTypes = new BiotechAnimationState[] {BiotechAnimationState.IDLE};
    @Override
    public BiotechAnimationState[] getAnimationTypes() {
        return animationTypes;
    }
}
