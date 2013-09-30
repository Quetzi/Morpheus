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
		if (Morpheus.playerSleepStatus.get(player.dimension) == null) {
			Morpheus.playerSleepStatus.put(player.dimension, new HashMap<String, Boolean>());
		}
		Morpheus.playerSleepStatus.get(player.dimension).put(player.username,
				false);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		Morpheus.playerSleepStatus.get(player.dimension)
				.remove(player.username);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
		Iterator<Entry<Integer,HashMap<String,Boolean>>> entry = Morpheus.playerSleepStatus.entrySet().iterator();
		while(entry.hasNext()) {
			try {
			Morpheus.playerSleepStatus.get(entry.next().getKey()).remove(player.username);
			}
			catch (Exception e) {
				Morpheus.mLog.info("Caught error removing player from world state:" + e.getMessage());
			}
		}
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
	}
}
