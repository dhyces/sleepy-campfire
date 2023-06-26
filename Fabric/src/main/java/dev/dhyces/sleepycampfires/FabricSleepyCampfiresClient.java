package dev.dhyces.sleepycampfires;

import dev.dhyces.sleepycampfires.client.screen.CampfireSleepScreen;
import dev.dhyces.sleepycampfires.modhelper.services.Services;
import dev.dhyces.sleepycampfires.networking.CampfireStarePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class FabricSleepyCampfiresClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(FabricSleepyCampfires.SLEEP_BLOCK_TYPE, (packet, player, responseSender) -> {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(packet.id);
                if (entity instanceof LivingEntity livingEntity) {
                    if (entity == player && !SleepyCampfires.isValidSleepPos(player, Services.PLATFORM_HELPER.getPlayerReach(player))) {
                        return;
                    }
                    SleepTracker.getFrom(livingEntity).setSleptBlock(packet.block);
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(this::endClientTick);

        HudRenderCallback.EVENT.register(SleepyCampfiresClient::renderCampfireStare);

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof InBedChatScreen && SleepyCampfires.isValidSleepBlock(client.player)) {
                client.setScreen(new CampfireSleepScreen());
            }
        });
    }

    private void endClientTick(Minecraft client) {
        // If player has stared at the campfire for over a second, send a packet and update tracker
        // If the current position is not the same campfire and the tracker is true, set it to false and send a packet
        if (client.player == null || client.level == null || client.hitResult == null) {
            updateInvalidStare(null, null);
            return;
        }

        PlayerCampfireTracker campfireTracker = PlayerCampfireTracker.getFrom(client.player);

        if (client.hitResult.getType() != HitResult.Type.BLOCK) {
            updateInvalidStare(null, campfireTracker);
            return;
        }

        if (client.hitResult instanceof BlockHitResult clientBlockHit) {
            BlockPos hitPos = clientBlockHit.getBlockPos().immutable();
            if (!hitPos.equals(SleepyCampfiresClient.lastBlockPos)) {
                updateInvalidStare(hitPos, campfireTracker);
                return;
            }

            if (client.level.getBlockState(hitPos).is(SleepyCampfires.SLEEPING_CAMPFIRE)) {
                if (SleepyCampfiresClient.stareTime == 0) {
                    SleepyCampfiresClient.stareTime = client.level.getGameTime() + SleepyCampfiresClient.TICKS_TO_ELAPSE;
                } else if (client.level.getGameTime() - SleepyCampfiresClient.stareTime >= 0 && !campfireTracker.getStared()) {
                    campfireTracker.setStared(true);
                    ClientPlayNetworking.send(new CampfireStarePacket(true));
                }
                SleepyCampfiresClient.lastBlockPos = hitPos;
            }
        }
    }

    private void updateInvalidStare(@Nullable BlockPos hitPos, @Nullable PlayerCampfireTracker campfireTracker) {
        SleepyCampfiresClient.stareTime = 0;
        SleepyCampfiresClient.lastBlockPos = hitPos;
        if (campfireTracker != null && campfireTracker.getStared()) {
            campfireTracker.setStared(false);
            ClientPlayNetworking.send(new CampfireStarePacket(false));
        }
    }
}
