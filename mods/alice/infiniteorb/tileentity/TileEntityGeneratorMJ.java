package mods.alice.infiniteorb.tileentity;

import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.core.SafeTimeTracker;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;

public final class TileEntityGeneratorMJ extends TileEntityGenerator implements IPowerProvider
{
	@Override
	public void updateEntity()
	{
	}

	@Override
	public void updateParameters()
	{
	}

	@Override
	public void updateParameters(int outAmount, int outPerTick, int outTick)
	{
	}

	// IPowerProvider implementations.

	@Override
	public int getLatency()
	{
		return 0;
	}

	@Override
	public int getMinEnergyReceived()
	{
		return 0;
	}

	@Override
	public int getMaxEnergyReceived()
	{
		return 0;
	}

	@Override
	public int getMaxEnergyStored()
	{
		return 0;
	}

	@Override
	public int getActivationEnergy()
	{
		return 0;
	}

	@Override
	public float getEnergyStored()
	{
		return 0;
	}

	@Override
	public void configure(int latency, int minEnergyReceived, int maxEnergyReceived, int minActivationEnergy, int maxStoredEnergy)
	{
	}

	@Override
	public void configurePowerPerdition(int powerLoss, int powerLossRegularity)
	{
	}

	@Override
	public boolean update(IPowerReceptor receptor)
	{
		return false;
	}

	@Override
	public boolean preConditions(IPowerReceptor receptor)
	{
		return false;
	}

	@Override
	public float useEnergy(float min, float max, boolean doUse)
	{
		return 0;
	}

	@Override
	public void receiveEnergy(float quantity, ForgeDirection from)
	{
	}

	@Override
	public boolean isPowerSource(ForgeDirection from)
	{
		return false;
	}

	@Override
	public SafeTimeTracker getTimeTracker()
	{
		return null;
	}
}
