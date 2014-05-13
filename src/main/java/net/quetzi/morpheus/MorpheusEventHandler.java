package net.quetzi.morpheus;

import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.quetzi.morpheus.references.References;
import net.quetzi.morpheus.world.WorldSleepState;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class MorpheusEventHandler {
    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {
        if (Morpheus.playerSleepStatus.get(event.player.dimension) == null) {
            Morpheus.playerSleepStatus.put(event.player.dimension, new WorldSleepState(
                    event.player.dimension));
        }
        Morpheus.playerSleepStatus.get(event.player.dimension).setPlayerAwake(
                event.player.getCommandSenderName());
    }

    @SubscribeEvent
    public void loggedOutEvent(PlayerLoggedOutEvent event) {
        if (Morpheus.playerSleepStatus.get(event.player.dimension) == null) {
            return;
        }
        Morpheus.playerSleepStatus.get(event.player.dimension).removePlayer(
                event.player.getCommandSenderName());
        // Remove player from all world states
        // String username = event.player.getCommandSenderName();
        // Iterator<Entry<Integer, WorldSleepState>> entry =
        // Morpheus.playerSleepStatus
        // .entrySet().iterator();
        // while (entry.hasNext()) {
        // Morpheus.playerSleepStatus.get(entry.next().getKey()).removePlayer(
        // username);
        // }
    }

    @SubscribeEvent
    public void changedDimensionEvent(PlayerChangedDimensionEvent event) {
        if (Morpheus.playerSleepStatus.get(event.toDim) == null) {
            Morpheus.playerSleepStatus.put(event.toDim, new WorldSleepState(event.toDim));
        }
        // Remove player from old World state
        Morpheus.playerSleepStatus.get(event.fromDim).removePlayer(
                event.player.getCommandSenderName());
        // Add player to new world state
        Morpheus.playerSleepStatus.get(event.toDim).setPlayerAwake(
                event.player.getCommandSenderName());
    }

    @SubscribeEvent
    public void worldTickEvent(WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            // This is called every tick, do something every 20 ticks
            if (event.world.getWorldTime() % 20L == 10) {
                if (event.world.playerEntities.size() > 0) {
                    if (Morpheus.playerSleepStatus.get(event.world.provider.dimensionId) == null) {
                        Morpheus.playerSleepStatus.put(event.world.provider.dimensionId,
                                new WorldSleepState(event.world.provider.dimensionId));
                    }
                    Morpheus.checker.updatePlayerStates(event.world);
                } else {
                    Morpheus.playerSleepStatus.remove(event.world.provider.dimensionId);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void playerSleepInBedEvent(PlayerSleepInBedEvent event) {
        if (event.result == EnumStatus.NOT_POSSIBLE_NOW) {
            event.entityPlayer.setSpawnChunk(event.entityPlayer.playerLocation, false, event.entityPlayer.dimension);
            event.entityPlayer.addChatMessage(new ChatComponentText(Morpheus.spawnSetText));
        }
    }
}
