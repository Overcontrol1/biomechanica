package com.overcontrol1.biomechanica.client.screen;

import com.overcontrol1.biomechanica.block.entity.BiotechCraftingStationBlockEntity;
import com.overcontrol1.biomechanica.client.registry.ScreenHandlerRegistry;
import com.overcontrol1.biomechanica.recipe.BiotechCraftingStationShapedRecipe;
import com.overcontrol1.biomechanica.registry.RecipeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class BiotechCraftingStationScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PlayerEntity player;
    private final CraftingResultInventory resultInventory;
    private final ScreenHandlerContext context;

    // Client
    public BiotechCraftingStationScreenHandler(int syncId, PlayerInventory inventory) {
        super(ScreenHandlerRegistry.BIOTECH_CRAFTING_STATION, syncId);
        checkSize(inventory, BiotechCraftingStationBlockEntity.CONTAINER_SIZE);
        this.context = ScreenHandlerContext.EMPTY;
        this.player = inventory.player;
        this.inventory = new CraftingInventory(this, 3, 4);
        this.resultInventory = new CraftingResultInventory();

        this.addSlot(new CraftingResultSlot(inventory.player, (RecipeInputInventory) this.inventory, this.resultInventory, 0, 124, 43-8));
        System.out.println(this.getSlot(0).x);
        inventory.onOpen(inventory.player);

        for (int i = 0; i < 4; i++) { // Y
            for (int j = 0; j < 3; j++) { // X
                this.addSlot(new Slot(this.inventory, j + i * 3, 30 + j * 18, 17 + i * 18-9));
            }
        }

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    // Server
    public BiotechCraftingStationScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerRegistry.BIOTECH_CRAFTING_STATION, syncId);
        checkSize(inventory, BiotechCraftingStationBlockEntity.CONTAINER_SIZE);
        this.context = context;
        this.player = playerInventory.player;
        this.inventory = inventory;
        this.resultInventory = new CraftingResultInventory();
        this.addSlot(new CraftingResultSlot(playerInventory.player, (RecipeInputInventory) inventory, this.resultInventory, 0, 120, 140));
        ((BiotechCraftingStationBlockEntity) inventory).setHandler(this);
        System.out.println(this.getSlot(0));
        inventory.onOpen(playerInventory.player);

        for (int i = 0; i < 4; i++) { // Y
            for (int j = 0; j < 3; j++) { // X
                this.addSlot(new Slot(inventory, j + i * 3, 30 + j * 18, 17 + i * 18-9));
            }
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<BiotechCraftingStationShapedRecipe> optional = Objects.requireNonNull(world.getServer()).getRecipeManager().getFirstMatch(RecipeRegistry.Types.BIOTECH_CRAFTING, craftingInventory, world);
            if (optional.isPresent()) {
                BiotechCraftingStationShapedRecipe craftingRecipe = optional.get();
                if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
                    ItemStack itemStack2 = craftingRecipe.craft(craftingInventory, world.getRegistryManager());
                    if (itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                        itemStack = itemStack2;
                    }
                }
            }

            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, blockPos) ->
                updateResult(this, world, this.player, (RecipeInputInventory) this.inventory, this.resultInventory));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (slotIndex < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory inventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18-9));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory inventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 160-9));
        }
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.resultInventory && super.canInsertIntoSlot(stack, slot);
    }
}
