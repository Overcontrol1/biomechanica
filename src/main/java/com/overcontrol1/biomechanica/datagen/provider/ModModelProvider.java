package com.overcontrol1.biomechanica.datagen.provider;

import com.overcontrol1.biomechanica.registry.BlockRegistry;
import com.overcontrol1.biomechanica.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.item.Items;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerParented(Blocks.IRON_BLOCK, BlockRegistry.BIOTECH_CRAFTING_STATION);
        blockStateModelGenerator.registerParented(Blocks.QUARTZ_BLOCK, BlockRegistry.BIOTECH_CORE_INSERTER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemRegistry.BIOTECH_EXOSKELETON, Models.GENERATED);
        itemModelGenerator.register(ItemRegistry.BIOTECH_ANIMATABLE, Items.BARRIER, Models.GENERATED);
    }
}
