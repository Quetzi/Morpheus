package net.quetzi.morpheus.api;

public interface IMorpheusAPI {

    void registerHandler(INewDayHandler newdayhandler, int dimension);
    void unregisterHandler(int dimension);
}
