package com.overcontrol1.biomechanica.tag;

import com.overcontrol1.biomechanica.Biomechanica;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class BiomechanicaTags {
    public static final TagKey<Item> BIOTECH_CORE = getItemTag(new Identifier(Biomechanica.MOD_ID, "biotech_core"));
    public static final TagKey<Item> BIOTECH_CORE_ATTACHABLE = getItemTag(new Identifier(Biomechanica.MOD_ID, "biotech_core_attachable"));

    private static TagKey<Item> getItemTag(Identifier id) {
        return TagKey.of(RegistryKeys.ITEM, id);
    }
}
