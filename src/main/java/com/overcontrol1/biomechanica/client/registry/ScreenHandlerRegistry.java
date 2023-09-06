package com.overcontrol1.biomechanica.client.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.client.screen.BiotechCoreInserterScreenHandler;
import com.overcontrol1.biomechanica.client.screen.BiotechCraftingStationScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlerRegistry {
    public static ScreenHandlerType<BiotechCraftingStationScreenHandler> BIOTECH_CRAFTING_STATION;
    public static ScreenHandlerType<BiotechCoreInserterScreenHandler> BIOTECH_CORE_INSERTER;

    public static void register() {
        BIOTECH_CRAFTING_STATION = new ScreenHandlerType<>(BiotechCraftingStationScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
        BIOTECH_CORE_INSERTER = new ScreenHandlerType<>(BiotechCoreInserterScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
    }

    private static ScreenHandlerType<?> registerScreenHandler(String name, ScreenHandlerType<?> handler) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(Biomechanica.MOD_ID, name), handler);
    }
}
