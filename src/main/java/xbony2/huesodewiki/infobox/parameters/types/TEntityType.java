package xbony2.huesodewiki.infobox.parameters.types;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EntityBlock;
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
		// "tile entities" are now called "block entities"
		return Block.byItem(itemstack.getItem()) instanceof EntityBlock;
	}
}
