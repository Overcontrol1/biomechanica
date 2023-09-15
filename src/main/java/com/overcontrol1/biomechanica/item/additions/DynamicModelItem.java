package com.overcontrol1.biomechanica.item.additions;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface DynamicModelItem {
    Identifier getDynamicModel(ItemStack stack);
}
