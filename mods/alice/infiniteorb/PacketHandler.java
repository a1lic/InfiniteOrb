package mods.alice.infiniteorb;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import mods.alice.infiniteorb.tileentity.TileEntityGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
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
		InputStream byteIn;
		DataInput streamIn;

		if(packet.channel.equals("INFORB__"))
		{
			byteIn = new ByteArrayInputStream(packet.data);
			streamIn = new DataInputStream(byteIn);

			try
			{
				doInfiniteOrb(streamIn, (EntityPlayer)player);
			}
			catch(IOException e)
			{
			}
		}
	}

	private static void doInfiniteOrb(DataInput stream, EntityPlayer player) throws IOException
	{
		Packet pkt;
		TileEntity entity;
		World world;
		int num, nums[];

		num = stream.readByte();
		if(num == 0)
		{
			// Update notification from Generator GUI.
			nums = new int[6];

			for(byte i = 0; i < 6; i++)
			{
				nums[i] = stream.readInt();
			}

			world = ((EntityPlayer)player).worldObj;

			entity = world.getBlockTileEntity(nums[0], nums[1], nums[2]);
			if(entity == null)
			{
				return;
			}

			if(!(entity instanceof TileEntityGenerator))
			{
				return;
			}

			((TileEntityGenerator)entity).updateParameters(nums[3], nums[4], nums[5]);

			if(!(player instanceof EntityPlayerMP))
			{
				return;
			}

			pkt = entity.getDescriptionPacket();

			if(pkt != null)
			{
				@SuppressWarnings("unchecked")
				List<EntityPlayer> playerList = world.playerEntities;

				for(EntityPlayer p : playerList)
				{
					if(p == player)
					{
						continue;
					}

					if(p instanceof EntityPlayerMP)
					{
						((EntityPlayerMP)p).playerNetServerHandler.sendPacketToPlayer(pkt);
					}
				}
			}
		}
	}
}
