package net.quetzi.morpheus.world;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.quetzi.morpheus.helpers.Config;

import java.util.HashMap;
import java.util.Map.Entry;

public class WorldSleepState {
    private int dimension;
    private HashMap<String, Boolean> playerStatus;

    public WorldSleepState(int dimension) {
        this.dimension = dimension;
        this.playerStatus = new HashMap<>();
    }

    public int getPercentSleeping() {
        return (this.playerStatus.size() - this.getMiningPlayers() - this.getSpectators() > 0) ? (this.getSleepingPlayers() > 0) ? (this.getSleepingPlayers() * 100) / (this.playerStatus.size() - this.getMiningPlayers() - this.getSpectators()) : 0 : 100;
    }

    private int getMiningPlayers() {
        int miningPlayers = 0;
        for (PlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if ((player.dimension.getId() == this.dimension) && (player.posY < Config.SERVER.groundLevel.get())) {
                miningPlayers++;
            }
        }
        return Config.SERVER.includeMiners.get() ? miningPlayers : 0;
    }

    private int getSpectators() {
        int spectators = 0;
        for (PlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (player.isSpectator() || player.isCreative()) {
                spectators++;
            }
        }
        return spectators;
    }

    public int getSleepingPlayers() {
        int asleepCount = 0;
        for (Entry<String, Boolean> entry : this.playerStatus.entrySet()) {
            if (entry.getValue()) {
                asleepCount++;
            }
        }
        return asleepCount;
    }

    public String toString() {
        return !Config.SERVER.includeMiners.get() ? this.getSleepingPlayers() + "/" + this.playerStatus.size() + " (" + this.getPercentSleeping() + "%)" : this.getSleepingPlayers() + "/" + this.playerStatus.size() + " - " + this.getMiningPlayers() + " miners (" + this.getPercentSleeping() + "%)";
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
