package dev.dhyces.sleepycampfires.mixin.client;

import dev.dhyces.sleepycampfires.client.screen.CampfireSleepScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "isChatFocused", at = @At("HEAD"), cancellable = true)
    private void sleepycampfires$hideChat(CallbackInfoReturnable<Boolean> cir) {
        if (minecraft.screen instanceof CampfireSleepScreen campfireSleepScreen) {
            if (!campfireSleepScreen.isChatVisible()) {
                cir.setReturnValue(false);
            }
        }
    }
}
