package com.overcontrol1.biomechanica.block.entity;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.cca.BiomechanicaItemComponents;
import com.overcontrol1.biomechanica.cca.component.item.ItemCoreStorageComponent;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.registry.BlockEntityRegistry;
import com.overcontrol1.biomechanica.screen.BiotechCoreInserterScreenHandler;
import com.overcontrol1.biomechanica.tag.BiomechanicaTags;
import com.overcontrol1.biomechanica.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BiotechCoreInserterBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    public static final int CONTAINER_SIZE = 4;

    private static final int BIOTECH_INPUT_SLOT = 0;
    private static final int CORE_INPUT_SLOT = 1;
    private static final int FUEL_INPUT_SLOT = 2;
    private static final int RESULT_SLOT = 3;
    private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(CONTAINER_SIZE, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;
    public BiotechCoreInserterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BIOTECH_CORE_INSERTER, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BiotechCoreInserterBlockEntity.this.progress;
                    case 1 -> BiotechCoreInserterBlockEntity.this.maxProgress;

                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BiotechCoreInserterBlockEntity.this.progress = value;
                    case 1 -> BiotechCoreInserterBlockEntity.this.maxProgress = value;
                };
            }

            @Override
            public int size() {
                return 2;
            }
        };
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
        Inventories.writeNbt(nbt, this.getItems());
        nbt.putInt("Progress", this.progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.getItems());
        this.progress = nbt.getInt("Progress");
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("blockEntity." + Biomechanica.MOD_ID + ".biotech_core_inserter");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BiotechCoreInserterScreenHandler(syncId, playerInventory, this, ScreenHandlerContext.create(world, pos), this.propertyDelegate);
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState,
                            BiotechCoreInserterBlockEntity blockEntity) {
        if (world.isClient) {
            return;
        }

        if (hasValidRecipe(blockEntity)) {
            blockEntity.progress++;
            markDirty(world, blockPos, blockState);

            if (blockEntity.progress >= blockEntity.maxProgress) {
                craftItem(blockEntity);
                System.out.println("crafting item at " + blockEntity.progress);
            }
        } else {
            if (blockEntity.progress > 0) {
                blockEntity.resetProgress();
                System.out.println("resetting progress");
                markDirty(world, blockPos, blockState);
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(BiotechCoreInserterBlockEntity blockEntity) {
        ItemStack biotech = blockEntity.getStack(BIOTECH_INPUT_SLOT);
        ItemStack core = blockEntity.getStack(CORE_INPUT_SLOT);

        CoreType coreCoreType = BiomechanicaItemComponents.CORE_TYPE.get(core).getCoreType();

        if (coreCoreType != null) {
            BiomechanicaItemComponents.CORE_TYPE.get(biotech).setCoreType(coreCoreType);

            core.setCount(0);

            blockEntity.setStack(BIOTECH_INPUT_SLOT, ItemStack.EMPTY);
            blockEntity.setStack(RESULT_SLOT, biotech);
        }
    }

    private static boolean hasValidRecipe(BiotechCoreInserterBlockEntity blockEntity) {
        ItemStack input = blockEntity.getStack(BIOTECH_INPUT_SLOT);
        ItemStack core = blockEntity.getStack(CORE_INPUT_SLOT);

        boolean hasBiotechInBiotechSlot = input.isIn(BiomechanicaTags.BIOTECH_CORE_ATTACHABLE);
        boolean hasCoreInCoreSlot = core.isIn(BiomechanicaTags.BIOTECH_CORE);

        Optional<ItemCoreStorageComponent> inputCoreType = BiomechanicaItemComponents.CORE_TYPE.maybeGet(input);
        Optional<ItemCoreStorageComponent> coreCoreType = BiomechanicaItemComponents.CORE_TYPE.maybeGet(core);

        boolean differentCores = false;

        if (inputCoreType.isPresent() && coreCoreType.isPresent()) {
            differentCores = inputCoreType.get().getCoreType() != coreCoreType.get().getCoreType()
                    && coreCoreType.get().getCoreType() != null;
        }

        return hasCoreInCoreSlot && hasBiotechInBiotechSlot && differentCores &&
                canInsertItemstackIntoOutputSlot(blockEntity, blockEntity.getStack(BIOTECH_INPUT_SLOT));
    }

    private static boolean canInsertItemstackIntoOutputSlot(BiotechCoreInserterBlockEntity blockEntity, ItemStack stack) {
        boolean isValidItem = blockEntity.getStack(RESULT_SLOT).getItem() == stack.getItem() || blockEntity.getStack(RESULT_SLOT).isEmpty();

        return blockEntity.canInsert(RESULT_SLOT, stack, null) && isValidItem && canInsertAmountIntoOutputSlot(blockEntity, stack.getCount());
    }

    private static boolean canInsertAmountIntoOutputSlot(BiotechCoreInserterBlockEntity blockEntity, int count) {
        return blockEntity.getStack(RESULT_SLOT).getMaxCount() > blockEntity.getStack(RESULT_SLOT).getCount() + count;
    }
}
