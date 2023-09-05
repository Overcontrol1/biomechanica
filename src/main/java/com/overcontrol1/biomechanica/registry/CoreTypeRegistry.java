package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import com.overcontrol1.biomechanica.util.TranslationUtils;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CoreTypeRegistry {
    public static final CoreType CUSTOS = registerCoreType("custos", new CoreType(TranslationUtils.getCoreTypeKey("custos"), 0xFFFFFF));

    public static void register() {
    }

    private static CoreType registerCoreType(String name, CoreType type) {
        return Registry.register(CustomRegistries.CORE_TYPES, new Identifier(Biomechanica.MOD_ID, name), type);
    }
}
