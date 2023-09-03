package com.overcontrol1.biomechanica.biotech;

import com.overcontrol1.biomechanica.client.animation.BiotechAnimationState;

import java.util.List;

public interface BiotechHolder {
    List<Biotech> getAttachedBiotech();
    default BiotechAnimationState getAnimationState() {
        return BiotechAnimationState.IDLE;
    }
}
