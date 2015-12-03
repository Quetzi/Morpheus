package net.quetzi.morpheus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;

public class SleepChecker {

    private HashMap<Integer, Boolean> alertSent = new HashMap<Integer, Boolean>();

    @SuppressWarnings("unchecked")
    public void updatePlayerStates(World world) {

        // Iterate players and update their status
        for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
            String username = player.getCommandSenderName();
            if (player.isPlayerFullyAsleep() && !Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(username)) {
                Morpheus.playerSleepStatus.get(player.dimension).setPlayerAsleep(username);
                // Alert players that this player has gone to bed
                alertPlayers(createAlert(player.dimension, player.getDisplayName(), Morpheus.onSleepText), world);
            }
            else if (!player.isPlayerFullyAsleep() && Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(username)) {
                Morpheus.playerSleepStatus.get(player.dimension).setPlayerAwake(username);
                // Alert players that this player has woken up
                if (!world.isDaytime() && !alertSent.get(world.provider.dimensionId)) {
                    alertPlayers(createAlert(player.dimension, player.getDisplayName(), Morpheus.onWakeText), world);
                }
            }
        }
        if (areEnoughPlayersAsleep(world.provider.dimensionId)) {
            advanceToMorning(world);
        } else {
            alertSent.put(world.provider.dimensionId, false);
        }
    }

    private void alertPlayers(ChatComponentText alert, World world) {

        if ((alert != null) && (Morpheus.isAlertEnabled())) {
            for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
                player.addChatComponentMessage(alert);
            }
        }
    }

    private ChatComponentText createAlert(int dimension, String username, String text) {

        String alertText = EnumChatFormatting.WHITE + username + EnumChatFormatting.GOLD + " " + text + " "
                + Morpheus.playerSleepStatus.get(dimension).toString();
        Morpheus.mLog.info(username + " " + text + " " + Morpheus.playerSleepStatus.get(dimension).toString());
        return new ChatComponentText(alertText);
    }

    private void advanceToMorning(World world) {

        try {
            MorpheusRegistry.registry.get(world.provider.dimensionId).startNewDay();
        }
        catch (Exception e) {
            Morpheus.mLog.error("Exception caught while starting a new day for dimension " + world.provider.dimensionId);
        }

        if (!alertSent.get(world.provider.dimensionId)) {
            // Send Good morning message
            alertPlayers(new ChatComponentText(DateHandler.getMorningText()), world);
            Morpheus.playerSleepStatus.get(world.provider.dimensionId).wakeAllPlayers();
            alertSent.put(world.provider.dimensionId, true);
        }
        world.provider.resetRainAndThunder();
    }

    private boolean areEnoughPlayersAsleep(int dimension) {

        if (Morpheus.playerSleepStatus.get(dimension).getSleepingPlayers() > 0) {
            return ((dimension == 0) || (MorpheusRegistry.registry.get(dimension) != null)) && Morpheus.playerSleepStatus.get(dimension).getPercentSleeping() >= Morpheus.perc;
        }
        return false;
    }
}
