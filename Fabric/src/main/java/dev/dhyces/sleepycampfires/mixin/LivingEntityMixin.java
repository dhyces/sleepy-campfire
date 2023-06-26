package dev.dhyces.sleepycampfires.mixin;

import dev.dhyces.sleepycampfires.SleepTracker;
import dev.dhyces.sleepycampfires.SleepyCampfires;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements SleepTracker {

    @Unique
    private BlockState sleepBlock;

    @Override
    public void setSleptBlock(BlockState block) {
        this.sleepBlock = block;
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
    private void sleepycampfire$adjustEyeHeight(Pose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
        if (sleepBlock != null && sleepBlock.is(SleepyCampfires.SLEEPING_CAMPFIRE) && pose == Pose.SITTING) {
            cir.setReturnValue(dimensions.height);
        }
    }
}
