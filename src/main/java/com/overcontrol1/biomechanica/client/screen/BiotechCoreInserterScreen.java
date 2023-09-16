package com.overcontrol1.biomechanica.client.screen;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.client.util.ScreenUtil;
import com.overcontrol1.biomechanica.screen.BiotechCoreInserterScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BiotechCoreInserterScreen extends HandledScreen<BiotechCoreInserterScreenHandler> {
    private static final Identifier BACKGROUND_TEXTURE = ScreenUtil.getGuiTexture("biotech_core_infuser");
    private static final Identifier EMPTY_FUEL_OVERLAY = ScreenUtil.getGuiTexture("empty_fuel_overlay");

    public BiotechCoreInserterScreen(BiotechCoreInserterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void renderProgressArrow(DrawContext context, int x, int y) {
        if (this.handler.isCrafting()) {
            context.drawTexture(BACKGROUND_TEXTURE, x + 99, y + 33, 176, 0, 8, this.handler.getScaledProgress());
        }
    }

    protected void renderEmptyFuelOverlay(DrawContext context, int x, int y) {
        if (this.handler.hasEmptyFuelSlot()) {
            context.drawTexture(EMPTY_FUEL_OVERLAY, x + 114, y + 39, 0, 0, 13, 13, 13, 13);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        renderProgressArrow(context, x, y);
        renderEmptyFuelOverlay(context, x, y);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
