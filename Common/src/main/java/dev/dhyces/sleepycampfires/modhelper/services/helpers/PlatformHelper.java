package dev.dhyces.sleepycampfires.modhelper.services.helpers;

import dev.dhyces.sleepycampfires.PlayerCampfireTracker;
import dev.dhyces.sleepycampfires.SleepTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface PlatformHelper {
    boolean isModLoaded(String modid);
    boolean isClientDist();

    double getPlayerReach(Player player);

    boolean isValidSleepTime(LivingEntity entity);

    PlayerCampfireTracker getStareTracker(Player player);

    SleepTracker getSleepTracker(LivingEntity livingEntity);
}
