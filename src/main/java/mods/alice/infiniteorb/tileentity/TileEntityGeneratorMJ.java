package mods.alice.infiniteorb.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;

public final class TileEntityGeneratorMJ extends TileEntityGenerator implements IPowerEmitter
{
	private static ForgeDirection sides[] = new ForgeDirection[6];
	private static ForgeDirection oSides[] = new ForgeDirection[6];
	private TileEntity tiles[] = new TileEntity[6];
	private byte tick;

	static
	{
		sides[0] = ForgeDirection.SOUTH;
		sides[1] = ForgeDirection.NORTH;
		sides[2] = ForgeDirection.EAST;
		sides[3] = ForgeDirection.WEST;
		sides[4] = ForgeDirection.DOWN;
		sides[5] = ForgeDirection.UP;

		oSides[0] = ForgeDirection.NORTH;
		oSides[1] = ForgeDirection.SOUTH;
		oSides[2] = ForgeDirection.WEST;
		oSides[3] = ForgeDirection.EAST;
		oSides[4] = ForgeDirection.UP;
		oSides[5] = ForgeDirection.DOWN;
	}

	public TileEntityGeneratorMJ()
	{
		outputAmount = 32;
		outputPerTick = 32;
		outputTick = 0;
		tickToNextOut = 0;

		updateParameters();
	}

	@Override
	public void updateEntity()
	{
		PowerReceiver h;
		TileEntity tile;
		double currentPower, maxPower, amountToSupply, powerPerTick;

		if(worldObj.isRemote)
		{
			return;
		}

		if(tick >= 10)
		{
			// Check tile entities around this block.
			// NORTH
			tiles[0] = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
			// SOUTH
			tiles[1] = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
			// WEST
			tiles[2] = worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
			// EAST
			tiles[3] = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
			// UP
			tiles[4] = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
			// BOTTOM
			tiles[5] = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);

			tick = 0;
		}
		else if(tick < 0)
		{
			tick = 0;
		}
		else
		{
			tick++;
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

			for(byte i = 0; i < 6; i++)
			{
				tile = tiles[i];

				if(tile == null)
				{
					continue;
				}
				if(!(tile instanceof IPowerReceptor))
				{
					continue;
				}

				h = ((IPowerReceptor)tile).getPowerReceiver(oSides[i]);

				if(h == null)
				{
					continue;
				}

				currentPower = h.getEnergyStored();
				maxPower = h.getMaxEnergyStored();
				powerPerTick = h.getMaxEnergyReceived();

				if(currentPower >= maxPower)
				{
					continue;
				}

				amountToSupply = maxPower - currentPower;

				if(amountToSupply > outputAmount)
				{
					amountToSupply = outputAmount;
				}

				if(powerPerTick > this.outputPerTick)
				{
					powerPerTick = outputPerTick;
				}

				try
				{
					for(; amountToSupply >= powerPerTick; amountToSupply -= powerPerTick)
					{
						h.receiveEnergy(PowerHandler.Type.ENGINE, powerPerTick, sides[i]);
					}

					if(amountToSupply > 0)
					{
						h.receiveEnergy(PowerHandler.Type.ENGINE, amountToSupply, sides[i]);
					}
				}
				catch(Exception e)
				{
				}
			}
		}
		else
		{
			tickToNextOut++;
		}
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
	}

	// IPowerEmitter implementations.

	@Override
	public boolean canEmitPowerFrom(ForgeDirection side)
	{
		return true;
	}
}
