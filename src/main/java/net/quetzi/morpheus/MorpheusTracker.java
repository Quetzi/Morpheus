package net.quetzi.morpheus;

import java.util.Iterator;
import java.util.Map.Entry;

import net.quetzi.morpheus.world.WorldSleepState;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class MorpheusTracker {
	@SubscribeEvent
	public void loggedInEvent(PlayerLoggedInEvent event) {
		if (Morpheus.playerSleepStatus.get(event.player.dimension) == null) {
			Morpheus.playerSleepStatus.put(event.player.dimension, new WorldSleepState(event.player.dimension));
		}
		Morpheus.playerSleepStatus.get(event.player.dimension).setPlayerAwake(event.player.getDisplayName());
	}

	@SubscribeEvent
	public void loggedOutEvent(PlayerLoggedOutEvent event) {
		Morpheus.playerSleepStatus.get(event.player.dimension).removePlayer(event.player.getDisplayName());
	}

	@SubscribeEvent
	public void changedDimensionEvent(PlayerChangedDimensionEvent event) {
		if (Morpheus.playerSleepStatus.get(event.player.dimension) == null) {
			Morpheus.playerSleepStatus.put(event.player.dimension, new WorldSleepState(event.player.dimension));
		}
		// Remove player from all world states
		Iterator<Entry<Integer, WorldSleepState>> entry = Morpheus.playerSleepStatus.entrySet().iterator();
		while (entry.hasNext()) {
			Morpheus.playerSleepStatus.get(entry.next().getKey()).removePlayer(event.player.getDisplayName());
		}
		// Add player to new world state
		Morpheus.playerSleepStatus.get(event.player.dimension).setPlayerAwake(event.player.getDisplayName());
	}
}
