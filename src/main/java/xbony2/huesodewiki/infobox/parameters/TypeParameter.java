package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

public class TypeParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String parameterName(){
		return "type";
	}

	@Override
	public String parameterText(ItemStack itemstack){
		return itemstack.getItem() instanceof ItemBlock ? "block" : "item"; //TODO: more advanced API (this is probably okay for now, though)
	}
}
