package net.quetzi.morpheus.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SleepState {
    //List of all players sleeping right now in all Worlds
    private static List<EntityPlayer> sleepingPlayers = new ArrayList<EntityPlayer>();

	public static int getPercentSleeping(World world) {
		return (getSleepingPlayers(world) * 100) / world.playerEntities.size();
	}

	private static int getSleepingPlayers(World world) {
        int asleepCount = 0;
        for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
            if (player.isPlayerFullyAsleep())
                asleepCount++;
        }
		return asleepCount;
	}

    public static String getSleepText(World world) {
        return getSleepingPlayers(world) + "/" + world.playerEntities.size() + " (" + getPercentSleeping(world) + "%)";
    }

    public static boolean wasPlayerSleeping(EntityPlayer player) {
        return sleepingPlayers.contains(player);
    }

    public static void addSleepingPlayer(EntityPlayer player) {
        sleepingPlayers.add(player);
    }

    public static void removeSleepingPlayer(EntityPlayer player) {
        sleepingPlayers.remove(player);
    }
}
