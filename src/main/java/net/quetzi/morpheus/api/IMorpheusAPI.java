package net.quetzi.morpheus.api;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.core.Registry;

public interface IMorpheusAPI {
    /**
     * Register your INewDayHandler with MorpheusRegistry
     *
     * @param newDayHandler Method that updates time in the dimension to the next morning
     * @param dimension     Dimension to be registered
     */
    void registerHandler(INewDayHandler newDayHandler, ResourceKey<Level> dimension);

    void unregisterHandler(Registry<Level> dimension);
}
