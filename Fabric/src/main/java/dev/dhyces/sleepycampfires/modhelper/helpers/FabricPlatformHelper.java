package dev.dhyces.sleepycampfires.modhelper.helpers;

import dev.dhyces.sleepycampfires.PlayerCampfireTracker;
import dev.dhyces.sleepycampfires.SleepTracker;
import dev.dhyces.sleepycampfires.modhelper.services.helpers.PlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class FabricPlatformHelper implements PlatformHelper {
    @Override
    public boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override
    public boolean isClientDist() {
        return FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT);
    }

    @Override
    public double getPlayerReach(Player player) {
        return 4;
    }

    @Override
    public boolean isValidSleepTime(LivingEntity entity) {
        boolean vanillaResult = !entity.level().isDay();
        if (entity instanceof Player player) {
            BlockPos sleepingPos = player.getSleepingPos().orElse(player.blockPosition());
            return EntitySleepEvents.ALLOW_SLEEP_TIME.invoker().allowSleepTime(player, sleepingPos, vanillaResult) == InteractionResult.sidedSuccess(entity.level().isClientSide);
        }
        return vanillaResult;
    }

    @Override
    public PlayerCampfireTracker getStareTracker(Player player) {
        return PlayerCampfireTracker.getFrom(player);
    }

    @Override
    public SleepTracker getSleepTracker(LivingEntity livingEntity) {
        return SleepTracker.getFrom(livingEntity);
    }
}
