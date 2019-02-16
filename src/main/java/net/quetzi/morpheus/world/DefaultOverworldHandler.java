package net.quetzi.morpheus.world;

import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.quetzi.morpheus.api.INewDayHandler;

/**
 * @author dmillerw
 */
public class DefaultOverworldHandler implements INewDayHandler
{

    @Override
    public void startNewDay()
    {
        World world = ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD);
        world.setTotalWorldTime(world.getGameTime() + getTimeToSunrise(world));
    }

    private long getTimeToSunrise(World world)
    {
        long dayLength = 24000;
        return dayLength - (world.getGameTime() % dayLength);
    }
}
