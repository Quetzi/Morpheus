package net.quetzi.morpheus.api;

public interface IWorldSleepState {
    
    public int getDimension();
    
    public int getPercentSleeping();
    
    public int getSleepingPlayers();
    
    public int getAllPlayers();
    
    public boolean isPlayerSleeping(String username);
    
}
