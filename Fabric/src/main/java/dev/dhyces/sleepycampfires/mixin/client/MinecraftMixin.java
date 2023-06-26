package dev.dhyces.sleepycampfires.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable
    public LocalPlayer player;

    @Shadow @Nullable public Screen screen;
}
