package net.quetzi.morpheus;

import java.util.ArrayList;

import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.INewDayHandler;

public class MorpheusRegistry implements IMorpheusAPI {
    
    public static ArrayList<INewDayHandler> registry;

    public MorpheusRegistry() {

        registry = new ArrayList<INewDayHandler>();
    }

    @Override
    public void registerHandler(INewDayHandler newDayHandler, int dimension) {
        
        registry.add(dimension, newDayHandler);
    }

    @Override
    public void unregisterHandler(int dimension) {
        
        registry.remove(dimension);
    }

}
