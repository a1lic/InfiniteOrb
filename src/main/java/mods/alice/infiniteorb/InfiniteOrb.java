package mods.alice.infiniteorb;

import java.io.File;
import java.util.Collections;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "infiniteorb", name = "無限オーブMOD", version = "0.7")
public final class InfiniteOrb
{
	@Mod.Instance("infiniteorb")
	private static InfiniteOrb INSTANCE;

	@Deprecated
	@SidedProxy(clientSide = "mods.alice.infiniteorb.client.ClientProxy", serverSide = "mods.alice.infiniteorb.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Metadata("infiniteorb")
	public ModMetadata meta;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		File configFile = event.getSuggestedConfigurationFile();
		Configuration config = new Configuration(configFile);
		CommonProxy p = getProxy();

		ModConfig.loadConfigurations(config);

		this.meta.modId = "infiniteorb";
		this.meta.name = "無限オーブMOD";
		this.meta.description = "工業MODで使われる動力源を無限に出力する装置を追加します。"; //"_|￣|Σ・∴'､－＝≡( \u055E\u0A0A \u055E)ｳｯﾋｮｱｱｱｱｱｱｱｱｱwwwwwwwwwwwwwww";
		this.meta.url = "http://alice.mydns.jp/mc/";
		//this.meta.updateUrl = "http://alice.mydns.jp/mc/";
		//this.meta.logoFile = "";
		this.meta.version = "0.7";
		this.meta.authorList.add("alice");
		this.meta.credits = "alice";
		this.meta.parent = "Minecraft";
		this.meta.screenshots = new String[0];
		//this.meta.parentMod;
		//this.meta.childMods;
		this.meta.useDependencyInformation = false;
		this.meta.requiredMods = Collections.emptySet();
		this.meta.dependencies = Collections.emptyList();
		this.meta.dependants = Collections.emptyList();

		// falseにしないとMod listで情報が出ない。
		this.meta.autogenerated = false;

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
