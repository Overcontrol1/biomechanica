package com.overcontrol1.biomechanica.client.util;

import com.overcontrol1.biomechanica.Biomechanica;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ScreenUtil {
    public static Identifier getGuiTexture(String id) {
        return new Identifier(Biomechanica.MOD_ID, "textures/gui/" + id + ".png");
    }

    public static Slot[] createPlayerInventorySlots(PlayerInventory playerInventory, int topLeftX, int topLeftY) {
        List<Slot> slots = new ObjectArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                slots.add(new Slot(playerInventory, j + i * 9 + 9, topLeftX + j * 18, topLeftY + i * 18-9));
            }
        }
        return slots.toArray(Slot[]::new);
    }

    public static Slot[] createPlayerHotbarSlots(PlayerInventory inventory, int topLeftX, int topLeftY) {
        List<Slot> slots = new ObjectArrayList<>();
        for (int i = 0; i < 9; i++) {
            slots.add(new Slot(inventory, i, topLeftX + i * 18, topLeftY-9));
        }
        return slots.toArray(Slot[]::new);
    }

    public static <T extends RecipeType<R>, R extends Recipe<I>, I extends Inventory> void updateResult(ScreenHandler handler, World world,
                                                                                                        ServerPlayerEntity player,
                                                                                                        I craftingInventory, CraftingResultInventory resultInventory,
                                                                                                        T recipeType, TriConsumer<ItemStack, BlockEntity, World> onResultChanged) {
        if (!world.isClient) {
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<R> optional = Objects.requireNonNull(world.getServer()).getRecipeManager().getFirstMatch(recipeType, craftingInventory, world);
            if (optional.isPresent()) {
                R craftingRecipe = optional.get();
                if (resultInventory.shouldCraftRecipe(world, player, craftingRecipe)) {
                    ItemStack itemStack2 = craftingRecipe.craft(craftingInventory, world.getRegistryManager());
                    if (itemStack2.isItemEnabled(world.getEnabledFeatures())) {
                        itemStack = itemStack2;
                    }
                }
            }

            ItemStack previousStack = resultInventory.getStack(0);
            resultInventory.setStack(0, itemStack);
            handler.setPreviousTrackedSlot(0, itemStack);
            player.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 0, itemStack));
            if (itemStack != previousStack) {
                if (craftingInventory instanceof BlockEntity) {
                    onResultChanged.accept(itemStack, (BlockEntity) craftingInventory, world);
                }
            }
        }
    }
}
