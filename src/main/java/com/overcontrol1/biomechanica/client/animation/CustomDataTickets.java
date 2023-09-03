package com.overcontrol1.biomechanica.client.animation;

import com.overcontrol1.biomechanica.biotech.Biotech;
import software.bernie.geckolib.core.object.DataTicket;

public class CustomDataTickets {
    public static final DataTicket<Biotech> BIOTECH = new DataTicket<>("biotech", Biotech.class);
    public static final DataTicket<BiotechAnimationState> BIOTECH_ANIMATION_STATE = new DataTicket<>("biotech_animation_state", BiotechAnimationState.class);
}
