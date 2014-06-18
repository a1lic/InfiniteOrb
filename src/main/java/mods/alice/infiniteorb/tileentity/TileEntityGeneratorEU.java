package mods.alice.infiniteorb.tileentity;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventBus;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;

public final class TileEntityGeneratorEU extends TileEntityGenerator implements IEnergySource
{
	private boolean addedToEnergyNet = false;
	private double storage;

	public TileEntityGeneratorEU()
	{
		outputAmount = 32;
		outputPerTick = 32;
		outputTick = 0;
		tickToNextOut = 0;

		updateParameters();
	}

	@Override
	public void updateParameters()
	{
	}

	@Override
	public void updateParameters(int outAmount, int outPerTick, int outTick)
	{
		outputAmount = outAmount;
		outputPerTick = outPerTick;
		outputTick = outTick;

		updateParameters();
	}

	@Override
	public void invalidate()
	{
		onChunkUnload();
		super.invalidate();
	}

	@Override
	public void onChunkUnload()
	{
		Event event;
		EventBus bus;

		if(!worldObj.isRemote)
		{
			if(addedToEnergyNet)
			{
				bus = MinecraftForge.EVENT_BUS;
				event = new EnergyTileUnloadEvent(this);

				bus.post(event);

				addedToEnergyNet = false;
			}
		}
	}

	@Override
	public void updateEntity()
	{
		Event event;
		EventBus bus;

		if(worldObj.isRemote)
		{
			return;
		}

		bus = MinecraftForge.EVENT_BUS;

		if(!addedToEnergyNet)
		{
			event = new EnergyTileLoadEvent(this);
			bus.post(event);
			addedToEnergyNet = true;
		}

		if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
		{
			return;
		}

		if(tickToNextOut < 0)
		{
			tickToNextOut = 0;
		}

		if(tickToNextOut >= outputTick)
		{
			tickToNextOut = 0;

			if(outputPerTick <= 0)
			{
				return;
			}

			if(storage < outputAmount)
			{
				storage += outputAmount;
			}
		}
		else
		{
			tickToNextOut++;
		}
	}

	@Override
	public void validate()
	{
		super.validate();
	}

	// IEnergyEmitter implementation.

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
	{
		return true;
	}

	// IEnergySource implementation.

	@Override
	public double getOfferedEnergy()
	{
		if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
		{
			return 0;
		}

		if(storage < 1)
		{
			return 0;
		}

		return (double)Math.min(outputAmount, outputPerTick);
	}

	@Override
	public void drawEnergy(double amount)
	{
		if(storage >= amount)
		{
			storage -= amount;
		}
	}
}
