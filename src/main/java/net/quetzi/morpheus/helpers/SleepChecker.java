package net.quetzi.morpheus.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.MorpheusRegistry;

import java.util.HashMap;

public class SleepChecker {
    private HashMap<RegistryKey<World>, Boolean> alertSent = new HashMap<>();

    public void updatePlayerStates(World world) {
        //Don't bother updating if there is only 1 player
        if (world.getPlayers().size() > 1) {
            // Iterate players and update their status
            for (PlayerEntity player : world.getPlayers()) {
                String username = player.getGameProfile().getName();
                RegistryKey<World> playerWorld = player.getEntityWorld().func_234923_W_();
                if (player.isPlayerFullyAsleep() && !Morpheus.playerSleepStatus.get(playerWorld).isPlayerSleeping(username)) {
                    Morpheus.playerSleepStatus.get(playerWorld).setPlayerAsleep(username);
                    // Alert players that this player has gone to bed
                    alertPlayers(createAlert(playerWorld, player.getDisplayName().getString(), Config.SERVER.onSleepText.get()), world);
                } else if (!player.isPlayerFullyAsleep() && Morpheus.playerSleepStatus.get(playerWorld).isPlayerSleeping(username)) {
                    Morpheus.playerSleepStatus.get(playerWorld).setPlayerAwake(username);
                    // Alert players that this player has woken up
                    if (!world.isDaytime() && !alertSent.get(world.func_234923_W_())) {
                        alertPlayers(createAlert(playerWorld, player.getDisplayName().getString(), Config.SERVER.onWakeText.get()), world);
                    }
                }
            }
            if (areEnoughPlayersAsleep(world.func_234923_W_())) {
                if (!alertSent.containsKey(world.func_234923_W_())) {
                    alertSent.put(world.func_234923_W_(), false);
                }
                advanceToMorning(world);
            } else {
                alertSent.put(world.func_234923_W_(), false);
            }
        }
    }

    private void alertPlayers(StringTextComponent alert, World world) {
        if ((alert != null) && (Config.SERVER.alertEnabled.get())) {
            for (PlayerEntity player : world.getPlayers()) {
                player.sendMessage(alert, player.getUniqueID());
            }
        }
    }

    private StringTextComponent createAlert(RegistryKey<World> dimension, String username, String text) {
        Morpheus.logger.info(String.format("%s %s %s", username, text, Morpheus.playerSleepStatus.get(dimension).toString()));
        return new StringTextComponent(String.format("%s%s%s %s %s", TextFormatting.WHITE, username, TextFormatting.GOLD, text, Morpheus.playerSleepStatus.get(dimension).toString()));
    }

    private void advanceToMorning(World world) {
        try {
            MorpheusRegistry.registry.get(world.func_234923_W_()).startNewDay();
        } catch (Exception e) {
            Morpheus.logger.error("Exception caught while starting a new day for dimension " + world.func_234923_W_());
            e.printStackTrace();
        }
        if (!alertSent.get(world.func_234923_W_())) {
            // Send Good morning message
            alertPlayers(new StringTextComponent(DateHandler.getMorningText()), world);
            Morpheus.playerSleepStatus.get(world.func_234923_W_()).wakeAllPlayers();
            alertSent.put(world.func_234923_W_(), true);
        }
        if (world.isRaining()) {
            world.setRainStrength(0);
        }
    }

    private boolean areEnoughPlayersAsleep(RegistryKey<World> dimension) {
        if (Morpheus.playerSleepStatus.get(dimension).getSleepingPlayers() > 0) {
            return ((MorpheusRegistry.registry.get(dimension) != null) && Morpheus.playerSleepStatus.get(dimension).getPercentSleeping() >= Config.SERVER.perc.get());
        }
        return false;
    }
}
