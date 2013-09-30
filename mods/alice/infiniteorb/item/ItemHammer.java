package mods.alice.infiniteorb.item;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.alice.infiniteorb.ItemManager;
import mods.alice.infiniteorb.creativetab.CreativeTabInfiniteOrb;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public final class ItemHammer extends Item
{
	public ItemHammer(int itemID)
	{
		super(itemID - 256);
		setCreativeTab(CreativeTabInfiniteOrb.INSTANCE);
		setFull3D();
		setMaxStackSize(1);
		setUnlocalizedName("hammer");

		ItemManager.addItem(this);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		ForgeDirection opposite;
		ForgeDirection sideDirection;
		PowerReceiver h;
		TileEntity tile;

		if(world.isRemote)
		{
			// If return true, onItemUseFirst on server side will not invoked.
			return false;
		}

		tile = world.getBlockTileEntity(x, y, z);
		if(tile == null)
		{
			return false;
		}

		if(!(tile instanceof IPowerReceptor))
		{
			return false;
		}

		sideDirection = ForgeDirection.getOrientation(side);
		opposite = sideDirection.getOpposite();

		h = ((IPowerReceptor)tile).getPowerReceiver(sideDirection);

		if(h == null)
		{
			return false;
		}

		if(!world.isRemote)
		{
			h.receiveEnergy(PowerHandler.Type.ENGINE, 1, opposite);
		}

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg)
	{
		itemIcon = iconReg.registerIcon("infiniteorb:hammer");
	}
}
