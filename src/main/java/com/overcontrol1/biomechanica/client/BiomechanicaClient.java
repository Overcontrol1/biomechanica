package com.overcontrol1.biomechanica.client;

import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.client.model.DefaultedBiotechModel;
import com.overcontrol1.biomechanica.client.renderer.BiotechRenderer;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.fabricmc.api.ClientModInitializer;

public class BiomechanicaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Biotech biotech : CustomRegistries.BIOTECH.stream().toList()) {
            BiotechRenderer.addModel(biotech, new DefaultedBiotechModel<>(CustomRegistries.BIOTECH.getId(biotech)));
        }
    }
}
