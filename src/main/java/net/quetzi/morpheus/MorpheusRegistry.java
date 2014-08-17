package net.quetzi.morpheus;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.IMorpheusListener;
import net.quetzi.morpheus.api.INewDayHandler;

public class MorpheusRegistry implements IMorpheusAPI {
    
    public HashMap<Integer, INewDayHandler>        handlers;
    
    public List<Entry<Integer, IMorpheusListener>> dimensionListeners;
    public List<IMorpheusListener>                 globalListeners;
    
    protected MorpheusRegistry() {
    
        handlers = new HashMap<Integer, INewDayHandler>();
        
        dimensionListeners = new ArrayList<Entry<Integer, IMorpheusListener>>();
        globalListeners = new ArrayList<IMorpheusListener>();
    }
    
    @Override
    public void registerHandler(INewDayHandler newDayHandler, int dimension) {
    
        handlers.put(dimension, newDayHandler);
    }
    
    @Override
    public void unregisterHandler(int dimension) {
    
        handlers.remove(dimension);
    }
    
    @Override
    public INewDayHandler getHandler(int dimensionId) {
    
        return null;
    }
    
    @Override
    public void registerListener(IMorpheusListener listener, int dimension) {
    
        dimensionListeners.add(new AbstractMap.SimpleEntry<Integer, IMorpheusListener>(dimension, listener));
    }
    
    @Override
    public void registerListener(IMorpheusListener listener) {
    
        globalListeners.add(listener);
    }
    
    @Override
    public IMorpheusListener[] getListeners(int dimension) {
    
        List<IMorpheusListener> listeners = new ArrayList<IMorpheusListener>();
        listeners.addAll(globalListeners);
        
        for (Entry<Integer, IMorpheusListener> listener : dimensionListeners)
            listeners.add(listener.getValue());
        
        IMorpheusListener[] listenerArray = listeners.toArray(new IMorpheusListener[listeners.size()]);
        listeners.clear();
        return listenerArray;
    }
}
