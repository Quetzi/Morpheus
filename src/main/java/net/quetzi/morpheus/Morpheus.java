package net.quetzi.morpheus;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.quetzi.morpheus.commands.CommandMorpheus;
import net.quetzi.morpheus.helpers.MorpheusEventHandler;
import net.quetzi.morpheus.helpers.References;
import net.quetzi.morpheus.helpers.SleepChecker;
import net.quetzi.morpheus.world.WorldSleepState;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, dependencies = "required-after:Forge@[11.14.0.1239,);", acceptableRemoteVersions = "*")
public class Morpheus {

    public static int    perc;
    public static String onSleepText, onWakeText, onMorningText;
    public static Logger mLog;
    public static final HashMap<Integer, WorldSleepState> playerSleepStatus = new HashMap<Integer, WorldSleepState>();
    public static final SleepChecker                      checker           = new SleepChecker();
    public static       MorpheusRegistry                  register          = new MorpheusRegistry();
    private static boolean  alertEnabled;
    public static  boolean  includeMiners;
    public static  int      groundLevel;
    public static Configuration config;

    @Instance(References.MODID)
    public static  Morpheus INSTANCE;

    public static boolean isAlertEnabled() {

        return alertEnabled;
    }

    public static void setAlertPlayers(boolean state) {

        alertEnabled = state;
    }

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {

        Morpheus.mLog = event.getModLog();
        mLog.info("Loading configuration");
        // Read configs
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        perc = config.get("settings", "SleeperPerc", 50).getInt();
        alertEnabled = config.get("settings", "AlertEnabled", true).getBoolean();
        onSleepText = config.get("settings", "OnSleepText", "is now sleeping.").getString();
        onWakeText = config.get("settings", "OnWakeText", "has left their bed.").getString();
        onMorningText = config.get("settings", "OnMorningText", "Wakey, wakey, rise and shine... Good Morning everyone!").getString();
        includeMiners = config.get("settings", "IncludeMiners", true).getBoolean();
        groundLevel = config.getInt("settings", "GroundLevel", 64, 1, 255, "Ground Level (1-255)");
        config.save();

    }

    @EventHandler
    public void PostInit(FMLPostInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new MorpheusEventHandler());
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

        event.registerServerCommand(new CommandMorpheus());
    }
}
