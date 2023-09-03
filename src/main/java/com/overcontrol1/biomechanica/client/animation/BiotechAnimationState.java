package com.overcontrol1.biomechanica.client.animation;

public enum BiotechAnimationState {
    EQUIP("equip"), IDLE("idle"), ACTIVATE("activate"), UNEQUIP("unequip");

    public final String animationName;
    BiotechAnimationState(String animationName) {
        this.animationName = animationName;
    }
}
