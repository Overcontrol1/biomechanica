package com.overcontrol1.biomechanica.client.screen;

import com.overcontrol1.biomechanica.client.util.ScreenUtil;
import com.overcontrol1.biomechanica.screen.BiotechCraftingStationScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BiotechCraftingStationScreen extends HandledScreen<BiotechCraftingStationScreenHandler> {
    private static final Identifier TEXTURE = ScreenUtil.getGuiTexture("biotech_crafting_station");
    public BiotechCraftingStationScreen(BiotechCraftingStationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.backgroundWidth = 176;
        this.backgroundHeight = 184;
        this.titleY = -3;
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
        this.playerInventoryTitleY = 81;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
