package net.quetzi.morpheus.api;

public interface IMorpheusAPI {

    void registerHandler(INewDayHandler newdayhandler);
    void unregisterHandler(INewDayHandler newdayhandler);
}
