package net.quetzi.morpheus;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.quetzi.morpheus.commands.CommandMorpheus;
import net.quetzi.morpheus.references.References;
import net.quetzi.morpheus.world.WorldSleepState;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, acceptableRemoteVersions = "*")
public class Morpheus {

    public static int    perc;
    public static String onSleepText, onWakeText, onMorningText;
    public static Logger mLog;
    public static HashMap<Integer, WorldSleepState> playerSleepStatus = new HashMap<Integer, WorldSleepState>();
    public static SleepChecker                      checker           = new SleepChecker();
    public static MorpheusRegistry                  register          = new MorpheusRegistry();
    private static boolean alertEnabled;
    public static  boolean includeMiners;
    public static  int     groundLevel;
    public static boolean setSpawn;

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {

        Morpheus.mLog = event.getModLog();
        mLog.info("Loading configuration");
        String cat = "settings";

        // Read configs
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        perc = config.get(cat, "SleeperPerc", 50).getInt();
        alertEnabled = config.get(cat, "AlertEnabled", true).getBoolean();
        onSleepText = config.get(cat, "OnSleepText", "is now sleeping.").getString();
        onWakeText = config.get(cat, "OnWakeText", "has left their bed.").getString();
        onMorningText = config.get(cat, "OnMorningText", "Wakey, wakey, rise and shine... Good Morning everyone!").getString();
        includeMiners = config.get(cat, "IncludeMiners", true).getBoolean();
        groundLevel = config.getInt(cat, "GroundLevel", 64, 1, 255, "Ground Level (1-255)");
        setSpawn = config.get(cat, "SetSpawnDuringDay", true, "Set to true to enable beds to set players respawn point when they can't sleep").getBoolean();

        config.save();

    }

    @EventHandler
    public void PostInit(FMLPostInitializationEvent event) {

        FMLCommonHandler.instance().bus().register(new MorpheusEventHandler());
        MinecraftForge.EVENT_BUS.register(new MorpheusEventHandler());
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

        event.registerServerCommand(new CommandMorpheus());
    }

    public static boolean isAlertEnabled() {

        return alertEnabled;
    }

    public static void setAlertPlayers(boolean state) {

        alertEnabled = state;
    }
}
