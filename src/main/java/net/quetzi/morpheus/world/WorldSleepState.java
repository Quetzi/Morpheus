package net.quetzi.morpheus.world;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.quetzi.morpheus.api.IWorldSleepState;

public class WorldSleepState implements IWorldSleepState {
    
    private int                      dimension;
    private HashMap<String, Boolean> playerStatus;
    
    public WorldSleepState(int dimension) {
    
        this.dimension = dimension;
        playerStatus = new HashMap<String, Boolean>();
    }
    
    @Override
    public int getDimension() {
    
        return dimension;
    }
    
    @Override
    public int getPercentSleeping() {
    
        return (getSleepingPlayers() * 100) / playerStatus.size();
    }
    
    @Override
    public int getSleepingPlayers() {
    
        int asleepCount = 0;
        Iterator<Entry<String, Boolean>> entry = playerStatus.entrySet().iterator();
        while (entry.hasNext()) {
            if (entry.next().getValue()) {
                asleepCount++;
            }
        }
        return asleepCount;
    }
    
    @Override
    public int getAllPlayers() {
    
        return playerStatus.size();
    }
    
    @Override
    public String toString() {
    
        return getSleepingPlayers() + "/" + playerStatus.size() + " (" + getPercentSleeping() + "%)";
    }
    
    public void setPlayerAsleep(String username) {
    
        playerStatus.put(username, true);
    }
    
    public void setPlayerAwake(String username) {
    
        playerStatus.put(username, false);
    }
    
    @Override
    public boolean isPlayerSleeping(String username) {
    
        if (playerStatus.containsKey(username)) {
            return playerStatus.get(username);
        } else {
            playerStatus.put(username, false);
        }
        return false;
    }
    
    public void removePlayer(String username) {
    
        playerStatus.remove(username);
    }
    
    public void wakeAllPlayers() {
    
        Iterator<Entry<String, Boolean>> entry = playerStatus.entrySet().iterator();
        while (entry.hasNext()) {
            entry.next().setValue(false);
        }
    }
}
