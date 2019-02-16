package net.quetzi.morpheus;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.quetzi.morpheus.commands.CommandMorpheus;
import net.quetzi.morpheus.helpers.MorpheusEventHandler;
import net.quetzi.morpheus.helpers.SleepChecker;
import net.quetzi.morpheus.world.WorldSleepState;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;

@Mod(value = Morpheus.MODID)
public class Morpheus
{
    public static final String MODID = "morpheus";
    public static int    perc;
    public static String onSleepText, onWakeText, onMorningText;
    public static Logger mLog;
    public static final HashMap<Integer, WorldSleepState> playerSleepStatus = new HashMap<Integer, WorldSleepState>();
    public static final SleepChecker                      checker           = new SleepChecker();
    public static       MorpheusRegistry                  register          = new MorpheusRegistry();
    private static      boolean                           alertEnabled;
    public static       boolean                           includeMiners;
    public static       int                               groundLevel;
    public static       boolean                           setSpawnDaytime;
    public static       ModConfig                         config;

    public static Morpheus instance;

    public Morpheus() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::PreInit);
        MinecraftForge.EVENT_BUS.register(new MorpheusEventHandler());
    }

    public static boolean isAlertEnabled()
    {
        return alertEnabled;
    }

    public static void setAlertPlayers(boolean state)
    {
        alertEnabled = state;
    }

    public void PreInit(FMLCommonSetupEvent event)
    {
        Morpheus.mLog = event.getModLog();
        mLog.info("Loading configuration");
        // Read configs
        config = new ModConfig(ModConfig.Type.COMMON, ForgeConfigSpec(config, new HashMap<List<String>, String>()), Morpheus.instance);
        config.load();

        perc = config.get("settings", "SleeperPerc", 50).getInt();
        alertEnabled = config.get("settings", "AlertEnabled", true).getBoolean();
        onSleepText = config.get("settings", "OnSleepText", "is now sleeping.").getString();
        onWakeText = config.get("settings", "OnWakeText", "has left their bed.").getString();
        onMorningText = config.get("settings", "OnMorningText", "Wakey, wakey, rise and shine... Good Morning everyone!").getString();
        includeMiners = config.get("settings", "IncludeMiners", true).getBoolean();
        groundLevel = config.getInt("settings", "GroundLevel", 64, 1, 255, "Ground Level (1-255)");
        setSpawnDaytime = config.get("settings", "AllowSetSpawnDaytime", true).getBoolean();
        config.save();
    }

    @Mod.EventBusSubscriber(modid = Morpheus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandMorpheus());
    }
}
