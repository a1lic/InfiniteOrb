package mods.alice.infiniteorb.item.block;

import mods.alice.infiniteorb.EnergyType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public final class ItemBlockGenerator extends ItemBlockWithMetadata
{
	public ItemBlockGenerator(int id, Block block)
	{
		super(id, block);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		int metaData;

		metaData = itemStack.getItemDamage();

		if(metaData == EnergyType.EU.metaData)
		{
			return "tile.eugenerator";
		}

		if(metaData == EnergyType.MJ.metaData)
		{
			return "tile.mjgenerator";
		}

		return getUnlocalizedName();
	}
}
