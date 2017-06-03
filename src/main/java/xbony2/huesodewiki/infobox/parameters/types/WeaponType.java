package xbony2.huesodewiki.infobox.parameters.types;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import xbony2.huesodewiki.api.infobox.IType;

public class WeaponType implements IType {

	@Override
	public int getPriority(){
		return 10;
	}

	@Override
	public String getName(){
		return "weapon";
	}

	@Override
	public boolean isApplicable(ItemStack itemstack){ //This isn't comprehensive, obviously, none of these really are.
		return itemstack.getItem() instanceof ItemSword || itemstack.getItem() instanceof ItemBow;
	}
}
