package mods.alice.infiniteorb;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public final class ItemManager
{
	private static Map<Class<? extends Block>, Block> blockMap;
	private static Map<Class<? extends Item>, Item> itemMap;

	static
	{
		blockMap = new HashMap<Class<? extends Block>, Block>();
		itemMap = new HashMap<Class<? extends Item>, Item>();
	}

	public static void addBlock(Block block)
	{
		blockMap.put(block.getClass(), block);
	}

	public static void addItem(Item item)
	{
		itemMap.put(item.getClass(), item);
	}

	public static Block getBlock(Class<? extends Block> _class)
	{
		return blockMap.get(_class);
	}

	public static Item getItem(Class<? extends Item> _class)
	{
		return itemMap.get(_class);
	}
}
