package mods.alice.infiniteorb;

public enum ItemList
{
	/**
	 * Infinite generator.
	 */
	GENERATOR(2630, "generator", false),
	/**
	 * Infinite EU battery.
	 */
	INFINITEORB(23461, "infiniteorb", true);

	public final short defaultID;
	public final String itemName;

	private ItemList(int id, String name, boolean isItem)
	{
		if(isItem)
		{
			defaultID = (short)(id - 256);
		}
		else
		{
			defaultID = (short)id;
		}

		itemName = name;
	}
}
