package com.overcontrol1.biomechanica.item.util;

import com.overcontrol1.biomechanica.item.additions.DynamicModelItem;
import net.minecraft.item.Item;

public abstract class DefaultDynamicModelItem extends Item implements DynamicModelItem {
    public DefaultDynamicModelItem(Settings settings) {
        super(settings);
    }
}
