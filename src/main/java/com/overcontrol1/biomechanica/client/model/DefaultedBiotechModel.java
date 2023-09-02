package com.overcontrol1.biomechanica.client.model;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class DefaultedBiotechModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {
    public DefaultedBiotechModel(Identifier assetSubpath) {
        super(assetSubpath);
    }

    @Override
    protected String subtype() {
        return "biotech";
    }
}
