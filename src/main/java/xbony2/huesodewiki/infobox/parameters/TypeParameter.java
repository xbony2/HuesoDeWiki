package xbony2.huesodewiki.infobox.parameters;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.api.infobox.IType;
import xbony2.huesodewiki.infobox.parameters.types.*;

public class TypeParameter implements IInfoboxParameter {
	public static List<IType> types = new ArrayList();
	
	static {
		types.add(new BlockType());
		types.add(new FoodType());
		types.add(new ItemType());
	}

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
		IType possibleType = new ItemType(); //per default
		
		for(IType type : types)
			if(type.isApplicable(itemstack) && type.getPriority() > possibleType.getPriority())
				possibleType = type;
		
		return possibleType.getName();
	}
}
