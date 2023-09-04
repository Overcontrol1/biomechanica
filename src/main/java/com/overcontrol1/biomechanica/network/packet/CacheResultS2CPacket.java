package com.overcontrol1.biomechanica.network.packet;

import com.overcontrol1.biomechanica.block.entity.BiotechCraftingStationBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CacheResultS2CPacket {

    public static void receive(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler,
                               PacketByteBuf packetByteBuf, PacketSender packetSender) {
        BlockPos pos = packetByteBuf.readBlockPos();
        ItemStack result = packetByteBuf.readItemStack();
        World world = minecraftClient.world;

        if (world != null) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BiotechCraftingStationBlockEntity biotechEntity) {
                biotechEntity.setCachedResult(result);
            }
        }
    }
}
