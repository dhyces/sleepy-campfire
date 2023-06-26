package dev.dhyces.sleepycampfires;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod(SleepyCampfires.MODID)
public class ForgeSleepyCampfires extends SleepyCampfires {

    public ForgeSleepyCampfires() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        forgeBus.addListener(this::onCampfireUse);
        forgeBus.addListener(this::checkCampfire);
        forgeBus.addListener(this::onWakeup);
    }

    private void onCampfireUse(final PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().getBlockState(event.getHitVec().getBlockPos()).is(BlockTags.CAMPFIRES)) {
            if (trySleep(event.getEntity(), event.getLevel())) {
                event.getEntity().setForcedPose(Pose.SITTING);
                event.getEntity().refreshDimensions();
            }
        }
    }

    private void checkCampfire(final SleepingLocationCheckEvent event) {
        LivingEntity entity = event.getEntity();
        double reach = entity instanceof Player ? entity.getAttributeValue(ForgeMod.BLOCK_REACH.get()) : 3;
        if (isValidSleepPos(entity, reach)) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    private void onWakeup(final PlayerWakeUpEvent event) {
        event.getEntity().setForcedPose(null);
    }
}
