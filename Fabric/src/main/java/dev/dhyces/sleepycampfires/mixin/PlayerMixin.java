package dev.dhyces.sleepycampfires.mixin;

import dev.dhyces.sleepycampfires.PlayerCampfireTracker;
import dev.dhyces.sleepycampfires.SleepTracker;
import dev.dhyces.sleepycampfires.SleepyCampfires;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerCampfireTracker {

    @Unique
    boolean hasStared;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setStared(boolean hasStared) {
        this.hasStared = hasStared;
    }

    @Override
    public boolean getStared() {
        return hasStared;
    }

    @Inject(method = "updatePlayerPose", at = @At("TAIL"))
    private void sleepycampfire$forceSit(CallbackInfo ci) {
        if (isSleeping() && SleepTracker.getFrom(this).getSleptBlock().map(block -> block.is(SleepyCampfires.SLEEPING_CAMPFIRE)).orElse(false)) {
            setPose(Pose.SITTING);
        }
    }

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    private void sleepycampfires$setSittingDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (SleepTracker.getFrom(this).getSleptBlock().map(block -> block.is(SleepyCampfires.SLEEPING_CAMPFIRE)).orElse(false) && pose == Pose.SITTING) {
            cir.setReturnValue(SleepyCampfires.SITTING_AT_CAMPFIRE);
        }
    }
}
