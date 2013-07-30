package mods.alice.infiniteorb.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.EventBus;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySource;

public final class TileEntityGeneratorEU extends TileEntityGenerator implements IEnergyEmitter, IEnergySource
{
	private EnergyTileSourceEvent emitEvent;
	private EnergyTileSourceEvent emitEventRemaind;
	private boolean addedToEnergyNet = false;
	private int separates;
	private int remain;

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
		if(outputPerTick > 0)
		{
			separates = outputAmount / outputPerTick;
			remain = outputAmount % outputPerTick;

			emitEvent = new EnergyTileSourceEvent(this, outputPerTick);

			if(remain > 0)
			{
				emitEventRemaind = new EnergyTileSourceEvent(this, remain);
			}
			else
			{
				emitEventRemaind = null;
			}
		}
		else
		{
			emitEvent = null;
			emitEventRemaind = null;
		}
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

		if(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
		{
			if(tickToNextOut < 0)
			{
				tickToNextOut = 0;
			}

			if(tickToNextOut >= outputTick)
			{
				tickToNextOut = 0;

				if(separates > 0)
				{
					// Emit EU!
					for(int i = 0; i < separates; i++)
					{
						try
						{
							event = new EnergyTileSourceEvent(this, outputPerTick);
							bus.post(event);
						}
						catch(IllegalArgumentException e)
						{
						}
					}
				}

				if(remain > 0)
				{
					try
					{
						event = new EnergyTileSourceEvent(this, remain);
						bus.post(event);
					}
					catch(IllegalArgumentException e)
					{
					}
				}
			}
			else
			{
				tickToNextOut++;
			}
		}
	}

	@Override
	public void validate()
	{
		super.validate();
	}

	// IEnergyTile implementation.

	@Override
	public boolean isAddedToEnergyNet()
	{
		return addedToEnergyNet;
	}

	// IEnergyEmitter implementation.

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
	{
		return true;
	}

	// IEnergySource implementation.

	@Override
	public int getMaxEnergyOutput()
	{
		return outputPerTick;
	}
}
