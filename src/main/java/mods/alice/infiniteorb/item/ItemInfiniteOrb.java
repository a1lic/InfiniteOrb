package mods.alice.infiniteorb.item;

import java.util.List;

import ic2.api.info.IEnergyValueProvider;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.alice.infiniteorb.ItemManager;
import mods.alice.infiniteorb.creativetab.CreativeTabInfiniteOrb;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class ItemInfiniteOrb extends Item implements ISpecialElectricItem, IElectricItemManager, IEnergyValueProvider
{
	public ItemInfiniteOrb()
	{
		super();
		setCreativeTab(CreativeTabInfiniteOrb.INSTANCE);
//		setMaxStackSize(1);
		setUnlocalizedName("infiniteorb");

		ItemManager.addItem(this);
	}

	/* IEnergyValueProvider implementations */

	@Override
	public int getEnergyValue(ItemStack itemStack)
	{
		return Integer.MAX_VALUE;
	}

	/* End of IEenergyValueProvider implementations */

	/* ISpecialElectricItem implementations */

	@Override
	public IElectricItemManager getManager(ItemStack paramItemStack)
	{
		return this;
	}

	/* End of ISpecialElectricItem implementations */

	/* IElectricItemManager implementations */

	@Override
	public int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
	{
		int limit;

		limit = getTransferLimit(itemStack);
		if(amount > limit)
		{
			if(ignoreTransferLimit)
			{
				return amount;
			}
			else
			{
				return limit;
			}
		}

		return amount;
	}

	@Override
	public int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
	{
		int limit;

		limit = getTransferLimit(itemStack);
		if(amount > limit)
		{
			if(ignoreTransferLimit)
			{
				return amount;
			}
			else
			{
				return limit;
			}
		}

		return amount;
	}

	@Override
	public int getCharge(ItemStack itemStack)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean canUse(ItemStack itemStack, int amount)
	{
		return true;
	}

	@Override
	public boolean use(ItemStack itemStack, int amount, EntityLivingBase entity)
	{
		return true;
	}

	@Override
	public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity)
	{
	}

	@Override
	public String getToolTip(ItemStack itemStack)
	{
		return "This orb have infinite EU!";
	}

	/* End of IElectricItemManager implementations */

	/* IElectricItem implementations */

	@Override
	public boolean canProvideEnergy(ItemStack itemStack)
	{
		return true;
	}

	@Override
	public Item getChargedItem(ItemStack itemStack)
	{
		return this;
	}

	@Override
	public Item getEmptyItem(ItemStack itemStack)
	{
		return this;
	}

	@Override
	public int getMaxCharge(ItemStack itemStack)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public int getTier(ItemStack itemStack)
	{
		NBTTagCompound tag;

		tag = itemStack.getTagCompound();
		if(tag == null)
		{
			return 1;
		}

		return tag.getByte("Tier");
	}

	@Override
	public int getTransferLimit(ItemStack itemStack)
	{
		return 65536;
	}

	/* End of IElectricItem implementations */

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, @SuppressWarnings("rawtypes") List info, boolean detailMode)
	{
		@SuppressWarnings("unchecked")
		List<String> _list = (List<String>)info;
		NBTTagCompound tag;
		byte tier;

		tag = itemStack.getTagCompound();
		if(tag == null)
		{
			tier = 1;
		}
		else
		{
			tier = tag.getByte("Tier");
		}

		_list.add(String.format("Tier: %d", tier));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item id, CreativeTabs tab, @SuppressWarnings("rawtypes") List list)
	{
		ItemStack i;
		NBTTagCompound tag;
		@SuppressWarnings("unchecked")
		List<ItemStack> _list = (List<ItemStack>)list;

		i = new ItemStack(id, 1, 1);
		tag = new NBTTagCompound();
		tag.setByte("Tier", (byte)1);
		i.setTagCompound(tag);
		_list.add(i);

		i = new ItemStack(id, 1, 1);
		tag = new NBTTagCompound();
		tag.setByte("Tier", (byte)2);
		i.setTagCompound(tag);
		_list.add(i);

		i = new ItemStack(id, 1, 1);
		tag = new NBTTagCompound();
		tag.setByte("Tier", (byte)3);
		i.setTagCompound(tag);
		_list.add(i);

		i = new ItemStack(id, 1, 1);
		tag = new NBTTagCompound();
		tag.setByte("Tier", (byte)4);
		i.setTagCompound(tag);
		_list.add(i);
	}

	@Override
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity)
	{
		return (armorType == 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconReg)
	{
		itemIcon = iconReg.registerIcon("infiniteorb:infiniteorb");
	}
}
