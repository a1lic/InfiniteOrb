package mods.alice.infiniteorb;

public enum EnergyType
{
	/**
	 * IndustrialCraft² - Energy Unit
	 */
	EU(0, "eugenerator", "eu"),
	/**
	 * BuildCraft - Minecraft Jules
	 */
	MJ(1, "mjgenerator", "mj");

	public final byte metaData;
	@Deprecated
	public final String name;
	public final String prefix;

	private EnergyType(int _metaData, String _name, String _prefix)
	{
		metaData = (byte)_metaData;
		name = _name;
		prefix = _prefix;
	}

	public static EnergyType getType(int meta)
	{
		switch(meta)
		{
		case 0:
			return EU;
		case 1:
			return MJ;
		}

		return null;
	}
}
