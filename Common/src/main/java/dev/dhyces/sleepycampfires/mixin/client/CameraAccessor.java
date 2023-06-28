package dev.dhyces.sleepycampfires.mixin.client;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraAccessor {
    @Invoker
    void invokeSetRotation(float yRot, float xRot);

    @Invoker
    void invokeMove(double distanceOffset, double verticalOffset, double horizonalOffset);
}
