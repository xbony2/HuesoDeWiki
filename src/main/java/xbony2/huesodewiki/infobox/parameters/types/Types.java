package xbony2.huesodewiki.infobox.parameters.types;

import net.minecraft.item.*;
import xbony2.huesodewiki.api.infobox.IType;

public enum Types implements IType {
	ARMOR(10, "armor") {
		@Override
		public boolean isApplicable(ItemStack itemstack){
			return itemstack.getItem() instanceof ItemArmor;
		}
	},
	BLOCK(5, "block") {
		@Override
		public boolean isApplicable(ItemStack itemstack){
			return itemstack.getItem() instanceof ItemBlock;
		}
	},
	FOOD(10, "food") {
		@Override
		public boolean isApplicable(ItemStack itemstack){
			return itemstack.getItem() instanceof ItemFood;
		}
	},
	ITEM(0, "item") {
		@Override
		public boolean isApplicable(ItemStack itemstack){
			return true;
		}
	},
	TOOL(10, "tool") {
		@Override
		public boolean isApplicable(ItemStack itemstack){
			return itemstack.getItem() instanceof ItemTool;
		}
	},
	WEAPON(10, "weapon") {
		@Override
		public boolean isApplicable(ItemStack itemstack){
			return itemstack.getItem() instanceof ItemSword || itemstack.getItem() instanceof ItemBow;
		}
	};

	private final int priority;
	private final String name;

	Types(int priority, String name){
		this.priority = priority;
		this.name = name;
	}

	@Override
	public int getPriority(){
		return priority;
	}

	@Override
	public String getName(){
		return name;
	}
}
