package mods.alice.infiniteorb.creativetab;

import mods.alice.infiniteorb.ItemList;
import mods.alice.infiniteorb.ModConfig;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class CreativeTabInfiniteOrb extends CreativeTabs
{
	public static final CreativeTabs INSTANCE = new CreativeTabInfiniteOrb();
	private final ItemStack icon;

	public CreativeTabInfiniteOrb()
	{
		super("infiniteorb");
		icon = new ItemStack(ModConfig.getItemID(ItemList.INFINITEORB), 1, 0);
	}

	@Override
	public ItemStack getIconItemStack()
	{
		if(icon.getItem() == null)
		{
			icon.itemID = ModConfig.getItemID(ItemList.INFINITEORB);
		}

		return icon;
	}
}
