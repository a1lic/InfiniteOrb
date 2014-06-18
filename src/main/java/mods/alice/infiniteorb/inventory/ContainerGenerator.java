package mods.alice.infiniteorb.inventory;

import mods.alice.infiniteorb.tileentity.TileEntityGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;

public final class ContainerGenerator extends Container
{
	public ContainerGenerator(InventoryPlayer playerInventory, World world, TileEntityGenerator generator)
	{
		for(byte i = 0; i < 3; ++i)
		{
			for(byte j = 0; j < 9; ++j)
			{
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for(byte i = 0; i < 9; ++i)
		{
			addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
}
