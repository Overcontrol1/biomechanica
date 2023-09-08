package com.overcontrol1.biomechanica.client.renderer.item;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.cca.BiomechanicaItemComponents;
import com.overcontrol1.biomechanica.cca.component.item.ItemCoreStorageComponent;
import com.overcontrol1.biomechanica.item.BiotechExoskeletonItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Optional;

public class BiotechExoskeletonRenderer extends GeoArmorRenderer<BiotechExoskeletonItem> {
    private static final Identifier BASE_TEXTURE_PATH = new Identifier(Biomechanica.MOD_ID, "textures/armor/biotech_exoskeleton/");
    public BiotechExoskeletonRenderer() {
        super(new DefaultedItemGeoModel<>(new Identifier(Biomechanica.MOD_ID, "biotech_exoskeleton")));
    }

    @Override
    public Identifier getTextureLocation(BiotechExoskeletonItem animatable) {
        Optional<ItemCoreStorageComponent> coreType = BiomechanicaItemComponents.CORE_TYPE.maybeGet(this.currentStack);

        if (coreType.isPresent()) {
            if (coreType.get().getCoreType() != null) {
                return BASE_TEXTURE_PATH.withSuffixedPath(coreType.get().getCoreType().biomeId() + ".png");
            }
        }

        return BASE_TEXTURE_PATH.withSuffixedPath("default.png");
    }
}