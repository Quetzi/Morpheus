package net.quetzi.morpheus;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SleepEventHandler implements IPlayerTracker {

	private int sleepingPlayers;

	@ForgeSubscribe
	@SideOnly(Side.SERVER)
	public void onPlayerSleeping(PlayerSleepInBedEvent event) {
		// Is it night?
		if (!event.entityPlayer.worldObj.isDaytime()) {
			sleepCheck(event.entityPlayer, "is now asleep", true);
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

	private int countSleepingPlayers(EntityPlayer playerTrigger, boolean forced) {
		ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) playerTrigger.worldObj.playerEntities;
		if(forced) {
			sleepingPlayers = 1;
		}
		else {
			sleepingPlayers = 0;
		}
		// Count sleeping players
		for (EntityPlayer player : players) {
			if (player.isPlayerSleeping()) {
				sleepingPlayers++;
			}
		}
		return sleepingPlayers * 100 / players.size();
	}

	private boolean areEnoughPlayersAsleep(int percAsleep, EntityPlayer player) {
		if (percAsleep >= Morpheus.perc) {
			return true;
		}
		return false;
	}

	private ChatMessageComponent createAlert(EntityPlayer player, String text,
			int percAsleep) {
		ChatMessageComponent chatAlert = new ChatMessageComponent();
		chatAlert.addText(EnumChatFormatting.GOLD + "Player "
				+ EnumChatFormatting.WHITE + player.username
				+ EnumChatFormatting.GOLD + " " + text + ". " + sleepingPlayers
				+ "/" + player.worldObj.playerEntities.size() + " ("
				+ percAsleep + "%)");
		return chatAlert;
	}

	public void sleepCheck(EntityPlayer player, String reason, boolean forced) {
		int percAsleep = countSleepingPlayers(player, forced);
		if (percAsleep > 0 || forced) {
			alertPlayers(createAlert(player, reason, percAsleep),
					(ArrayList<EntityPlayer>) player.worldObj.playerEntities);
			// Update console
			Morpheus.mLog.info(player.username + " " + reason + ":"
					+ sleepingPlayers + "/"
					+ player.worldObj.playerEntities.size());
			// If enough players are asleep set it to daytime
			if (areEnoughPlayersAsleep(percAsleep, player)) {
				advanceToMorning(player.worldObj.getWorldTime(),
						player.worldObj);
				sleepingPlayers = 0;
			}
		}
	}
	@Override
	public void onPlayerLogin(EntityPlayer player) {
		sleepCheck(player,"logged in", false);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		sleepCheck(player,"logged out", false);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
		sleepCheck(player,"left this world", false);
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
	}
}