package com.overcontrol1.biomechanica.cca.component;

import com.overcontrol1.biomechanica.biotech.Biotech;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;

public class BiotechListComponent implements Component, AutoSyncedComponent {
    public static final String NBT_ID = "Biotech";
    private List<Biotech> value = new ObjectArrayList<>();
    public List<Biotech> get() {
        return this.value;
    }

    public void set(List<Biotech> list) {
        this.value = list;
    }

    public boolean add(Biotech biotech) {
        return this.get().add(biotech);
    }

    public boolean remove(Biotech biotech) {
        return this.get().remove(biotech);
    }

    public void clear() {
        this.get().clear();
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtList list = tag.getList(NBT_ID, NbtElement.STRING_TYPE);

        for (NbtElement element : list) {
            this.add(CustomRegistries.BIOTECH.get(Identifier.tryParse(element.asString())));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList list = new NbtList();
        for (Biotech biotech : this.get()) {
            list.add(NbtString.of(Objects.requireNonNull(
                    CustomRegistries.BIOTECH.getId(biotech), "Unregistered Biotech").toString()));
        }

        tag.put(NBT_ID, list);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeCollection(this.get(), (packetByteBuf, biotech) ->
                packetByteBuf.writeIdentifier(CustomRegistries.BIOTECH.getId(biotech)));
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.set(buf.readCollection(ObjectArrayList::new, packetByteBuf ->
            CustomRegistries.BIOTECH.get(packetByteBuf.readIdentifier())
        ));
    }
}
