package com.overcontrol1.biomechanica.biotech;

import com.overcontrol1.biomechanica.client.animation.BiotechAnimationState;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Obsolete
public interface BiotechHolder {
    List<Biotech> getAttachedBiotech();
    default BiotechAnimationState getAnimationState() {
        return BiotechAnimationState.IDLE;
    }
}
