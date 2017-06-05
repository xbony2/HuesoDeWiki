package xbony2.huesodewiki.infobox.parameters;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.api.infobox.IType;
import xbony2.huesodewiki.infobox.parameters.types.*;

public class TypeParameter implements IInfoboxParameter {
	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String getParameterName(){
		return "type";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		IType possibleType = Types.ITEM; //per default
		
		for(IType type : Types.values())
			if(type.isApplicable(itemstack) && type.getPriority() > possibleType.getPriority())
				possibleType = type;
		
		return possibleType.getName();
	}
}
