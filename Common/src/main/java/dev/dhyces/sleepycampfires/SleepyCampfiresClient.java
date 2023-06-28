package dev.dhyces.sleepycampfires;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Constants;
import dev.dhyces.sleepycampfires.client.screen.CampfireSleepScreen;
import dev.dhyces.sleepycampfires.mixin.client.CameraAccessor;
import dev.dhyces.sleepycampfires.modhelper.services.Services;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

public class SleepyCampfiresClient {

    public static final float HALF_PI = 0.017453292f;

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

    public static void onCameraUpdate(Camera camera, BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick) {
        CameraAccessor cameraAccessor = (CameraAccessor) camera;
        if (!detached && entity instanceof LivingEntity livingEntity && livingEntity.isSleeping() && livingEntity.hasPose(Pose.SITTING) && SleepyCampfires.isValidSleepBlock(livingEntity)) {
            Vec3 campfirePos = Vec3.atCenterOf(livingEntity.getSleepingPos().orElse(livingEntity.blockPosition()));
            Vec3 distances = livingEntity.getEyePosition(partialTick).vectorTo(campfirePos);
//            float xRot = (float) Mth.wrapDegrees(-Mth.atan2(distances.y, Mth.sqrt((float) (distances.x * distances.x + distances.z * distances.z))) * Constants.RAD_TO_DEG);
            float yRot = (float) Mth.wrapDegrees((Mth.atan2(distances.z, distances.x) * Constants.RAD_TO_DEG) - 90f);
            cameraAccessor.invokeSetRotation(yRot, 0);
            cameraAccessor.invokeMove(0.4, 0, 0);
            if (livingEntity instanceof Player player) {
                cameraAccessor.invokeSetRotation(camera.getYRot(), camera.getXRot() + (30 * (player.getSleepTimer() / 100f)));
            }
        }
    }
}
