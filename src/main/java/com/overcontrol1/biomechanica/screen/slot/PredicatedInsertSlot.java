package com.overcontrol1.biomechanica.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.function.Predicate;

public class PredicatedInsertSlot extends Slot {
    private final Predicate<ItemStack> predicate;
    public PredicatedInsertSlot(Inventory inventory, int index, int x, int y, Predicate<ItemStack> predicate) {
        super(inventory, index, x, y);
        this.predicate = predicate;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return this.predicate.test(stack);
    }
}
