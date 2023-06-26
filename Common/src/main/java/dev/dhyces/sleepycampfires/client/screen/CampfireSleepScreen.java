package dev.dhyces.sleepycampfires.client.screen;

import dev.dhyces.sleepycampfires.SleepyCampfires;
import dev.dhyces.sleepycampfires.modhelper.services.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class CampfireSleepScreen extends ChatScreen {

    public CampfireSleepScreen() {
        super("");
    }

    @Override
    protected void init() {
        super.init();
        this.input.setFocused(false);
        this.input.setVisible(false);
        this.input.setResponder(this::nothing);
    }

    private void nothing(String _input) {}

    @Override
    public void render(GuiGraphics guiGraphics, int $$1, int $$2, float $$3) {
        renderBackground(guiGraphics);
        if (this.input.isVisible()) {
            super.render(guiGraphics, $$1, $$2, $$3);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (!input.isVisible() && minecraft.options.keyChat.matches(keyCode, scanCode) || minecraft.options.keyCommand.matches(keyCode, scanCode)) {
            input.setVisible(true);
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        Minecraft client = Minecraft.getInstance();

        guiGraphics.fill(0, 0, width, height, 0xFF000000);

        TextureAtlasSprite campfireFire = client.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("block/campfire_fire"));
        guiGraphics.blit(64, 0, 0, guiGraphics.guiWidth() - 64, guiGraphics.guiHeight(), campfireFire, 1, 1, 1, 0.5f);
    }

    @Override
    public void onClose() {
        this.minecraft.player.connection.send(new ServerboundPlayerCommandPacket(this.minecraft.player, ServerboundPlayerCommandPacket.Action.STOP_SLEEPING));
    }

    public void wakeUp() {
        if (this.input.getValue().isEmpty()) {
            this.minecraft.setScreen(null);
        } else {
            this.minecraft.setScreen(new ChatScreen(this.input.getValue()));
        }
    }
}
