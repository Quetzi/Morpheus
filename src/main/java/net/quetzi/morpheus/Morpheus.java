package net.quetzi.morpheus;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.quetzi.morpheus.commands.CommandMorpheus;
import net.quetzi.morpheus.helpers.Config;
import net.quetzi.morpheus.helpers.MorpheusEventHandler;
import net.quetzi.morpheus.helpers.SleepChecker;
import net.quetzi.morpheus.world.WorldSleepState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(value = Morpheus.MODID)
public class Morpheus {
    public static final String MODID = "morpheus";
    public static Morpheus instance;
    public static Logger logger = LogManager.getLogger(MODID);

    public static final HashMap<RegistryKey<World>, WorldSleepState> playerSleepStatus = new HashMap<RegistryKey<World>, WorldSleepState>();
    public static final SleepChecker checker = new SleepChecker();
    public static MorpheusRegistry register = new MorpheusRegistry();

    public Morpheus() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MorpheusEventHandler());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
    }

    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        CommandMorpheus.register(event.getServer().getCommandManager().getDispatcher());
    }
}
