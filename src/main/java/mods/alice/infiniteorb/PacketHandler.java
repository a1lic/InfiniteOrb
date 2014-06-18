package mods.alice.infiniteorb;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import mods.alice.infiniteorb.tileentity.TileEntityGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class PacketHandler
{
	@SubscribeEvent
	public void onServerPacket(ServerCustomPacketEvent event)
	{
		String channel = event.packet.channel();
		if(channel.equals("INFORB__"))
		{
			NetHandlerPlayServer playerServer = (NetHandlerPlayServer)event.handler;
			ByteBuf data = event.packet.payload();

			try
			{
				byte[] rawData = data.array();
				InputStream byteIn = new ByteArrayInputStream(rawData);
				DataInputStream streamIn = new DataInputStream(byteIn);

				doInfiniteOrb(streamIn, playerServer.playerEntity);

				streamIn.close();
				byteIn.close();
			}
			catch(IOException e)
			{
			}
		}
	}

	@SubscribeEvent
	public void onClientPacket(ClientCustomPacketEvent event)
	{
	}

	private static void doInfiniteOrb(DataInputStream stream, EntityPlayer player) throws IOException
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

			world = player.worldObj;

			entity = world.getTileEntity(nums[0], nums[1], nums[2]);
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
//					if(p == player)
//					{
//						continue;
//					}

					if(p instanceof EntityPlayerMP)
					{
						((EntityPlayerMP)p).playerNetServerHandler.sendPacket(pkt);
					}
				}
			}
		}
	}
}
