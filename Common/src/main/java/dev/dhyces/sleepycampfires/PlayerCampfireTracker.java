package dev.dhyces.sleepycampfires;

import net.minecraft.world.entity.player.Player;

public interface PlayerCampfireTracker {
    static PlayerCampfireTracker getFrom(Player player) {
        return (PlayerCampfireTracker) player;
    }

    void setStared(boolean hasStared);

    boolean getStared();
}
