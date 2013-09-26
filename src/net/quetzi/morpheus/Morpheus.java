package net.quetzi.morpheus;

import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.quetzi.morpheus.references.References;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION)
@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class Morpheus {
	public static int perc;
	public static boolean alertPlayers;
	public static Logger mLog = Logger.getLogger("Morpheus");

	@EventHandler
	@SideOnly(Side.SERVER)
	public void Init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new SleepEventHandler());
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void PreInit(FMLPreInitializationEvent event) {
		mLog.setParent(FMLLog.getLogger());
		mLog.info("Loading configuration");
		// Read configs
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();
		perc = config.get("settings", "SleeperPerc", 50).getInt();
		alertPlayers = config.get("settings", "AlertPlayers", true).getBoolean(true);
		config.save();
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void PostInit(FMLPostInitializationEvent event) {
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void serverLoad(FMLServerStartingEvent event) {
	}
}