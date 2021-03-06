package com.bioxx.tfc.Items.ItemBlocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import com.bioxx.tfc.Core.TFC_Time;
import com.bioxx.tfc.TileEntities.TEOilLamp;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCFluids;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.ISmeltable;

public class ItemOilLamp extends ItemTerraBlock implements ISmeltable
{
	public ItemOilLamp(Block par1)
	{
		super(par1);
		this.MetaNames = new String[]{"Gold", "Platinum", "Rose Gold", "Silver", "Sterling Silver", "Blue Steel"};
	}

	@Override
	public int getDisplayDamage(ItemStack is)
	{
		FluidStack fuel = FluidStack.loadFluidStackFromNBT(is.getTagCompound());
		return fuel.amount;
	}

	@Override
	public boolean isDamaged(ItemStack is)
	{
		if (is.hasTagCompound())
			return true;
		else
			return false;
	}

	@Override
	public int getMaxDamage(ItemStack is)
	{
		return 1000;
	}

	@Override
	public EnumSize getSize(ItemStack is) {
		return EnumSize.SMALL;
	}

	@Override
	public EnumWeight getWeight(ItemStack is) {
		return EnumWeight.LIGHT;
	}

	@Override
	public int getItemStackLimit(ItemStack is)
	{
		return 1;
	}

	@Override
	public short GetMetalReturnAmount(ItemStack is) {

		return 100;
	}

	@Override
	public boolean isSmeltable(ItemStack is) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public EnumTier GetSmeltTier(ItemStack is) {
		// TODO Auto-generated method stub
		return EnumTier.TierI;
	}

	@Override
	public Metal GetMetalType(ItemStack is) 
	{
		int meta = is.getItemDamage();
		switch(meta)
		{
		case 1: return Global.GOLD;
		case 2: return Global.PLATINUM;
		case 3: return Global.ROSEGOLD;
		case 4: return Global.SILVER;
		case 5: return Global.STERLINGSILVER;
		case 6: return Global.BLUESTEEL;
		default : return Global.UNKNOWN;
		}
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		int xCoord = x; int yCoord = y; int zCoord = z;
		if (side == 0) --zCoord;
		if (side == 1) ++zCoord;
		if (side == 2) --zCoord;
		if (side == 3) ++zCoord;
		if (side == 4) --xCoord;
		if (side == 5) ++xCoord;
		Block block = world.getBlock(xCoord, yCoord, zCoord);
		if(world.isAirBlock(xCoord, yCoord, zCoord))
		{
			if(super.onItemUse(is, player, world, x, y, z, side, hitX, hitY, hitZ))
			{
				TileEntity _t =  world.getTileEntity(xCoord, yCoord, zCoord);
				if(_t != null && _t instanceof TEOilLamp)
				{
					((TEOilLamp)_t).fuel = FluidStack.loadFluidStackFromNBT(is.getTagCompound());
					((TEOilLamp)_t).hourPlaced = (int)TFC_Time.getTotalHours();
				}
			}
		}

		return false;
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		super.addInformation(is, player, arraylist, flag);
		if(is.hasTagCompound())
		{
			FluidStack fs = FluidStack.loadFluidStackFromNBT(is.getTagCompound());
			arraylist.add((fs.amount/(1000/TFC_Time.daysInYear))+" Days Remaining ("+(fs.amount/1000f)+")");
		}
	}

	public static ItemStack GetFullLamp(int meta)
	{
		ItemStack is = new ItemStack(TFCBlocks.OilLamp, 1, meta);
		FluidStack fs = new FluidStack(TFCFluids.OLIVEOIL, 1000);
		is.setTagCompound(fs.writeToNBT(new NBTTagCompound()));
		return is;
	}
}
