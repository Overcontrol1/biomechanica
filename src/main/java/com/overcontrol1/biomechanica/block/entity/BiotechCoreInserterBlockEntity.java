package com.overcontrol1.biomechanica.block.entity;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.client.screen.BiotechCraftingStationScreenHandler;
import com.overcontrol1.biomechanica.recipe.BiotechCraftingStationShapedRecipe;
import com.overcontrol1.biomechanica.registry.BlockEntityRegistry;
import com.overcontrol1.biomechanica.registry.RecipeRegistry;
import com.overcontrol1.biomechanica.util.ImplementedInventory;
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

public class BiotechCoreInserterBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    public static final int CONTAINER_SIZE = 3;
    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(CONTAINER_SIZE, ItemStack.EMPTY);
    public BiotechCoreInserterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BIOTECH_CORE_INSERTER, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.stacks;
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
}
