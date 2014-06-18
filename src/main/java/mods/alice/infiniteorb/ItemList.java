package mods.alice.infiniteorb;

public enum ItemList
{
	/**
	 * Infinite generator.
	 */
	GENERATOR("generator"),
	/**
	 * Infinite EU battery.
	 */
	INFINITEORB("infiniteorb"),
	/**
	 * MJ Hammer
	 */
	HAMMER("hammer");

	public final String itemName;

	private ItemList(String name)
	{
		itemName = name;
	}
}
