package com.overcontrol1.biomechanica.client.util;

import com.overcontrol1.biomechanica.Biomechanica;
import net.minecraft.util.Identifier;

public class ScreenUtil {
    public static Identifier getGuiTexture(String id) {
        return new Identifier(Biomechanica.MOD_ID, "textures/gui/" + id + ".png");
    }
}
