package mods.alice.infiniteorb;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public final class ModConfig
{
	private static Map<ItemList, Short> idMap;

	public static void loadConfigurations(Configuration config)
	{
		idMap = new HashMap<ItemList, Short>();

		config.load();

		addBlockConfig(config, ItemList.GENERATOR);
		addItemConfig(config, ItemList.INFINITEORB);

		config.save();
	}

	public static short getItemID(ItemList item)
	{
		return idMap.get(item);
	}

	private static void addBlockConfig(Configuration config, ItemList item)
	{
		Property p;
		int configItemID;

		p = config.getBlock(item.itemName, item.defaultID);
		configItemID = getBlockId(p);
		idMap.put(item, (short)configItemID);
	}

	private static void addItemConfig(Configuration config, ItemList item)
	{
		Property p;
		int configItemID;

		p = config.getItem(item.itemName, item.defaultID);
		configItemID = getItemId(p);
		idMap.put(item, (short)configItemID);
	}

//	static byte getEntityId(Property p)
//	{
//		return TypeTransformer.truncateIntToUnsignedByte(Math.min(Math.max(p.getInt(), 0), 255));
//	}

	private static short getItemId(Property p)
	{
		return (short)Math.min(Math.max(p.getInt(), 4096 - 256), 32767 - 256);
	}

	private static short getBlockId(Property p)
	{
		return (short)Math.min(Math.max(p.getInt(), 1), 4095);
	}
}
