package com.overcontrol1.biomechanica.screen;

import com.overcontrol1.biomechanica.block.entity.BiotechCoreInserterBlockEntity;
import com.overcontrol1.biomechanica.client.util.ScreenUtil;
import com.overcontrol1.biomechanica.registry.ScreenHandlerRegistry;
import com.overcontrol1.biomechanica.screen.slot.GenericResultSlot;
import com.overcontrol1.biomechanica.screen.slot.PredicatedInsertSlot;
import com.overcontrol1.biomechanica.tag.BiomechanicaTags;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class BiotechCoreInserterScreenHandler extends GenericScreenHandler {
    private final PropertyDelegate delegate;
    public BiotechCoreInserterScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(BiotechCoreInserterBlockEntity.CONTAINER_SIZE),
                ScreenHandlerContext.EMPTY, new ArrayPropertyDelegate(2));
    }

    public BiotechCoreInserterScreenHandler(int syncId, PlayerInventory playerInventory,
                                            Inventory inventory, ScreenHandlerContext context, PropertyDelegate delegate) {
        super(ScreenHandlerRegistry.BIOTECH_CORE_INSERTER, syncId, playerInventory, inventory, context);

        checkSize(inventory, BiotechCoreInserterBlockEntity.CONTAINER_SIZE);

        inventory.onOpen(this.player);

        this.delegate = delegate;

        this.addSlot(new GenericResultSlot(inventory, 3, 80, 60)); // RESULT

        this.addSlot(new PredicatedInsertSlot(inventory, 0, 80, 15, stack -> stack.isIn(BiomechanicaTags.BIOTECH_CORE_ATTACHABLE))); // TOP INPUT
        this.addSlot(new PredicatedInsertSlot(inventory, 1, 54, 38, stack -> stack.isIn(BiomechanicaTags.BIOTECH_CORE))); // CORE INPUT
        this.addSlot(new Slot(inventory, 2, 112, 37)); // FUEL INPUT

        this.addSlots(ScreenUtil.createPlayerInventorySlots(playerInventory, 8, 86+9));
        this.addSlots(ScreenUtil.createPlayerHotbarSlots(playerInventory, 8, 144+9));

        addProperties(this.delegate);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.getIndex() != 3;
    }

    public boolean isCrafting() {
        return this.delegate.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.delegate.get(0);
        int maxProgress = this.delegate.get(1);
        int arrowSize = 26;
        return maxProgress != 0 && progress != 0 ? progress * arrowSize / maxProgress : 0;
    }

    public boolean hasEmptyFuelSlot() {
        return this.inventory.getStack(2).isEmpty();
    }
}
