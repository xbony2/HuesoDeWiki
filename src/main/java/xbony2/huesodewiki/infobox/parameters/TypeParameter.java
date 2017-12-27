package xbony2.huesodewiki.infobox.parameters;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.api.infobox.type.BasicInstanceOfType;
import xbony2.huesodewiki.api.infobox.type.IType;
import xbony2.huesodewiki.infobox.parameters.types.*;

public class TypeParameter implements IInfoboxParameter {
	public static List<IType> types = new ArrayList();
	
	static {
		types.add(new BasicInstanceOfType(5, "block", ItemBlock.class));
		types.add(new BasicInstanceOfType(10, "food", ItemFood.class));
		types.add(new ItemType());
		types.add(new BasicInstanceOfType(10, "armor", ItemArmor.class));
		types.add(new BasicInstanceOfType(10, "tool", ItemTool.class));
		types.add(new BasicInstanceOfType(10, "weapon", ItemSword.class, ItemBow.class)); //Not really comprehensive but hey
	}

	@Override
	public boolean canAdd(ItemStack itemstack){
		return true;
	}

	@Override
	public String getParameterName(){
		return "type";
	}
	
	IType possibleType = new ItemType(); //per default. Doesn't work as a local variable because lambda weirdness.

	@Override
	public String getParameterText(ItemStack itemstack){
		types.stream().filter((type) -> type.isApplicable(itemstack) && type.getPriority() > possibleType.getPriority()).forEach((type) -> possibleType = type);
		
		IType type = possibleType;
		IType possibleType = new ItemType(); //Clearing "local" variable.
		
		return type.getName();
	}
}
