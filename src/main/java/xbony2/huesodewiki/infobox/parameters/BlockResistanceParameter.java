package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class BlockResistanceParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemBlock;
	}

	@Override
	public String parameterName(){
		return "blastresistance";
	}

	@Override
	public String parameterText(ItemStack itemstack){
		String ret;
		
		try{
			ret = Float.toString(((ItemBlock)itemstack.getItem()).getBlock().getExplosionResistance(null) * 5); //Minecraft is weird with it, don't ask
		}catch(Exception e){ //In case of a null pointer
			ret = "?";
		}
		
		return ret;
	}
}
