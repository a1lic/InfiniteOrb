package mods.alice.infiniteorb;

import java.io.File;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "InfiniteOrb", version = "0.1")
@NetworkMod(clientSideRequired = true,  packetHandler = PacketHandler.class, channels = {"INFORB__"})
public final class InfiniteOrb
{
	@Mod.Instance("InfiniteOrb")
	private static InfiniteOrb INSTANCE;

	@Deprecated
	@SidedProxy(clientSide = "mods.alice.infiniteorb.client.ClientProxy", serverSide = "mods.alice.infiniteorb.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
//	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		File configFile = event.getSuggestedConfigurationFile();
		Configuration config = new Configuration(configFile);
		CommonProxy p = getProxy();

		ModConfig.loadConfigurations(config);

		p.preInit();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		CommonProxy p = getProxy();

		p.init(INSTANCE);
	}

	public static InfiniteOrb getInstance()
	{
		return INSTANCE;
	}

	public static CommonProxy getProxy()
	{
		return proxy;
	}
}
