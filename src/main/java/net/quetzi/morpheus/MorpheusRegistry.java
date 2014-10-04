package net.quetzi.morpheus;

import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.INewDayHandler;
import net.quetzi.morpheus.world.DefaultOverworldHandler;

import java.util.HashMap;

public class MorpheusRegistry implements IMorpheusAPI {

    public static HashMap<Integer, INewDayHandler> registry;

    public MorpheusRegistry() {

        registry = new HashMap<Integer, INewDayHandler>();

        registerHandler(new DefaultOverworldHandler(), 0);
    }

    @Override
    public void registerHandler(INewDayHandler newDayHandler, int dimension) {

        if (registry.containsKey(dimension)) Morpheus.mLog.warn("INewDayHandler for dimension " + dimension + " has been replaced");
        registry.put(dimension, newDayHandler);
    }

    @Override
    public void unregisterHandler(int dimension) {

        registry.remove(dimension);
    }
}
