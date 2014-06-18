package mods.alice.infiniteorb.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mods.alice.infiniteorb.CommonProxy;
import mods.alice.infiniteorb.EnergyType;
import mods.alice.infiniteorb.client.gui.GuiGenerator;
import mods.alice.infiniteorb.tileentity.TileEntityGenerator;

public final class ClientProxy extends CommonProxy
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		EnergyType type;
		TileEntity generator;
		int meta;

		generator = world.getTileEntity(x, y, z);

		if(generator != null)
		{
			if(generator instanceof TileEntityGenerator)
			{
				meta = world.getBlockMetadata(x, y, z);
				type = EnergyType.getType(meta);

				if(type != null)
				{
					return new GuiGenerator(player, world, type, (TileEntityGenerator)generator);
				}
			}
		}

		return null;
	}
}
