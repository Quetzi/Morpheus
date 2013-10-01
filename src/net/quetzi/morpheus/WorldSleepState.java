package net.quetzi.morpheus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class WorldSleepState {
	private int dimension;
	private HashMap<String, Boolean> playerStatus;

	public WorldSleepState(int dimension) {
		this.dimension = dimension;
		this.playerStatus = new HashMap<String, Boolean>();
	}

	public int getPercentSleeping() {
		return (this.getSleepingPlayers() * 100) / this.playerStatus.size();
	}

	public int getSleepingPlayers() {
		int asleepCount = 0;
		Iterator<Entry<String, Boolean>> entry = this.playerStatus.entrySet().iterator();
		while (entry.hasNext()) {
			if (entry.next().getValue()) {
				asleepCount++;
			}
		}
		return asleepCount;
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
		}
		else {
			this.playerStatus.put(username, false);
		}
		return false;
	}

	public void removePlayer(String username) {
		this.playerStatus.remove(username);
	}

	public void wakeAllPlayers() {
		Iterator<Entry<String, Boolean>> entry = this.playerStatus.entrySet()
				.iterator();
		while (entry.hasNext()) {
			entry.next().setValue(false);
		}
	}
}
