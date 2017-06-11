package net.quetzi.morpheus.helpers;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.world.WorldSleepState;

public class MorpheusEventHandler
{
    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event)
    {
        if (!event.player.getEntityWorld().getMinecraftServer().getWorld(event.player.dimension).isRemote)
        {
            if (!Morpheus.playerSleepStatus.containsKey(event.player.dimension))
            {
                Morpheus.playerSleepStatus.put(event.player.dimension, new WorldSleepState(event.player.dimension));
            }
            Morpheus.playerSleepStatus.get(event.player.dimension).setPlayerAwake(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void loggedOutEvent(PlayerLoggedOutEvent event)
    {
        if (!event.player.getEntityWorld().getMinecraftServer().getWorld(event.player.dimension).isRemote)
        {
            if (Morpheus.playerSleepStatus.get(event.player.dimension) != null)
            {
                Morpheus.playerSleepStatus.get(event.player.dimension).removePlayer(event.player.getGameProfile().getName());
            }
        }
    }

    @SubscribeEvent
    public void changedDimensionEvent(PlayerChangedDimensionEvent event)
    {
        if (!event.player.getEntityWorld().getMinecraftServer().getWorld(event.player.dimension).isRemote)
        {
            if (!Morpheus.playerSleepStatus.containsKey(event.toDim))
            {
                Morpheus.playerSleepStatus.put(event.toDim, new WorldSleepState(event.toDim));
            }
            // Remove player from old World state
            if (Morpheus.playerSleepStatus.get(event.fromDim) != null)
            {
                Morpheus.playerSleepStatus.get(event.fromDim).removePlayer(event.player.getGameProfile().getName());
            }
            // Add player to new world state
            Morpheus.playerSleepStatus.get(event.toDim).setPlayerAwake(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void worldTickEvent(WorldTickEvent event)
    {
        if (!event.world.isRemote)
        {
            // This is called every tick, do something every 20 ticks
            if (event.world.getWorldTime() % 20L == 10 && event.phase == TickEvent.Phase.END)
            {
                if (event.world.playerEntities.size() > 0)
                {
                    if (!Morpheus.playerSleepStatus.containsKey(event.world.provider.getDimension()))
                    {
                        Morpheus.playerSleepStatus.put(event.world.provider.getDimension(), new WorldSleepState(event.world.provider.getDimension()));
                    }
                    Morpheus.checker.updatePlayerStates(event.world);
                }
                else
                {
                    Morpheus.playerSleepStatus.remove(event.world.provider.getDimension());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void bedClicked(PlayerInteractEvent.RightClickBlock event)
    {
        if (Morpheus.setSpawnDaytime)
        {
            EntityPlayer player = event.getEntityPlayer();
            BlockPos     pos    = event.getPos();
            if (!event.getWorld().isRemote && event.getWorld().isDaytime() && !event.getEntityPlayer().isSneaking())
            {
                if (player.getBedLocation() == null || player.getBedLocation().getDistance(pos.getX(), pos.getY(), pos.getZ()) > 4)
                {
                    IBlockState state = event.getWorld().getBlockState(pos);
                    if (state.getBlock() instanceof BlockBed)
                    {
                        if (state.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD)
                        {
                            pos = event.getPos().offset(state.getValue(BlockBed.FACING));
                            state = event.getWorld().getBlockState(pos);
                            if (!(state.getBlock() instanceof BlockBed) || state.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD)
                            {
                                return;
                            }
                        }
                        if (event.getWorld().provider.canRespawnHere() && event.getWorld().provider.getBiomeForCoords(pos) != Biomes.HELL)
                        {
                            if (!state.getValue(BlockBed.OCCUPIED))
                            {
                                player.setSpawnPoint(event.getEntityPlayer().getPosition(), false);
                                player.setSpawnChunk(pos, false, event.getWorld().provider.getDimension());
                                player.sendMessage(new TextComponentString(References.SPAWN_SET));
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
