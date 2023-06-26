package dev.dhyces.sleepycampfires;

import dev.dhyces.sleepycampfires.networking.CampfireStarePacket;
import dev.dhyces.sleepycampfires.networking.SleepBlockPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;

public class FabricSleepyCampfires extends SleepyCampfires implements ModInitializer {

    public static final PacketType<SleepBlockPacket> SLEEP_BLOCK_TYPE = PacketType.create(SleepyCampfires.id("sleep_block"), SleepBlockPacket::new);
    public static final PacketType<CampfireStarePacket> CAMPFIRE_STARE_TYPE = PacketType.create(SleepyCampfires.id("campfire_stare"), CampfireStarePacket::new);

    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            PlayerCampfireTracker campfireTracker = PlayerCampfireTracker.getFrom(player);
            if (!campfireTracker.getStared()) {
                return InteractionResult.PASS;
            }

            BlockState state = world.getBlockState(hitResult.getBlockPos());
            if (state.is(SleepyCampfires.SLEEPING_CAMPFIRE)) {
                SleepTracker.getFrom(player).setSleptBlock(state);
                if (trySleep(player, world)) {
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            }
            return InteractionResult.PASS;
        });
        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> {
            if (isValidSleepPos(player, 4) && vanillaResult) {
                return InteractionResult.sidedSuccess(player.level().isClientSide);
            }
            return InteractionResult.PASS;
        });
        EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) -> {
            if (SleepTracker.getFrom(entity).getSleptBlock().map(block -> block.is(SLEEPING_CAMPFIRE)).orElse(false)) {
                return InteractionResult.sidedSuccess(entity.level().isClientSide);
            }
            return InteractionResult.PASS;
        });
        EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
            SleepTracker.getFrom(entity).setSleptBlock(null);
            if (entity.level().isClientSide) {
                SleepyCampfiresClient.closeCampfireSleepScreen();
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(CAMPFIRE_STARE_TYPE, (packet, player, responseSender) -> {
            PlayerCampfireTracker.getFrom(player).setStared(packet.stared);
        });
    }
}
