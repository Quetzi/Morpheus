package net.quetzi.morpheus;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.INewDayHandler;
import net.quetzi.morpheus.world.DefaultOverworldHandler;

import java.util.HashMap;

public class MorpheusRegistry implements IMorpheusAPI {
    public static HashMap<ResourceKey<Level>, INewDayHandler> registry;

    public MorpheusRegistry() {
        registry = new HashMap<>();
        registerHandler(new DefaultOverworldHandler(), ServerLevel.OVERWORLD);
    }

    @Override
    public void registerHandler(INewDayHandler newDayHandler, ResourceKey<Level> dimension) {
        if (registry.containsKey(dimension))
            Morpheus.logger.warn("New day handler for dimension " + dimension + " has been replaced");
        registry.put(dimension, newDayHandler);
    }

    @Override
    public void unregisterHandler(Registry<Level> dimension) {
        Morpheus.logger.warn("New day handler for dimension " + dimension + " has been removed");
        registry.remove(dimension);
    }

    public boolean isDimRegistered(Registry<Level> dim) {
        return registry.containsKey(dim);
    }
}
