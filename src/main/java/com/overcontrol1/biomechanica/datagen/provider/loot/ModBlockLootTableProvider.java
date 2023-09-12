package com.overcontrol1.biomechanica.datagen.provider.loot;

import com.overcontrol1.biomechanica.registry.BlockRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(BlockRegistry.BIOTECH_CRAFTING_STATION);
        addDrop(BlockRegistry.BIOTECH_CORE_INSERTER);
    }
}
