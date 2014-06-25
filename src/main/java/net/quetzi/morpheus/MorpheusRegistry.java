package net.quetzi.morpheus;

import java.util.ArrayList;
import java.util.HashMap;

import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.INewDayHandler;

public class MorpheusRegistry implements IMorpheusAPI {

    public static HashMap<Integer, INewDayHandler> registry;

    public MorpheusRegistry() {

        registry = new HashMap<Integer, INewDayHandler>();
    }

    @Override
    public void registerHandler(INewDayHandler newDayHandler, int dimension) {

        registry.put(dimension, newDayHandler);
    }

    @Override
    public void unregisterHandler(int dimension) {

        registry.remove(dimension);
    }
}
