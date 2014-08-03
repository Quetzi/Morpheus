package net.quetzi.morpheus;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
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
            if (event.phase == TickEvent.Phase.END) {
                // This is called every tick, do something every 20 ticks
                if (event.world.getWorldTime() % 20L == 10) {
                    if (event.world.playerEntities.size() > 0) {
                        if (Morpheus.playerSleepStatus.get(event.world.provider.dimensionId) == null) {
                            Morpheus.playerSleepStatus.put(event.world.provider.dimensionId, new WorldSleepState(event.world.provider.dimensionId));
                        }
                        Morpheus.checker.updatePlayerStates(event.world);
                    } else {
                        Morpheus.playerSleepStatus.remove(event.world.provider.dimensionId);
                    }
                }
            }
        }
    }
}
