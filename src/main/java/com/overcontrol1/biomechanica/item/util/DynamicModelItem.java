package com.overcontrol1.biomechanica.item.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface DynamicModelItem {
    Identifier getDynamicModel(ItemStack stack);
}
