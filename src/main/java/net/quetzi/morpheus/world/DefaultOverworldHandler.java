package net.quetzi.morpheus.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.quetzi.morpheus.api.INewDayHandler;

import java.util.Objects;

public class DefaultOverworldHandler implements INewDayHandler {

    @Override
    public void startNewDay() {
        Level world = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getLevel(ServerLevel.OVERWORLD)).getLevel();
        ((ServerLevelData)world.getLevelData()).setDayTime(world.getLevelData().getDayTime() + getTimeToSunrise(world));
    }

    private long getTimeToSunrise(Level world) {
        long dayLength = 24000;
        return dayLength - (world.getDayTime() % dayLength);
    }
}
