package dhyces.modhelper.helpers;

import dev.dhyces.sleepycampfires.PlayerCampfireTracker;
import dev.dhyces.sleepycampfires.SleepTracker;
import dev.dhyces.sleepycampfires.modhelper.services.helpers.PlatformHelper;
import net.fabricmc.api.EnvType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.impl.launch.common.QuiltLauncherBase;

public final class QuiltPlatformHelper implements PlatformHelper {
    @Override
    public boolean isModLoaded(String modid) {
        return QuiltLoader.isModLoaded(modid);
    }

    @Override
    public boolean isClientDist() {
        return QuiltLauncherBase.getLauncher().getEnvironmentType().equals(EnvType.CLIENT);
    }

    @Override
    public double getPlayerReach(Player player) {
        return 4;
    }

    @Override
    public boolean isValidSleepTime(LivingEntity entity) {
        return false; // TODO: impl
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
