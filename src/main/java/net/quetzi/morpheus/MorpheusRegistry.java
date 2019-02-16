package net.quetzi.morpheus;

import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.INewDayHandler;
import net.quetzi.morpheus.world.DefaultOverworldHandler;

import java.util.HashMap;

public class MorpheusRegistry implements IMorpheusAPI
{
    public static HashMap<Integer, INewDayHandler> registry;

    public MorpheusRegistry()
    {
        registry = new HashMap<>();
        registerHandler(new DefaultOverworldHandler(), 0);
    }

    @Override
    public void registerHandler(INewDayHandler newDayHandler, int dimension)
    {
        if (registry.containsKey(dimension))
            Morpheus.logger.warn("New day handler for dimension " + dimension + " has been replaced");
        registry.put(dimension, newDayHandler);
    }

    @Override
    public void unregisterHandler(int dimension)
    {
        registry.remove(dimension);
    }

    public boolean isDimRegistered(int dim)
    {
        return registry.containsKey(dim);
    }
}
