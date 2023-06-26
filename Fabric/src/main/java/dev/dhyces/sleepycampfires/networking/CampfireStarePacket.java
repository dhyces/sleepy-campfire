package dev.dhyces.sleepycampfires.networking;

import dev.dhyces.sleepycampfires.FabricSleepyCampfires;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;

public class CampfireStarePacket implements FabricPacket {

    public final boolean stared;

    public CampfireStarePacket(boolean stared) {
        this.stared = stared;
    }

    public CampfireStarePacket(FriendlyByteBuf buf) {
        stared = buf.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(stared);
    }

    @Override
    public PacketType<?> getType() {
        return FabricSleepyCampfires.CAMPFIRE_STARE_TYPE;
    }
}
