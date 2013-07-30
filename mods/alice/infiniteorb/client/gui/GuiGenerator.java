package mods.alice.infiniteorb.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import mods.alice.infiniteorb.EnergyType;
import mods.alice.infiniteorb.inventory.ContainerGenerator;
import mods.alice.infiniteorb.tileentity.TileEntityGenerator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public final class GuiGenerator extends GuiContainer
{
	private static ResourceLocation gui = new ResourceLocation("infiniteorb", "textures/gui/container/generator.png");
	private EnergyType energyType;
	private String guiCaption;
	private String inventoryCaption;
	private String tickAmount;
	private String tickWait;
	private GuiTextField outputAmount;
	private GuiTextField outputTick;
	private GuiTextField outputPerTick;
	private GuiButton applyButton;
	private List<GuiTextField> textFields = new ArrayList<GuiTextField>();
	private TileEntityGenerator generator;
	private byte frameCount;

	public GuiGenerator(EntityPlayer player, World world, EnergyType type, TileEntityGenerator gen)
	{
		super(new ContainerGenerator(player.inventory, world, gen));

		energyType = type;
		generator = gen;

		guiCaption = StatCollector.translateToLocal(String.format("container.%sgenerator", energyType.prefix));
		inventoryCaption = StatCollector.translateToLocal("container.inventory");

		tickAmount = StatCollector.translateToLocal(String.format("container.%stickamount", energyType.prefix));
		tickWait = StatCollector.translateToLocal("container.tickwait");
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		ByteArrayOutputStream byteOut;
		DataOutputStream streamOut;
		NetClientHandler netClient;
		Packet packet;
		String text;
		int num[];
		boolean failed;

		num = new int[3];

		failed = false;

		text = outputAmount.getText();
		try
		{
			num[0] = Integer.parseInt(text);
		}
		catch(NumberFormatException e)
		{
			failed = true;
		}

		text = outputTick.getText();
		try
		{
			num[2] = Integer.parseInt(text);
		}
		catch(NumberFormatException e)
		{
			failed = true;
		}

		text = outputPerTick.getText();
		try
		{
			num[1] = Integer.parseInt(text);
		}
		catch(NumberFormatException e)
		{
			failed = true;
		}

		if(!failed)
		{
			generator.updateParameters(num[0], num[1], num[2]);

			byteOut = new ByteArrayOutputStream();
			streamOut = new DataOutputStream(byteOut);
			netClient = mc.getNetHandler();
			try
			{
				streamOut.writeByte(0); // TODO: Enum
				streamOut.writeInt(generator.xCoord);
				streamOut.writeInt(generator.yCoord);
				streamOut.writeInt(generator.zCoord);
				streamOut.writeInt(num[0]);
				streamOut.writeInt(num[1]);
				streamOut.writeInt(num[2]);

				packet = new Packet250CustomPayload("INFORB__", byteOut.toByteArray());
				netClient.addToSendQueue(packet);
			}
			catch(IOException e)
			{
			}
		}

		mc.thePlayer.closeScreen();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		int xCenter, yCenter;

		GL11.glColor4f(1, 1, 1, 1);
		mc.renderEngine.func_110577_a(gui);

		xCenter = (width - xSize) / 2;
		yCenter = (height - ySize) / 2;

		drawTexturedModalRect(xCenter, yCenter, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		fontRenderer.drawString(guiCaption, (xSize - fontRenderer.getStringWidth(guiCaption)) / 2, 6, 0x404040);
		fontRenderer.drawString(tickAmount, 55, 20, 0x404040);
		fontRenderer.drawString(tickWait, 55, 35, 0x404040);
		fontRenderer.drawString("Unit", 55, 50, 0x404040);
		fontRenderer.drawString(inventoryCaption, 8, ySize - 94, 0x404040);
	}

	@Override
	public void drawScreen(int x, int y, float par3)
	{
		super.drawScreen(x, y, par3);

		GL11.glDisable(GL11.GL_LIGHTING);

		frameCount++;

		if((frameCount == 5) || (frameCount == 10))
		{
			for(GuiTextField f : textFields)
			{
				f.updateCursorCounter();
			}

			if(frameCount == 10)
			{
				frameCount = 0;
			}
		}

		for(GuiTextField f : textFields)
		{
			f.drawTextBox();
		}

		GL11.glEnable(GL11.GL_LIGHTING);
	}

	@Override
	public void initGui()
	{
		Integer num;
		@SuppressWarnings("unchecked")
		List<GuiButton> _buttonList = buttonList;
		int xCenter, yCenter;

		super.initGui();

		xCenter = (width - xSize) / 2;
		yCenter = (height - ySize) / 2;

		num = new Integer(generator.outputAmount);
		outputAmount = new GuiTextField(fontRenderer, xCenter + 10, yCenter + 20, 35, 10);
		outputAmount.setText(num.toString());
		outputAmount.setEnableBackgroundDrawing(false);

		num = new Integer(generator.outputTick);
		outputTick = new GuiTextField(fontRenderer, xCenter + 10, yCenter + 35, 35, 10);
		outputTick.setText(num.toString());
		outputTick.setEnableBackgroundDrawing(false);

		num = new Integer(generator.outputPerTick);
		outputPerTick = new GuiTextField(fontRenderer, xCenter + 10, yCenter + 50, 35, 10);
		outputPerTick.setText(num.toString());
		outputPerTick.setEnableBackgroundDrawing(false);

		applyButton = new GuiButton(1, xCenter + 125, yCenter + 45, 40, 20, "OK");

		_buttonList.add(applyButton);

		textFields.add(outputAmount);
		textFields.add(outputTick);
		textFields.add(outputPerTick);
	}

	private static void doKeyTyped(GuiTextField textField, GuiButton button)
	{
		String text;
		int num;
		boolean notNum;

		text = textField.getText();
		try
		{
			num = Integer.parseInt(text);
			notNum = false;
		}
		catch(NumberFormatException e)
		{
			num = 0;
			notNum = true;
		}

		if(!notNum)
		{
			if(num < 0)
			{
				notNum = true;
			}
		}

		button.enabled = !notNum;
	}

	@Override
	protected void keyTyped(char keyChar, int keyCode)
	{
		for(GuiTextField f : textFields)
		{
			if(f.textboxKeyTyped(keyChar, keyCode))
			{
				doKeyTyped(f, applyButton);
				return;
			}
		}

		super.keyTyped(keyChar, keyCode);
	}

	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		super.mouseClicked(x, y, button);

		outputAmount.mouseClicked(x, y, button);
		outputTick.mouseClicked(x, y, button);
		outputPerTick.mouseClicked(x, y, button);
	}
}
