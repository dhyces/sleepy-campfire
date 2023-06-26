package dev.dhyces.sleepycampfires;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface SleepTracker {
    static SleepTracker getFrom(LivingEntity livingEntity) {
        return (SleepTracker) livingEntity;
    }

    void setSleptBlock(BlockState block);

    Optional<BlockState> getSleptBlock();
}
