package mods.alice.infiniteorb.creativetab;

import mods.alice.infiniteorb.ItemManager;
import mods.alice.infiniteorb.item.ItemInfiniteOrb;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public final class CreativeTabInfiniteOrb extends CreativeTabs
{
	public static final CreativeTabs INSTANCE = new CreativeTabInfiniteOrb();

	public CreativeTabInfiniteOrb()
	{
		super("infiniteorb");
	}

	@Override
	public Item getTabIconItem()
	{
		return ItemManager.getItem(ItemInfiniteOrb.class);
	}
}
