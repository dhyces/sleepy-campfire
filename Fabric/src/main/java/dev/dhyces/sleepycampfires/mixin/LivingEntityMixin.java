package dev.dhyces.sleepycampfires.mixin;

import dev.dhyces.sleepycampfires.SleepTracker;
import dev.dhyces.sleepycampfires.SleepyCampfires;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SleepTracker {

    @Shadow public abstract boolean isSleeping();

    @Unique
    private BlockState sleepBlock;
    @Unique
    private boolean hasStartedSleeping;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setSleptBlock(BlockState block) {
        this.sleepBlock = block;
        if (block == null) {
            hasStartedSleeping = false;
        }
    }

    @Override
    public Optional<BlockState> getSleptBlock() {
        return Optional.ofNullable(sleepBlock);
    }

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    private void sleepycampfires$setSittingDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (sleepBlock != null && sleepBlock.is(SleepyCampfires.SLEEPING_CAMPFIRE) && pose == Pose.SITTING) {
            cir.setReturnValue(SleepyCampfires.SITTING_AT_CAMPFIRE);
        }
    }

    @Inject(method = "getEyeHeight", at = @At("HEAD"), cancellable = true)
    private void sleepycampfires$adjustEyeHeight(Pose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
        if (sleepBlock != null && sleepBlock.is(SleepyCampfires.SLEEPING_CAMPFIRE) && pose == Pose.SITTING) {
            cir.setReturnValue(dimensions.height);
        }
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setXRot(F)V"))
    private float sleepycampfires$rotateHead(float originalX) {
        if (isSleeping() && sleepBlock != null && sleepBlock.is(SleepyCampfires.SLEEPING_CAMPFIRE)) {
            if (!hasStartedSleeping) {
                hasStartedSleeping = true;
                return 0;
            }
            return Math.min(getXRot() + 0.25f, 30f);
        }
        return originalX;
    }
}
