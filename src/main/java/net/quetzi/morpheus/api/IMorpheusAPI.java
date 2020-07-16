package net.quetzi.morpheus.api;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public interface IMorpheusAPI {
    /**
     * Register your INewDayHandler with MorpheusRegistry
     *
     * @param newdayhandler Method that updates time in the dimension to the next morning
     * @param dimension     Dimension to be registered
     */
    void registerHandler(INewDayHandler newdayhandler, RegistryKey<World> dimension);

    void unregisterHandler(RegistryKey<World> dimension);
}
