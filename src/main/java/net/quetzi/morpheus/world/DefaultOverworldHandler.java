package net.quetzi.morpheus.world;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.quetzi.morpheus.api.INewDayHandler;

/**
 * @author dmillerw
 */
public class DefaultOverworldHandler implements INewDayHandler {

    @Override
    public void startNewDay() {

        World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
        world.setWorldTime(world.getWorldTime() + getTimeToSunrise(world));
    }

    private long getTimeToSunrise(World world) {

        long dayLength = 24000;
        return dayLength - (world.getWorldTime() % dayLength);
    }
}
