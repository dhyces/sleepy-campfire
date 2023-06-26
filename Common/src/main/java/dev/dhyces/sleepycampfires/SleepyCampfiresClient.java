package dev.dhyces.sleepycampfires;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dhyces.sleepycampfires.client.screen.CampfireSleepScreen;
import dev.dhyces.sleepycampfires.modhelper.services.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.inventory.InventoryMenu;

public class SleepyCampfiresClient {

    public static BlockPos lastBlockPos;
    public static long stareTime;
    public static final int TICKS_TO_ELAPSE = 20;

    public static final ResourceLocation CAMPFIRE_OUTLINE = SleepyCampfires.id("textures/gui/campfire_outline.png");
    public static final ResourceLocation CAMPFIRE_ITEM = new ResourceLocation("textures/item/campfire.png");

    public static void renderCampfireStare(GuiGraphics drawContext, float tickDelta) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null || client.hitResult == null || lastBlockPos == null || stareTime == 0 || client.player.isSleeping()) {
            return;
        }

        long current = client.level.getGameTime();

        float progress = 1 - ((float) stareTime - current) / (float) TICKS_TO_ELAPSE;
        float clampedProgress = Mth.clamp(progress, 0 , 1);
        int pixelProgress = (int)((16 * clampedProgress));

        int x = (drawContext.guiWidth() / 2) - 16;
        int y = (drawContext.guiHeight() / 2) - 8;

        drawContext.blit(CAMPFIRE_OUTLINE, x, y - 16, 0, 0, 16, 16, 16, 16);
        drawContext.blit(CAMPFIRE_ITEM, x, y - pixelProgress, 0, 16 - pixelProgress, 16, pixelProgress, 16, 16);

        if (clampedProgress == 1) {
            drawContext.drawCenteredString(client.font, Component.translatableWithFallback("sleepycampfires.sleep", "Sleep"), x + 32, y - 12, 0xFFFFFF);
        }
    }

    public static void closeCampfireSleepScreen() {
        if (Minecraft.getInstance().screen instanceof CampfireSleepScreen campfireSleepScreen) {
            campfireSleepScreen.wakeUp();
        }
    }

    public static <T extends LivingEntity> void adjustModelForSitting(T livingEntity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (livingEntity.isSleeping() && SleepTracker.getFrom(livingEntity).getSleptBlock().map(block -> block.is(SleepyCampfires.SLEEPING_CAMPFIRE)).orElse(false)) {
            if (livingEntity.hasPose(Pose.SITTING)) {
                matrixStack.translate(0, -0.6, 0);
            }
        }
    }
}
