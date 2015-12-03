package net.quetzi.morpheus.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.quetzi.morpheus.Morpheus;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class WorldSleepState {

    private int                      dimension;
    private HashMap<String, Boolean> playerStatus;

    public WorldSleepState(int dimension) {

        this.dimension = dimension;
        this.playerStatus = new HashMap<String, Boolean>();
    }

    public int getPercentSleeping() {

        return (this.playerStatus.size() - this.getMiningPlayers() > 0) ? (this.getSleepingPlayers() > 0) ? (this.getSleepingPlayers() * 100) / (this.playerStatus.size() - this.getMiningPlayers()) : 0 : 100;
    }
    
    private int getMiningPlayers() {
        int miningPlayers = 0;
        for (EntityPlayer player : (List<EntityPlayer>) MinecraftServer.getServer().worldServerForDimension(this.dimension).playerEntities) {
            if (player.posY < Morpheus.groundLevel) {
                miningPlayers++;
            }
        }
        return !Morpheus.includeMiners ? miningPlayers : 0;
    }

    public int getSleepingPlayers() {

        int asleepCount = 0;
        for (Entry<String, Boolean> entry : this.playerStatus.entrySet()) {
            if (entry.getValue())
                asleepCount++;
        }
        return asleepCount;
    }

    public String toString() {

        return Morpheus.includeMiners ? this.getSleepingPlayers() + "/" + this.playerStatus.size() + " (" + this.getPercentSleeping() + "%)" : this.getSleepingPlayers() + "/" + this.playerStatus.size() + " - " + this.getMiningPlayers() + " miners (" + this.getPercentSleeping() + "%)";
    }

    public void setPlayerAsleep(String username) {

        this.playerStatus.put(username, true);
    }

    public void setPlayerAwake(String username) {

        this.playerStatus.put(username, false);
    }

    public boolean isPlayerSleeping(String username) {

        if (this.playerStatus.containsKey(username)) {
            return this.playerStatus.get(username);
        } else {
            this.playerStatus.put(username, false);
        }
        return false;
    }

    public void removePlayer(String username) {

        this.playerStatus.remove(username);
    }

    public void wakeAllPlayers() {

        for (Entry<String, Boolean> entry : this.playerStatus.entrySet()) {
            entry.setValue(false);
        }
    }
}
