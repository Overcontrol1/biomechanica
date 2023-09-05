package com.overcontrol1.biomechanica.registry;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.block.BiotechCoreInserterBlock;
import com.overcontrol1.biomechanica.block.entity.BiotechCoreInserterBlockEntity;
import com.overcontrol1.biomechanica.block.entity.BiotechCraftingStationBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntityRegistry {
    public static final BlockEntityType<BiotechCraftingStationBlockEntity> BIOTECH_CRAFTING_STATION = registerBlockEntity("biotech_crafting_station",
            FabricBlockEntityTypeBuilder.create(BiotechCraftingStationBlockEntity::new, BlockRegistry.BIOTECH_CRAFTING_STATION).build());

    public static final BlockEntityType<BiotechCoreInserterBlockEntity> BIOTECH_CORE_INSERTER = registerBlockEntity("biotech_core_inserter",
            FabricBlockEntityTypeBuilder.create(BiotechCoreInserterBlockEntity::new, BlockRegistry.BIOTECH_CORE_INSERTER).build());
    public static void register() {

    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType<T> blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Biomechanica.MOD_ID, name), blockEntityType);
    }
}
