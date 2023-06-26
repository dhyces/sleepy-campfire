package dev.dhyces.sleepycampfires.modhelper.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.dhyces.sleepycampfires.modhelper.services.helpers.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class FabricGuiHelper implements GuiHelper {

    @Override
    public void setupForRender(boolean enableBlend, boolean enableDepth) {
        if (enableBlend) RenderSystem.enableBlend();
        else RenderSystem.disableBlend();
        if (enableDepth) RenderSystem.enableDepthTest();
        else RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
    }

    @Override
    public boolean shouldRenderSurvivalAspects() {
        return Minecraft.getInstance().gameMode.canHurtPlayer() && Minecraft.getInstance().getCameraEntity() instanceof Player;
    }
}
