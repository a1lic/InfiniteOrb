package mods.alice.infiniteorb.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.alice.infiniteorb.EnergyType;
import mods.alice.infiniteorb.InfiniteOrb;
import mods.alice.infiniteorb.ItemList;
import mods.alice.infiniteorb.ItemManager;
import mods.alice.infiniteorb.item.block.ItemBlockGenerator;
import mods.alice.infiniteorb.tileentity.TileEntityGenerator;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public final class BlockGenerator extends BlockContainer
{
	private static final ItemList item = ItemList.GENERATOR;
	@SideOnly(Side.CLIENT)
	private Icon iconEU;
	@SideOnly(Side.CLIENT)
	private Icon iconMJ;

	public BlockGenerator(int id)
	{
		super(id, Material.iron);

		disableStats();
		setCreativeTab(CreativeTabs.tabDecorations);
		setHardness(2);

		ItemManager.addBlock(this);
		GameRegistry.registerBlock(this, ItemBlockGenerator.class, item.itemName);
	}

	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
	{
		EntityItem drop;
		ItemStack itemDrop;
		int metaData;
		boolean removed;

		metaData = world.getBlockMetadata(x, y, z);
		removed = super.removeBlockByPlayer(world, player, x, y, z);
		if(removed)
		{
			if(!player.capabilities.isCreativeMode)
			{
				if(!world.isRemote)
				{
					itemDrop = new ItemStack(blockID, 1, metaData);
					drop = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, itemDrop);

					world.spawnEntityInWorld(drop);
				}
			}
		}

		return removed;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int metaData)
	{
		if(metaData == EnergyType.EU.metaData)
		{
			return TileEntityGenerator.createTile(world, EnergyType.EU);
		}
		if(metaData == EnergyType.MJ.metaData)
		{
			return TileEntityGenerator.createTile(world, EnergyType.MJ);
		}

		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metaData)
	{
		if(metaData == EnergyType.EU.metaData)
		{
			return iconEU;
		}
		if(metaData == EnergyType.MJ.metaData)
		{
			return iconMJ;
		}

		return null;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int metaData;

		metaData = world.getBlockMetadata(x, y, z);
		return new ItemStack(blockID, 1, metaData);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int id, CreativeTabs tabs, @SuppressWarnings("rawtypes") List _list)
	{
		ItemStack itemStack;
		@SuppressWarnings("unchecked")
		List<ItemStack> list = (List<ItemStack>)_list;

		itemStack = new ItemStack(id, 1, EnergyType.EU.metaData);
		list.add(itemStack);

		itemStack = new ItemStack(id, 1, EnergyType.MJ.metaData);
		list.add(itemStack);
	}

	@Override
	public int idDropped(int metaData, Random random, int fortuneLevel)
	{
		return 0;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py, float pz)
	{
//		TileEntity tileEntity;

		if(!world.isRemote)
		{
//			tileEntity = world.getBlockTileEntity(x, y, z);
			player.openGui(InfiniteOrb.getInstance(), 0, world, x, y, z);
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		iconEU = iconRegister.registerIcon("infiniteorb:eugenerator");
		iconMJ = iconRegister.registerIcon("infiniteorb:mjgenerator");
	}
}
