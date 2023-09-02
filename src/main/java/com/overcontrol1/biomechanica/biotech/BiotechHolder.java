package com.overcontrol1.biomechanica.biotech;

import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

import java.util.List;

public interface BiotechHolder {
    String NBT_ID = "Biotech";
    List<Biotech> getAttachedTech();
    default void addTech(Biotech biotech) {
        this.getAttachedTech().add(biotech);
    }

    default void writeBiotechToNbt(NbtCompound nbtCompound) {
        NbtList list = new NbtList();
        for (Biotech biotech : this.getAttachedTech()) {
            Identifier id = CustomRegistries.BIOTECH.getId(biotech);

            if (id != null) {
                list.add(NbtString.of(id.toString()));
            }
        }

        nbtCompound.put(NBT_ID, list);
    }

    static void fillFromNbt(NbtCompound modNbt, BiotechHolder holder) {
        NbtList list = modNbt.getList(NBT_ID, NbtElement.STRING_TYPE);
        if (list == null) {
            return;
        }

        for (NbtElement element : list) {
            Biotech biotech = CustomRegistries.BIOTECH.get(Identifier.tryParse(element.asString()));

            if (biotech != null) {
                holder.addTech(biotech);
            }
        }
    }
}
