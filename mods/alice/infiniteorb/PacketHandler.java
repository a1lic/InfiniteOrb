package mods.alice.infiniteorb;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import mods.alice.infiniteorb.tileentity.TileEntityGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public final class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		ByteArrayInputStream byteIn;
		DataInputStream streamIn;
		TileEntity entity;
		World world;
		int num, nums[];

		if(packet.channel.equals("INFORB__"))
		{
			byteIn = new ByteArrayInputStream(packet.data);
			streamIn = new DataInputStream(byteIn);

			try
			{
				num = streamIn.readByte();
				if(num == 0)
				{
					// Update notification from Generator GUI.
					nums = new int[6];

					for(byte i = 0; i < 6; i++)
					{
						nums[i] = streamIn.readInt();
					}

					world = ((EntityPlayer)player).worldObj;

					entity = world.getBlockTileEntity(nums[0], nums[1], nums[2]);
					if(entity != null)
					{
						if(entity instanceof TileEntityGenerator)
						{
							((TileEntityGenerator)entity).updateParameters(nums[3], nums[4], nums[5]);
						}
					}
				}
			}
			catch(IOException e)
			{
			}
		}
	}
}
