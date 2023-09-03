package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.block.BiotechCraftingStationBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockRegistry {
    public static final Block BIOTECH_CRAFTING_STATION = registerBlockWithItem("biotech_crafting_station",
            new BiotechCraftingStationBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK)));

    public static void register() {
    }

    private static Block registerBlockWithItem(String name, Block block) {
        Identifier id = new Identifier(Biomechanica.MOD_ID, name);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new FabricItemSettings()));
        return Registry.register(Registries.BLOCK, id, block);
    }
}
