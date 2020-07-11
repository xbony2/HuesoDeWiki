package xbony2.huesodewiki.infobox.parameters.types;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.type.IType;

public class TEntityType implements IType {
	@Override
	public int getPriority(){
		return 10;
	}

	@Override
	public String getName(){
		return "tentity";
	}

	@Override
	public boolean isApplicable(ItemStack itemstack){
		return Block.getBlockFromItem(itemstack.getItem()).hasTileEntity(Utils.stackToBlockState(itemstack));
	}
}
