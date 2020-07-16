package net.quetzi.morpheus.world;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.quetzi.morpheus.api.INewDayHandler;

import java.util.Objects;

public class DefaultOverworldHandler implements INewDayHandler {

    @Override
    public void startNewDay() {
        World world = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer().getWorld(ServerWorld.field_234918_g_)).getWorld();
        ((ServerWorldInfo)world.getWorldInfo()).setDayTime(world.getWorldInfo().getDayTime() + getTimeToSunrise(world));
    }

    private long getTimeToSunrise(World world) {
        long dayLength = 24000;
        return dayLength - (world.getDayTime() % dayLength);
    }
}
