package net.quetzi.morpheus.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.server.MinecraftServer;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.world.WorldSleepState;

public class MorpheusEventHandler {

    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {

        if (!MinecraftServer.getServer().worldServerForDimension(event.player.dimension).isRemote) {
            if (Morpheus.playerSleepStatus.get(event.player.dimension) == null) {
                Morpheus.playerSleepStatus.put(event.player.dimension, new WorldSleepState(event.player.dimension));
            }
            Morpheus.playerSleepStatus.get(event.player.dimension).setPlayerAwake(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void loggedOutEvent(PlayerLoggedOutEvent event) {

        if (!MinecraftServer.getServer().worldServerForDimension(event.player.dimension).isRemote) {
            if (Morpheus.playerSleepStatus.get(event.player.dimension) == null) { return; }
            Morpheus.playerSleepStatus.get(event.player.dimension).removePlayer(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void changedDimensionEvent(PlayerChangedDimensionEvent event) {

        if (!MinecraftServer.getServer().worldServerForDimension(event.player.dimension).isRemote) {
            if (Morpheus.playerSleepStatus.get(event.toDim) == null) {
                Morpheus.playerSleepStatus.put(event.toDim, new WorldSleepState(event.toDim));
            }
            // Remove player from old World state
            if (Morpheus.playerSleepStatus.get(event.fromDim) != null) {
                Morpheus.playerSleepStatus.get(event.fromDim).removePlayer(event.player.getGameProfile().getName());
            }
            // Add player to new world state
            Morpheus.playerSleepStatus.get(event.toDim).setPlayerAwake(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void worldTickEvent(WorldTickEvent event) {

        if (!event.world.isRemote) {
            // This is called every tick, do something every 20 ticks
            if (event.world.getWorldTime() % 20L == 10 && event.phase == TickEvent.Phase.END) {
                if (event.world.playerEntities.size() > 0) {
                    if (Morpheus.playerSleepStatus.get(event.world.provider.getDimensionId()) == null) {
                        Morpheus.playerSleepStatus.put(event.world.provider.getDimensionId(), new WorldSleepState(event.world.provider.getDimensionId()));
                    }
                    Morpheus.checker.updatePlayerStates(event.world);
                } else {
                    Morpheus.playerSleepStatus.remove(event.world.provider.getDimensionId());
                }
            }
        }
    }

    @SubscribeEvent
    public void playerSleepInBed(PlayerSleepInBedEvent event) {
        if (event.result != EntityPlayer.EnumStatus.OK && Morpheus.setSpawn) {
            event.entityPlayer.setSpawnChunk(event.entityPlayer.playerLocation, false, event.entityPlayer.dimension);
            event.entityPlayer.addChatMessage(new ChatComponentText("Your spawn point has been updated"));
        }
    }
}
