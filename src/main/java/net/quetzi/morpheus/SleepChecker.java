package net.quetzi.morpheus;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.quetzi.morpheus.api.IMorpheusListener;
import net.quetzi.morpheus.api.MorpheusAPI;
import net.quetzi.morpheus.world.WorldSleepState;

public class SleepChecker {
    
    public void updatePlayerStates(World world) {
    
        // Iterate players and update their status
        for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
            String username = player.getGameProfile().getName();
            if (player.isPlayerFullyAsleep() && !Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(username)) {
                Morpheus.playerSleepStatus.get(player.dimension).setPlayerAsleep(username);
                // Alert players that this player has gone to bed
                alertPlayers(createAlert(player.dimension, username, Morpheus.onSleepText), world);
            } else if (!player.isPlayerFullyAsleep() && Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(username)) {
                Morpheus.playerSleepStatus.get(player.dimension).setPlayerAwake(username);
                // Alert players that this player has woken up
                if (!world.isDaytime()) {
                    alertPlayers(createAlert(player.dimension, username, Morpheus.onWakeText), world);
                }
            }
        }
        if (areEnoughPlayersAsleep(world.provider.dimensionId)) {
            advanceToMorning(world);
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
    
        String alertText = EnumChatFormatting.GOLD + "Player " + EnumChatFormatting.WHITE + username + EnumChatFormatting.GOLD + " " + text + " "
                + Morpheus.playerSleepStatus.get(dimension).toString();
        
        WorldSleepState state = Morpheus.playerSleepStatus.get(dimension);
        
        for (IMorpheusListener listener : MorpheusAPI.getRegistry().getListeners(dimension)) {
            listener.onPlayerSleep(dimension, username, state);
        }
        
        Morpheus.mLog.info("Player " + username + " " + text + " " + state.toString());
        return new ChatComponentText(alertText);
    }
    
    private void advanceToMorning(World world) {
    
        if (world.provider.dimensionId == 0) {
            world.setWorldTime(world.getWorldTime() + getTimeToSunrise(world));
            
            for (IMorpheusListener listener : MorpheusAPI.getRegistry().getListeners(world.provider.dimensionId)) {
                listener.onSkipNight(world.provider.dimensionId);
            }
        } else {
            try {
                MorpheusAPI.getRegistry().getHandler(world.provider.dimensionId).startNewDay(world.provider.dimensionId);
                
                try {
                    for (IMorpheusListener listener : MorpheusAPI.getRegistry().getListeners(world.provider.dimensionId))
                        listener.onSkipNight(world.provider.dimensionId);
                } catch (Exception ex) {
                }
            } catch (Exception e) {
                Morpheus.mLog.error("Exception caught while starting a new day for dimension " + world.provider.dimensionId);
            }
        }
        // Send Good morning message
        alertPlayers(new ChatComponentText(EnumChatFormatting.GOLD + Morpheus.onMorningText), world);
        world.provider.resetRainAndThunder();
    }
    
    private long getTimeToSunrise(World world) {
    
        long dayLength = 24000;
        return dayLength - (world.getWorldTime() % dayLength);
    }
    
    private boolean areEnoughPlayersAsleep(int dimension) {
    
        if ((dimension == 0) || (MorpheusAPI.getRegistry().getHandler(dimension) != null)) { return Morpheus.playerSleepStatus.get(dimension)
                .getPercentSleeping() >= Morpheus.perc; }
        return false;
    }
}
