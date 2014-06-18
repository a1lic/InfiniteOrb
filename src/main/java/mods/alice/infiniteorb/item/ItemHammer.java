package mods.alice.infiniteorb.item;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.alice.infiniteorb.ItemManager;
import mods.alice.infiniteorb.creativetab.CreativeTabInfiniteOrb;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public final class ItemHammer extends Item
{
	public ItemHammer()
	{
		super();
		this.setCreativeTab(CreativeTabInfiniteOrb.INSTANCE);
		this.setFull3D();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("hammer");

		ItemManager.addItem(this);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		ForgeDirection opposite;
		ForgeDirection sideDirection;
		PowerHandler.PowerReceiver h;
		TileEntity tile;

		if(world.isRemote)
		{
			// If return true, onItemUseFirst on server side will not invoked.
			return false;
		}

		tile = world.getTileEntity(x, y, z);
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
	public void registerIcons(IIconRegister iconReg)
	{
		itemIcon = iconReg.registerIcon("infiniteorb:hammer");
	}
}
