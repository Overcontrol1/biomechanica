package com.overcontrol1.biomechanica.network;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.network.packet.CacheResultS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier CACHE_RESULT_S2C = new Identifier(Biomechanica.MOD_ID, "cache_result");
    public static void registerC2S() {

    }

    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(CACHE_RESULT_S2C, CacheResultS2CPacket::receive);
    }
}
