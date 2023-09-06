package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.item.util.BiomeIds;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import com.overcontrol1.biomechanica.util.TranslationUtils;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CoreTypeRegistry {
    public static final CoreType CUSTOS = registerCoreType("custos",
            new CoreType(TranslationUtils.getCoreTypeKey("custos"), 0xFFFFFF, BiomeIds.DEEP_DARK));
    public static final CoreType OCEANUS = registerCoreType("oceanus",
            new CoreType(TranslationUtils.getCoreTypeKey("oceanus"), 0xFFFFFF, BiomeIds.OCEAN));
    public static final CoreType CALOR = registerCoreType("calor",
            new CoreType(TranslationUtils.getCoreTypeKey("calor"), 0xFFFFFF, BiomeIds.DESERT));
    public static final CoreType IGNIS = registerCoreType("ignis",
            new CoreType(TranslationUtils.getCoreTypeKey("ignis"), 0xFFFFFF, BiomeIds.NETHER));
    public static final CoreType SALTU = registerCoreType("saltu",
            new CoreType(TranslationUtils.getCoreTypeKey("saltu"), 0xFFFFFF, BiomeIds.JUNGLE));

    public static void register() {
    }

    private static CoreType registerCoreType(String name, CoreType type) {
        return Registry.register(CustomRegistries.CORE_TYPES, new Identifier(Biomechanica.MOD_ID, name), type);
    }
}
