package dev.dhyces.sleepycampfires.modhelper.helpers;

import dev.dhyces.sleepycampfires.modhelper.services.helpers.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class ForgeGuiHelper implements GuiHelper {
    @Override
    public void setupForRender(boolean enableBlend, boolean enableDepth) {
        ((ForgeGui)Minecraft.getInstance().gui).setupOverlayRenderState(enableBlend, enableDepth);
    }

    @Override
    public boolean shouldRenderSurvivalAspects() {
        return ((ForgeGui)Minecraft.getInstance().gui).shouldDrawSurvivalElements();
    }
}
