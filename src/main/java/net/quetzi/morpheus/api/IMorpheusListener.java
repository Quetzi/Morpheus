package net.quetzi.morpheus.api;

public interface IMorpheusListener {
    
    public void onPlayerSleep(int dimension, String username, IWorldSleepState state);
    
    public void onSkipNight(int dimesion);
    
}
