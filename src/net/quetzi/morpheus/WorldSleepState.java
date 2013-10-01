package net.quetzi.morpheus;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;

public class WorldSleepState {
	private int percentSleeping,sleepingPlayers;
	private HashMap<String,Boolean> playerStatus;
	
	public int getPercentSleeping() {
		return percentSleeping;
	}
	public int getSleepingPlayers() {
		return sleepingPlayers;
	}
	public HashMap<String, Boolean> getPlayerStatus() {
		return playerStatus;
	}
	public void setPlayerStatus(HashMap<String, Boolean> playerStatus) {
		this.playerStatus = playerStatus;
	}
	public void setPlayerAsleep(int dimension) {
		
	}
	public void setPlayerAwake(int dimension) {
		
	}
	public void removePlayer(EntityPlayer player) {
		
	}
}
