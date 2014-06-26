package net.quetzi.morpheus.api;

public interface IMorpheusAPI {

    /**
     * Register your INewDayHandler with MorpheusRegistry
     *
     * @param newdayhandler
     * @param dimension
     */
    void registerHandler(INewDayHandler newdayhandler, int dimension);
    void unregisterHandler(int dimension);
}
