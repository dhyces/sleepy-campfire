package dev.dhyces.sleepycampfires.modhelper.helpers;

import dev.dhyces.sleepycampfires.PlayerCampfireTracker;
import dev.dhyces.sleepycampfires.SleepTracker;
import dev.dhyces.sleepycampfires.modhelper.services.helpers.PlatformHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.Optional;

public final class ForgePlatformHelper implements PlatformHelper {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public boolean isClientDist() {
        return FMLLoader.getDist().isClient();
    }

    @Override
    public double getPlayerReach(Player player) {
        return player.getAttributeValue(ForgeMod.BLOCK_REACH.get());
    }

    @Override
    public boolean isValidSleepTime(LivingEntity entity) {
        if (entity instanceof Player player) {
            return ForgeEventFactory.fireSleepingTimeCheck(player, player.getSleepingPos());
        }
        return true;
    }

    @Override
    public PlayerCampfireTracker getStareTracker(Player player) {
        return null; // TODO: impl
    }

    @Override
    public SleepTracker getSleepTracker(LivingEntity livingEntity) {
        return null; // TODO: impl
    }
}
