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
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
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

		this.energyType = type;
		this.generator = gen;

		this.guiCaption = StatCollector.translateToLocal(String.format("container.%sgenerator", energyType.prefix));
		this.inventoryCaption = StatCollector.translateToLocal("container.inventory");

		this.tickAmount = StatCollector.translateToLocal(String.format("container.%stickamount", energyType.prefix));
		this.tickWait = StatCollector.translateToLocal("container.tickwait");
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		String text;
		int num[];
		boolean failed;

		num = new int[3];

		failed = false;

		text = this.outputAmount.getText();
		try
		{
			num[0] = Integer.parseInt(text);
		}
		catch(NumberFormatException e)
		{
			failed = true;
		}

		text = this.outputTick.getText();
		try
		{
			num[2] = Integer.parseInt(text);
		}
		catch(NumberFormatException e)
		{
			failed = true;
		}

		text = this.outputPerTick.getText();
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
			this.generator.updateParameters(num[0], num[1], num[2]);

			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			DataOutputStream streamOut = new DataOutputStream(byteOut);
			NetHandlerPlayClient netClient = mc.getNetHandler();
			try
			{
				streamOut.writeByte(0); // TODO: Enum
				streamOut.writeInt(this.generator.xCoord);
				streamOut.writeInt(this.generator.yCoord);
				streamOut.writeInt(this.generator.zCoord);
				streamOut.writeInt(num[0]);
				streamOut.writeInt(num[1]);
				streamOut.writeInt(num[2]);

				netClient.addToSendQueue(new C17PacketCustomPayload("INFORB__", byteOut.toByteArray()));

				streamOut.close();
				byteOut.close();
			}
			catch(IOException e)
			{
			}
		}

		this.mc.thePlayer.closeScreen();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		int xCenter, yCenter;

		GL11.glColor4f(1, 1, 1, 1);
		this.mc.renderEngine.bindTexture(gui);

		xCenter = (this.width - this.xSize) / 2;
		yCenter = (this.height - this.ySize) / 2;

		drawTexturedModalRect(xCenter, yCenter, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRendererObj.drawString(this.guiCaption, (this.xSize - this.fontRendererObj.getStringWidth(this.guiCaption)) / 2, 6, 0x404040);
		this.fontRendererObj.drawString(this.tickAmount, 55, 20, 0x404040);
		this.fontRendererObj.drawString(this.tickWait, 55, 35, 0x404040);
		this.fontRendererObj.drawString("Unit", 55, 50, 0x404040);
		this.fontRendererObj.drawString(this.inventoryCaption, 8, this.ySize - 94, 0x404040);
	}

	@Override
	public void drawScreen(int x, int y, float par3)
	{
		super.drawScreen(x, y, par3);

		GL11.glDisable(GL11.GL_LIGHTING);

		this.frameCount++;

		if((this.frameCount == 5) || (this.frameCount == 10))
		{
			for(GuiTextField f : this.textFields)
			{
				f.updateCursorCounter();
			}

			if(this.frameCount == 10)
			{
				this.frameCount = 0;
			}
		}

		for(GuiTextField f : this.textFields)
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
		List<GuiButton> _buttonList = this.buttonList;
		int xCenter, yCenter;

		super.initGui();

		xCenter = (this.width - this.xSize) / 2;
		yCenter = (this.height - this.ySize) / 2;

		num = new Integer(this.generator.outputAmount);
		this.outputAmount = new GuiTextField(this.fontRendererObj, xCenter + 10, yCenter + 20, 35, 10);
		this.outputAmount.setText(num.toString());
		this.outputAmount.setEnableBackgroundDrawing(false);

		num = new Integer(this.generator.outputTick);
		this.outputTick = new GuiTextField(this.fontRendererObj, xCenter + 10, yCenter + 35, 35, 10);
		this.outputTick.setText(num.toString());
		this.outputTick.setEnableBackgroundDrawing(false);

		num = new Integer(this.generator.outputPerTick);
		this.outputPerTick = new GuiTextField(this.fontRendererObj, xCenter + 10, yCenter + 50, 35, 10);
		this.outputPerTick.setText(num.toString());
		this.outputPerTick.setEnableBackgroundDrawing(false);

		this.applyButton = new GuiButton(1, xCenter + 125, yCenter + 45, 40, 20, "OK");

		_buttonList.add(this.applyButton);

		this.textFields.add(outputAmount);
		this.textFields.add(outputTick);
		this.textFields.add(outputPerTick);
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
		for(GuiTextField f : this.textFields)
		{
			if(f.textboxKeyTyped(keyChar, keyCode))
			{
				doKeyTyped(f, this.applyButton);
				return;
			}
		}

		super.keyTyped(keyChar, keyCode);
	}

	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		super.mouseClicked(x, y, button);

		this.outputAmount.mouseClicked(x, y, button);
		this.outputTick.mouseClicked(x, y, button);
		this.outputPerTick.mouseClicked(x, y, button);
	}
}
