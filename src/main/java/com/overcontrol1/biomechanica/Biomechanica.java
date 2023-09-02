package com.overcontrol1.biomechanica;

import com.overcontrol1.biomechanica.registry.BiomechanicaRegistry;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Biomechanica implements ModInitializer {
    public static final String MOD_ID = "biomechanica";
    public static final Logger LOGGER = LoggerFactory.getLogger("Biomechanica");
    @Override
    public void onInitialize() {
        BiomechanicaRegistry.register();
    }
}
