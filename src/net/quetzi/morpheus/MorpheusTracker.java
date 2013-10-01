package net.quetzi.morpheus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

public class MorpheusTracker implements IPlayerTracker {
	// IPlayerTracker implementation
	@Override
	public void onPlayerLogin(EntityPlayer player) {
		Morpheus.playerSleepStatus.get(player.dimension).setPlayerAwake(
				player.username);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		Morpheus.playerSleepStatus.get(player.dimension).removePlayer(
				player.username);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
		// Remove player from all world states
		Iterator<Entry<Integer, WorldSleepState>> entry = Morpheus.playerSleepStatus
				.entrySet().iterator();
		while (entry.hasNext()) {
			Morpheus.playerSleepStatus.get(entry.next().getKey()).removePlayer(
					player.username);
		}
		// Add player to new world state
		Morpheus.playerSleepStatus.get(player.dimension).setPlayerAwake(player.username);;
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
	}
}
