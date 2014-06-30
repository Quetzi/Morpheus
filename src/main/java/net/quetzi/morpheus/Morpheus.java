package net.quetzi.morpheus;

import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;
import net.quetzi.morpheus.commands.CommandMorpheus;
import net.quetzi.morpheus.commands.CommandVersion;
import net.quetzi.morpheus.references.References;
import net.quetzi.morpheus.world.WorldSleepState;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, acceptableRemoteVersions = "*")
public class Morpheus {

    public static int                               perc;
    public static String                            onSleepText, onWakeText, onMorningText, spawnSetText;
    public static Logger                            mLog;
    public static HashMap<Integer, WorldSleepState> playerSleepStatus = new HashMap<Integer, WorldSleepState>();
    public static SleepChecker                      checker           = new SleepChecker();
    private static boolean                          alertEnabled;

    public static boolean isAlertEnabled() {

        return alertEnabled;
    }

    public static void setAlertPlayers(boolean state) {

        alertEnabled = state;
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {

    }

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {

        Morpheus.mLog = event.getModLog();
        mLog.info("Loading configuration");
        // Read configs
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        perc = config.get("settings", "SleeperPerc", 50).getInt();
        alertEnabled = config.get("settings", "AlertEnabled", true).getBoolean(true);
        onSleepText = config.get("settings", "OnSleepText", "is now sleeping.").getString();
        onWakeText = config.get("settings", "OnWakeText", "has left their bed.").getString();
        onMorningText = config.get("settings", "OnMorningText", "Wakey, wakey, rise and shine... Good Morning everyone!").getString();
        config.save();
        MorpheusRegistry register = new MorpheusRegistry();
    }

    @EventHandler
    public void PostInit(FMLPostInitializationEvent event) {

        FMLCommonHandler.instance().bus().register(new MorpheusEventHandler());
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

        event.registerServerCommand(new CommandMorpheus());
        event.registerServerCommand(new CommandVersion());
    }

}
