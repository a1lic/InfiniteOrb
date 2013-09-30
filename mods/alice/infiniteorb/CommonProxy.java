package mods.alice.infiniteorb;

import ic2.api.item.Items;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import mods.alice.infiniteorb.block.BlockGenerator;
import mods.alice.infiniteorb.inventory.ContainerGenerator;
import mods.alice.infiniteorb.item.ItemHammer;
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

		addRecipes();
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

		itemID = ModConfig.getItemID(ItemList.HAMMER);
		new ItemHammer(itemID);
	}

	protected void addTileEntity()
	{
		GameRegistry.registerTileEntity(TileEntityGeneratorEU.class, "GeneratorEU");
		GameRegistry.registerTileEntity(TileEntityGeneratorMJ.class, "GeneratorMJ");
	}

	protected void addRecipes()
	{
		Block craftBlock;
		CraftingManager manager;
		IRecipe recipe;
		ItemStack craftItem;
		ItemStack requireItems[];

		manager = CraftingManager.getInstance();
		assert(manager != null);
		@SuppressWarnings("unchecked")
		List<IRecipe> recipeList = manager.getRecipeList();

		craftBlock = ItemManager.getBlock(BlockGenerator.class);
		craftItem = new ItemStack(craftBlock, 1, 0);
		requireItems = new ItemStack[9];

		requireItems[1] = new ItemStack(Block.beacon, 1, 0);

		requireItems[0] = Items.getItem("iridiumPlate");
		if(requireItems[0] != null)
		{
			requireItems[2] = requireItems[0];
			requireItems[3] = Items.getItem("elemotor");
			requireItems[4] = Items.getItem("mfsUnit");
			requireItems[5] = requireItems[3];
			requireItems[6] = Items.getItem("advancedMachine");
			requireItems[7] = Items.getItem("hvTransformer");
			requireItems[8] = requireItems[6];
		}
		else
		{
			requireItems[0] = new ItemStack(Item.diamond, 1, 0);

			requireItems[2] = requireItems[0];
			requireItems[3] = new ItemStack(Block.torchRedstoneActive, 1, 0);
			requireItems[4] = new ItemStack(Block.glowStone, 1, 0);
			requireItems[5] = requireItems[3];
			requireItems[6] = new ItemStack(Block.blockDiamond, 1, 0);
			requireItems[7] = new ItemStack(Block.blockGold, 1, 0);
			requireItems[8] = requireItems[6];
		}

		recipe = new ShapedRecipes(3, 3, requireItems, craftItem);
		recipeList.add(recipe);

//		craftItem = new ItemStack(craftBlock, 1, 1);
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
