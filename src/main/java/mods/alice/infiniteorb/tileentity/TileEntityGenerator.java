package mods.alice.infiniteorb.tileentity;

import mods.alice.infiniteorb.EnergyType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TileEntityGenerator extends TileEntity
{
	public int outputAmount;
	public int outputPerTick;
	public int outputTick;
	protected int tickToNextOut;

	public static TileEntity createTile(World world, EnergyType type)
	{
		TileEntity tile;

		switch(type)
		{
		case EU:
			tile = new TileEntityGeneratorEU();
			break;
		case MJ:
			tile = new TileEntityGeneratorMJ();
			break;
		default:
			tile = null;
			break;
		}

		if(tile != null)
		{
			tile.setWorldObj(world);
		}

		return tile;
	}

	public abstract void updateParameters();

	public abstract void updateParameters(int outAmount, int outPerTick, int outTick);

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag;

		tag = new NBTTagCompound();
		writeToNBT(tag);

		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		if((pkt.func_148856_c() == xCoord) && (pkt.func_148855_d() == yCoord) && (pkt.func_148854_e() == zCoord) && (pkt.func_148853_f() == 1))
		{
			NBTTagCompound tag = pkt.func_148857_g();
			if(tag != null)
			{
				try
				{
					readFromNBT(tag);
				}
				catch(Exception e)
				{
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		outputAmount = tag.getInteger("PowerTotal");
		outputPerTick = tag.getInteger("PowerPerTick");
		outputTick = tag.getInteger("Tick");
		tickToNextOut = tag.getInteger("WaitCount");

		updateParameters();
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		tag.setInteger("PowerTotal", outputAmount);
		tag.setInteger("PowerPerTick", outputPerTick);
		tag.setInteger("Tick", outputTick);
		tag.setInteger("WaitCount", tickToNextOut);
	}
}
