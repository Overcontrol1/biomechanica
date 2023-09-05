package com.overcontrol1.biomechanica.cca.component.item;

import com.overcontrol1.biomechanica.cca.component.CoreStorageComponent;
import com.overcontrol1.biomechanica.item.util.CoreType;
import com.overcontrol1.biomechanica.registry.CoreTypeRegistry;
import com.overcontrol1.biomechanica.registry.custom.CustomRegistries;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class ItemCoreStorageComponent extends ItemComponent implements CoreStorageComponent {
    public ItemCoreStorageComponent(ItemStack stack) {
        super(stack);
    }

    @Override
    public CoreType getCoreType() {
        if (this.hasTag("CoreType")) this.setCoreType(CoreTypeRegistry.CUSTOS);
        return CustomRegistries.CORE_TYPES.get(Identifier.tryParse(this.getString("CoreType")));
    }

    @Override
    public void setCoreType(CoreType type) {
        this.putString("CoreType", Objects.requireNonNull(CustomRegistries.CORE_TYPES.getId(type)).toString());
    }
}
