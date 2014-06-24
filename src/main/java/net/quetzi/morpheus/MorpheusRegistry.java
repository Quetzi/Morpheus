package net.quetzi.morpheus;

import java.util.ArrayList;
import java.util.Iterator;

import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.INewDayHandler;

public class MorpheusRegistry implements IMorpheusAPI {
    
    private ArrayList<INewDayHandler> registry;

    public MorpheusRegistry() {

        registry = new ArrayList<INewDayHandler>();
    }

    @Override
    public void registerHandler(INewDayHandler newDayHandler) {
        
        registry.add(newDayHandler);
    }

    @Override
    public void unregisterHandler(INewDayHandler newdayhandler) {
        
        Iterator ite = registry.iterator();
        while (ite.hasNext()) {
            if (((INewDayHandler)ite).equals(newdayhandler)) {
                ite.remove();
            }
        }
    }

}
