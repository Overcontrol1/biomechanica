package com.overcontrol1.biomechanica.client;

import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.client.model.DefaultedBiotechModel;
import com.overcontrol1.biomechanica.client.registry.ScreenHandlerRegistry;
import com.overcontrol1.biomechanica.client.renderer.BiotechRenderer;
import com.overcontrol1.biomechanica.client.screen.BiotechCraftingStationScreen;
import com.overcontrol1.biomechanica.network.ModMessages;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class BiomechanicaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMessages.registerS2C();

        for (Biotech biotech : CustomRegistries.BIOTECH.stream().toList()) {
            BiotechRenderer.addModel(biotech, new DefaultedBiotechModel<>(CustomRegistries.BIOTECH.getId(biotech)));
        }

        ScreenHandlerRegistry.register();
        HandledScreens.register(ScreenHandlerRegistry.BIOTECH_CRAFTING_STATION, BiotechCraftingStationScreen::new);
    }
}