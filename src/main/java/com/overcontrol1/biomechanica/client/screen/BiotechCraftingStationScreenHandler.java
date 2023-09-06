package com.overcontrol1.biomechanica.client.screen;

import com.overcontrol1.biomechanica.block.entity.BiotechCraftingStationBlockEntity;
import com.overcontrol1.biomechanica.client.registry.ScreenHandlerRegistry;
import com.overcontrol1.biomechanica.client.slot.HandlerAttachedCraftingResultSlot;
import com.overcontrol1.biomechanica.network.ModMessages;
import com.overcontrol1.biomechanica.recipe.BiotechCraftingStationShapedRecipe;
import com.overcontrol1.biomechanica.registry.RecipeRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class BiotechCraftingStationScreenHandler extends GenericScreenHandler {
    private CraftingResultInventory resultInventory;

    // Client
    public BiotechCraftingStationScreenHandler(int syncId, PlayerInventory inventory) {
        super(ScreenHandlerRegistry.BIOTECH_CRAFTING_STATION, syncId, inventory, null, ScreenHandlerContext.EMPTY);

        this.inventory = new CraftingInventory(this, 3, 4);

        init();
    }

    // Server
    public BiotechCraftingStationScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScreenHandlerContext context) {
        super(ScreenHandlerRegistry.BIOTECH_CRAFTING_STATION, syncId, playerInventory, inventory, context);

        init();

        if (!inventory.isEmpty()) {
            updateResult(this, this.player.getWorld(), this.player,
                    (RecipeInputInventory) this.inventory, this.resultInventory);
        }
    }

    protected void init() {
        checkSize(inventory, BiotechCraftingStationBlockEntity.CONTAINER_SIZE);

        this.resultInventory = new CraftingResultInventory();

        this.addSlot(new HandlerAttachedCraftingResultSlot(this.player, (RecipeInputInventory) inventory,
                this.resultInventory, 0, 124, 35, this));

        if (this.inventory instanceof BiotechCraftingStationBlockEntity) {
            ((BiotechCraftingStationBlockEntity) inventory).setHandler(this.player, this);
        }
        inventory.onOpen(this.player);

        for (int i = 0; i < 4; i++) { // Y
            for (int j = 0; j < 3; j++) { // X
                this.addSlot(new Slot(inventory, j + i * 3, 30 + j * 18, 17 + i * 18-9));
            }
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player,
                                       RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
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

            ItemStack previousStack = resultInventory.getStack(0);
            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
            if (itemStack != previousStack) {
                ((BiotechCraftingStationScreenHandler) handler).sendCachePacket(itemStack, (BlockEntity) craftingInventory, world);
            }
        }
    }

    private void sendCachePacket(ItemStack stackToCache, BlockEntity blockEntity, World world) {
        if (!world.isClient) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(blockEntity.getPos());
            buf.writeItemStack(stackToCache);
            for (ServerPlayerEntity playerEntity : PlayerLookup.tracking(blockEntity)) {
                ServerPlayNetworking.send(playerEntity, ModMessages.CACHE_RESULT_S2C, PacketByteBufs.copy(buf));
            }
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, blockPos) ->
                updateResult(this, world, this.player, (RecipeInputInventory) this.inventory, this.resultInventory));
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
