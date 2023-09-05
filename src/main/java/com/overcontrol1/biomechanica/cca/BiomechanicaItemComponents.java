package com.overcontrol1.biomechanica.cca;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.cca.component.item.ItemCoreStorageComponent;
import com.overcontrol1.biomechanica.registry.ItemRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.util.Identifier;

@SuppressWarnings("UnstableApiUsage")
public class BiomechanicaItemComponents implements ItemComponentInitializer {
    public static final ComponentKey<ItemCoreStorageComponent> CORE_TYPE =
            ComponentRegistry.getOrCreate(new Identifier(Biomechanica.MOD_ID, "core_type"), ItemCoreStorageComponent.class);
    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(ItemRegistry.BIOTECH_EXOSKELETON, CORE_TYPE, ItemCoreStorageComponent::new);
        registry.register(ItemRegistry.BIOTECH_CORE, CORE_TYPE, ItemCoreStorageComponent::new);
    }
}
