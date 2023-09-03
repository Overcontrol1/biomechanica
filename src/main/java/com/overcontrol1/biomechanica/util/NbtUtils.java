package com.overcontrol1.biomechanica.util;

import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class NbtUtils {
    public static final String MOD_NBT_ID = "Biomechanica";

    public static void writeModNbt(NbtCompound parent, NbtCompound child) {
        parent.put(MOD_NBT_ID, child);
    }

    public static Optional<NbtCompound> readModNbt(NbtCompound from) {
        return Optional.ofNullable((NbtCompound) from.get(MOD_NBT_ID));
    }
}
