package com.overcontrol1.biomechanica.client.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;

public class HandlerAttachedCraftingResultSlot extends CraftingResultSlot {
    private final ScreenHandler handler;
    private final RecipeInputInventory input;
    public HandlerAttachedCraftingResultSlot(PlayerEntity player, RecipeInputInventory input, Inventory inventory, int index, int x, int y, ScreenHandler handler) {
        super(player, input, inventory, index, x, y);
        this.handler = handler;
        this.input = input;
    }

    @Override
    protected void onCrafted(ItemStack stack) {
        handler.onContentChanged(this.input);
        super.onCrafted(stack);
    }
}
