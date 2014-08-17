package net.quetzi.morpheus.api;

public interface IMorpheusAPI {
    
    /**
     * Register your INewDayHandler with MorpheusRegistry
     * 
     * @param newdayhandler
     * @param dimension
     */
    public void registerHandler(INewDayHandler newdayhandler, int dimension);
    
    public void unregisterHandler(int dimension);
    
    public INewDayHandler getHandler(int dimensionId);
    
    public void registerListener(IMorpheusListener listener, int dimension);
    
    public void registerListener(IMorpheusListener listener);
    
    public IMorpheusListener[] getListeners(int dimension);
}
