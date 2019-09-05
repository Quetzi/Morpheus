package net.quetzi.morpheus.world;

import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.quetzi.morpheus.api.INewDayHandler;

public class DefaultOverworldHandler implements INewDayHandler {

    @Override
    public void startNewDay() {
        World world = ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD);
        world.setDayTime(world.getDayTime() + getTimeToSunrise(world));
    }

    private long getTimeToSunrise(World world) {
        long dayLength = 24000;
        return dayLength - (world.getDayTime() % dayLength);
    }
}
