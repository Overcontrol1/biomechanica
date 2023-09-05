package com.overcontrol1.biomechanica.client.screen;

import com.overcontrol1.biomechanica.block.entity.BiotechCoreInserterBlockEntity;
import com.overcontrol1.biomechanica.client.registry.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class BiotechCoreInserterScreenHandler extends GenericScreenHandler {
    public BiotechCoreInserterScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(BiotechCoreInserterBlockEntity.CONTAINER_SIZE), ScreenHandlerContext.EMPTY);
    }

    public BiotechCoreInserterScreenHandler(int syncId, PlayerInventory playerInventory,
                                            Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerRegistry.BIOTECH_CORE_INSERTER, syncId, playerInventory, inventory, context);
    }
}
