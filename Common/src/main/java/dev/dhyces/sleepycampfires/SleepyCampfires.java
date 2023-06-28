package dev.dhyces.sleepycampfires;

import dev.dhyces.sleepycampfires.modhelper.services.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class SleepyCampfires {
    public static final String MODID = "sleepycampfires";
    public static ResourceLocation id(String id) {
        return new ResourceLocation(MODID, id);
    }

    // TODO: use a custom tag so that people can change it
    public static final TagKey<Block> SLEEPING_CAMPFIRE = BlockTags.CAMPFIRES;

    public static final EntityDimensions SITTING_AT_CAMPFIRE = new EntityDimensions(0.6f, 0.9f, true);

    public boolean trySleep(Player player, Level level, BlockPos blockPos) {
        if (!Services.PLATFORM_HELPER.isValidSleepTime(player) || !isValidSleepPos(player, Services.PLATFORM_HELPER.getPlayerReach(player))) {
            return false;
        }
        boolean isSuccess = true;

        if (player.isPassenger()) {
            player.stopRiding();
        }

        player.setSleepingPos(blockPos);
        player.setPose(Pose.SITTING);
        player.refreshDimensions();
        player.setDeltaMovement(Vec3.ZERO);
        player.hasImpulse = true;

        if (level instanceof ServerLevel serverLevel) {
            if (!serverLevel.canSleepThroughNights()) {
                player.displayClientMessage(Component.translatable("sleep.not_possible"), true);
                isSuccess = false;
            }

            serverLevel.updateSleepingPlayerList();
        }
        return isSuccess;
    }

    public static boolean isValidSleepPos(LivingEntity livingEntity, double reach) {
        return BlockPos.betweenClosedStream(livingEntity.getBoundingBox().inflate(reach)).anyMatch(blockPos -> livingEntity.level().getBlockState(blockPos).is(SLEEPING_CAMPFIRE));
    }

    public static boolean isValidSleepBlock(LivingEntity livingEntity) {
        return Services.PLATFORM_HELPER.getSleepTracker(livingEntity).getSleptBlock().map(blockState -> blockState.is(SLEEPING_CAMPFIRE)).orElse(false);
    }
}
