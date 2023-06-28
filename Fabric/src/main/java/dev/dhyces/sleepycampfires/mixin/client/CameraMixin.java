package dev.dhyces.sleepycampfires.mixin.client;

import dev.dhyces.sleepycampfires.SleepyCampfires;
import dev.dhyces.sleepycampfires.SleepyCampfiresClient;
import net.minecraft.client.Camera;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow protected abstract void move(double $$0, double $$1, double $$2);

    @Shadow protected abstract void setRotation(float $$0, float $$1);

    @Shadow private float yRot;

    @Shadow private float xRot;

    @Inject(method = "setup", at = @At("TAIL"))
    private void sleepycampfires$moveCameraOut(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        SleepyCampfiresClient.onCameraUpdate((Camera)(Object)this, level, entity, detached, thirdPersonReverse, partialTick);
    }
}
