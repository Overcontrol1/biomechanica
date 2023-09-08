package com.overcontrol1.biomechanica.block.entity;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.recipe.BiotechCraftingStationShapedRecipe;
import com.overcontrol1.biomechanica.registry.BlockEntityRegistry;
import com.overcontrol1.biomechanica.registry.RecipeRegistry;
import com.overcontrol1.biomechanica.screen.BiotechCraftingStationScreenHandler;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BiotechCraftingStationBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, RecipeInputInventory {
    public static final int CONTAINER_SIZE = 12;
    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private final Map<PlayerEntity, ScreenHandler> handlerMap = new Object2ObjectArrayMap<>();
    private ItemStack cachedResult = ItemStack.EMPTY;
    public BiotechCraftingStationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BIOTECH_CRAFTING_STATION, pos, state);
    }

    public ItemStack getCachedResult() {
        return this.cachedResult;
    }

    public void setCachedResult(ItemStack cachedResult) {
        this.cachedResult = cachedResult;
    }

    public void setHandler(PlayerEntity player, ScreenHandler handler) {
        this.handlerMap.put(player, handler);
    }

    public void clearHandler() {
        this.handlerMap.clear();
    }

    public void tryCacheResult() {
        if (this.world == null || this.isEmpty()) {
            this.cachedResult = ItemStack.EMPTY;
            return;
        }
        if (this.world.isClient) {
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<BiotechCraftingStationShapedRecipe> optional =
                    world.getRecipeManager().getFirstMatch(RecipeRegistry.Types.BIOTECH_CRAFTING, this, world);
            if (optional.isPresent()) {
                BiotechCraftingStationShapedRecipe craftingRecipe = optional.get();
                ItemStack recipeResult = craftingRecipe.craft(this, world.getRegistryManager());
                if (recipeResult.isItemEnabled(world.getEnabledFeatures())) {
                    itemStack = recipeResult;
                }
            }

            this.cachedResult = itemStack;
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.stacks);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.stacks);
        tryCacheResult();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("blockEntity." + Biomechanica.MOD_ID + ".biotech_crafting_station");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BiotechCraftingStationScreenHandler(syncId, playerInventory, this, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 4;
    }

    @Override
    public List<ItemStack> getInputStacks() {
        return this.stacks;
    }

    @Override
    public int size() {
        return CONTAINER_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return this.stacks.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.stacks.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack toReturn = Inventories.splitStack(this.stacks, slot, amount);
        if (!toReturn.isEmpty() && !this.handlerMap.isEmpty()) {
            this.handlerMap.forEach((player, handler) -> handler.onContentChanged(this));
        }
        return toReturn;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack removed = Inventories.removeStack(stacks, slot);
        if (!removed.isEmpty() && !this.handlerMap.isEmpty()) {
            this.handlerMap.forEach((player, handler) -> handler.onContentChanged(this));
        }
        return removed;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.stacks.set(slot, stack);
        if (!this.handlerMap.isEmpty()) {
            this.handlerMap.forEach((player, handler) -> handler.onContentChanged(this));
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        this.stacks.forEach(finder::addInput);
    }

    @Override
    public void clear() {
        this.stacks.clear();
    }
}
