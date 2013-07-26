package mods.alice.infiniteorb;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import mods.alice.infiniteorb.block.BlockGenerator;
import mods.alice.infiniteorb.inventory.ContainerGenerator;
import mods.alice.infiniteorb.item.ItemInfiniteOrb;
import mods.alice.infiniteorb.tileentity.TileEntityGenerator;
import mods.alice.infiniteorb.tileentity.TileEntityGeneratorEU;
import mods.alice.infiniteorb.tileentity.TileEntityGeneratorMJ;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy implements IGuiHandler
{
	public void preInit()
	{
		addBlocks();
		addItems();
		addTileEntity();

		loadLanguages();
	}

	public void init(InfiniteOrb instance)
	{
		NetworkRegistry network = NetworkRegistry.instance();
		network.registerGuiHandler(instance, this);
	}

	protected void addBlocks()
	{
		int blockID;

		blockID = ModConfig.getItemID(ItemList.GENERATOR);
		new BlockGenerator(blockID);
	}

	protected void addItems()
	{
		int itemID;

		itemID = ModConfig.getItemID(ItemList.INFINITEORB);
		new ItemInfiniteOrb(itemID);
	}

	protected void addTileEntity()
	{
		GameRegistry.registerTileEntity(TileEntityGeneratorEU.class, "GeneratorEU");
		GameRegistry.registerTileEntity(TileEntityGeneratorMJ.class, "GeneratorMJ");
	}

	protected static void loadLanguages()
	{
		InputStream languageListFile;
		InputStreamReader reader;
		LanguageRegistry lang;
		LineNumberReader lineReader;
		List<String> languageList;
		Matcher m;
		Pattern r;
		String line, matchedLine;

		lang = LanguageRegistry.instance();
		languageListFile = CommonProxy.class.getResourceAsStream("/assets/infiniteorb/lang/languages.txt");

		if(languageListFile == null)
		{
			// Load en_US when no language list.
			loadLang(lang, "en_US");
			return;
		}

		reader = new InputStreamReader(languageListFile, StandardCharsets.US_ASCII);
		lineReader = new LineNumberReader(reader);

		languageList = new ArrayList<String>();
		r = Pattern.compile("([a-z]{2}_[A-Z]{2})");

		for(;;)
		{
			try
			{
				line = lineReader.readLine();
			}
			catch(IOException e)
			{
				break;
			}

			if(line == null)
			{
				break;
			}

			if(line.length() > 0)
			{
				m = r.matcher(line);

				if(m.find())
				{
					matchedLine = m.group();

					languageList.add(matchedLine);
				}
			}
		}

		try
		{
			lineReader.close();
			reader.close();
			languageListFile.close();
		}
		catch(IOException e)
		{
		}

		if(languageList.size() > 0)
		{
			for(String langName : languageList)
			{
				loadLang(lang, langName);
			}
		}
	}

	protected static void loadLang(LanguageRegistry lang, String langName)
	{
		String langPath;

		langPath = String.format("/assets/infiniteorb/lang/%s.txt", langName);
		lang.loadLocalization(langPath, langName, false);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity generator;

		generator = world.getBlockTileEntity(x, y, z);

		if(generator != null)
		{
			if(generator instanceof TileEntityGenerator)
			{
				return new ContainerGenerator(player.inventory, world, (TileEntityGenerator)generator);
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}
