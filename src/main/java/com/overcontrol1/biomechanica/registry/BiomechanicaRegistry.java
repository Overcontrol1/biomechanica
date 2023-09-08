package com.overcontrol1.biomechanica.registry;

public class BiomechanicaRegistry {
    public static void register() {
        ItemRegistry.register();
        BlockRegistry.register();
        BlockEntityRegistry.register();
        BiotechRegistry.register();
        EffectRegistry.register();
        RecipeRegistry.register();
        ItemGroupRegistry.register();
        CoreTypeRegistry.register();
        ScreenHandlerRegistry.register();
    }
}
