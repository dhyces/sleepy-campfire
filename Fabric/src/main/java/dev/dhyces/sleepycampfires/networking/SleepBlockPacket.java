package dev.dhyces.sleepycampfires.networking;

import dev.dhyces.sleepycampfires.FabricSleepyCampfires;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SleepBlockPacket implements FabricPacket {

    public final int id;
    public final BlockState block;

    public SleepBlockPacket(FriendlyByteBuf buf) {
        id = buf.readInt();
        block = Block.stateById(buf.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(Block.getId(block));
    }

    @Override
    public PacketType<?> getType() {
        return FabricSleepyCampfires.SLEEP_BLOCK_TYPE;
    }
}
