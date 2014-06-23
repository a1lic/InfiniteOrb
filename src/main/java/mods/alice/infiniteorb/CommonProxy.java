package mods.alice.infiniteorb;

import ic2.api.item.IC2Items;

import java.util.List;

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
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler
{
	public void preInit()
	{
		addBlocks();
		addItems();
		addTileEntity();
	}

	public void init(InfiniteOrb instance)
	{
		NetworkRegistry network = NetworkRegistry.INSTANCE;
		network.registerGuiHandler(instance, this);

		FMLEventChannel event = NetworkRegistry.INSTANCE.newEventDrivenChannel("INFORB__");
		event.register(new PacketHandler());

		addRecipes();
	}

	protected void addBlocks()
	{
		new BlockGenerator();
	}

	protected void addItems()
	{
		new ItemInfiniteOrb();
		new ItemHammer();
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

		requireItems[1] = new ItemStack(Block.getBlockFromName("beacon"), 1, 0);

		requireItems[0] = IC2Items.getItem("iridiumPlate");
		if(requireItems[0].getItem() != null)
		{
			requireItems[2] = requireItems[0];
			requireItems[3] = IC2Items.getItem("elemotor");
			requireItems[4] = IC2Items.getItem("mfsUnit");
			requireItems[5] = requireItems[3];
			requireItems[6] = IC2Items.getItem("advancedMachine");
			requireItems[7] = IC2Items.getItem("hvTransformer");
			requireItems[8] = requireItems[6];
		}
		else
		{
			requireItems[0] = new ItemStack((Item)Item.itemRegistry.getObject("diamond"), 1, 0);

			requireItems[2] = requireItems[0];
			requireItems[3] = new ItemStack(Block.getBlockFromName("redstone_torch"), 1, 0);
			requireItems[4] = new ItemStack(Block.getBlockFromName("glowstone"), 1, 0);
			requireItems[5] = new ItemStack(Block.getBlockFromName("torch"), 1, 0);
			requireItems[6] = new ItemStack(Block.getBlockFromName("diamond_block"), 1, 0);
			requireItems[7] = new ItemStack(Block.getBlockFromName("gold_block"), 1, 0);
			requireItems[8] = requireItems[6];
		}

		recipe = new ShapedRecipes(3, 3, requireItems, craftItem);
		recipeList.add(recipe);

		craftItem = new ItemStack(craftBlock, 1, 1);
		requireItems = new ItemStack[9];
		requireItems[1] = new ItemStack(Block.getBlockFromName("beacon"), 1, 0);

		requireItems[0] = new ItemStack(Block.getBlockFromName("laserBlock"), 1, 0);
		if(requireItems[0].getItem() != null)
		{
			requireItems[2] = requireItems[0];
			requireItems[3] = new ItemStack((Item)Item.itemRegistry.getObject("diamondGearItem"), 1, 0);
			requireItems[4] = requireItems[0];
			requireItems[5] = requireItems[3];
			requireItems[6] = new ItemStack((Item)Item.itemRegistry.getObject("redstoneChipset"), 1, 3);
			requireItems[7] = requireItems[4];
			requireItems[8] = requireItems[6];
		}
		else
		{
			requireItems[0] = new ItemStack((Item)Item.itemRegistry.getObject("diamond"), 1, 0);

			requireItems[2] = requireItems[0];
			requireItems[3] = new ItemStack(Block.getBlockFromName("torch"), 1, 0);
			requireItems[4] = new ItemStack(Block.getBlockFromName("glowstone"), 1, 0);
			requireItems[5] = new ItemStack(Block.getBlockFromName("redstone_torch"), 1, 0);
			requireItems[6] = new ItemStack(Block.getBlockFromName("diamond_block"), 1, 0);
			requireItems[7] = new ItemStack(Block.getBlockFromName("gold_block"), 1, 0);
			requireItems[8] = requireItems[6];
		}

		recipe = new ShapedRecipes(3, 3, requireItems, craftItem);
		recipeList.add(recipe);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity generator;

		generator = world.getTileEntity(x, y, z);

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
