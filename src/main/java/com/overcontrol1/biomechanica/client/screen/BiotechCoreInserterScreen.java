package com.overcontrol1.biomechanica.client.screen;

import com.overcontrol1.biomechanica.Biomechanica;
import com.overcontrol1.biomechanica.screen.BiotechCoreInserterScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BiotechCoreInserterScreen extends HandledScreen<BiotechCoreInserterScreenHandler> {
    private static final Identifier BACKGROUND_TEXTURE = new Identifier(Biomechanica.MOD_ID, "textures/gui/biotech_core_infuser.png");
    private static final Identifier EMPTY_FUEL_OVERLAY = new Identifier(Biomechanica.MOD_ID, "textures/gui/empty_fuel_overlay.png");

    public BiotechCoreInserterScreen(BiotechCoreInserterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    protected void renderProgressArrow(DrawContext context, int x, int y) {
        if (this.handler.isCrafting()) {
            context.drawTexture(BACKGROUND_TEXTURE, x + 99, y + 33, 176, 0, 8, handler.getScaledProgress());
        }
    }

    protected void renderEmptyFuelOverlay(DrawContext context, int x, int y) {
        if (this.handler.hasEmptyFuelSlot()) {
            context.drawTexture(EMPTY_FUEL_OVERLAY, x + 55, y + 39, 0, 0, 13, 13);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

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
