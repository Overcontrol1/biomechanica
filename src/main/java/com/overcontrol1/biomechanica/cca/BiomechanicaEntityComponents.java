package com.overcontrol1.biomechanica.cca;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.cca.component.BiotechListComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class BiomechanicaEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<BiotechListComponent> ATTACHED_BIOTECH =
            ComponentRegistry.getOrCreate(new Identifier(Biomechanica.MOD_ID, "attached_biotech"), BiotechListComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PlayerEntity.class, ATTACHED_BIOTECH, player -> new BiotechListComponent());
    }
}
