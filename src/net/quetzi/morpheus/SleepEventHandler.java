package net.quetzi.morpheus;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SleepEventHandler {

	private int sleepingPlayers;

	@ForgeSubscribe
	@SideOnly(Side.SERVER)
	public void onPlayerSleeping(PlayerSleepInBedEvent event) {
		// Is it night?
		if (!event.entityPlayer.worldObj.isDaytime()) {
			sleepingPlayers = 1;

			// Get list of players for the dimension
			ArrayList<EntityPlayer> playerList = (ArrayList<EntityPlayer>) event.entityPlayer.worldObj.playerEntities;

			// Count Sleeping Players
			countSleepingPlayers(playerList);
			int playerCount = playerList.size();
			int percAsleep = sleepingPlayers * 100 / playerCount;

			// Construct alert text
			ChatMessageComponent chatAlert = new ChatMessageComponent();
			chatAlert.addText(EnumChatFormatting.GOLD + "Player "
					+ EnumChatFormatting.WHITE + event.entityPlayer.username
					+ EnumChatFormatting.GOLD + " is now sleeping. "
					+ sleepingPlayers + "/" + playerList.size() + " ("
					+ percAsleep + "%)");
			// Send players text alert
			if (Morpheus.alertPlayers) {
				alertPlayers(chatAlert, playerList);
			}
			// Update console
			Morpheus.mLog.info(event.entityPlayer.username + " is now asleep :"
					+ sleepingPlayers + "/" + playerCount + " " + percAsleep
					+ "%");

			// Check against config and set time
			if (percAsleep >= Morpheus.perc) {
				advanceToMorning(event.entityPlayer.worldObj.getWorldTime(),
						event.entityPlayer.worldObj);
				sleepingPlayers = 0;
			}
		} else {
			// Trying to sleep during the day
		}
	}

	private void advanceToMorning(long time, World world) {
		long ticks = time + (24000 - (time % 24000));
		world.setWorldTime(ticks);
	}

	private void alertPlayers(ChatMessageComponent alert,
			ArrayList<EntityPlayer> players) {
		// Iterate players and send them text alert
		Iterator<?> iterator = players.iterator();
		while (iterator.hasNext()) {
			EntityPlayer player = (EntityPlayer) iterator.next();
			player.sendChatToPlayer(alert);
		}
	}

	private void countSleepingPlayers(ArrayList<EntityPlayer> players) {
		// Count sleeping players
		Iterator<?> iterator = players.iterator();
		while (iterator.hasNext()) {
			EntityPlayer player = (EntityPlayer) iterator.next();
			if (player.isPlayerSleeping()) {
				sleepingPlayers++;
			}
		}
	}
}