package xbony2.huesodewiki.infobox;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.BasicInstanceOfParameter;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;
import xbony2.huesodewiki.infobox.parameters.*;

public class InfoboxCreator {
	public static List<IInfoboxParameter> parameters = new ArrayList();
	
	static {
		parameters.add(new NameParameter());
		parameters.add(new ImageIconParameter());
		parameters.add(new ModParameter());
		parameters.add(new TypeParameter());
		parameters.add(new BasicInstanceOfParameter("blastresistance", (itemstack) -> {
			String ret;
			
			try{
				ret = Utils.floatToString(((ItemBlock)itemstack.getItem()).getBlock().getExplosionResistance(null) * 5); //Minecraft is weird with it, don't ask
			}catch(Exception e){ //In case of a null pointer
				ret = "?";
			}
			
			return ret;
		}, ItemBlock.class));
		
		parameters.add(new BasicInstanceOfParameter("hardness", (itemstack) -> {
			String ret;
			
			try{
				ret = Utils.floatToString(((ItemBlock)itemstack.getItem()).getBlock().getBlockHardness(null, null, null));
			}catch(Exception e){ //In case of a null pointer
				ret = "?";
			}
			
			return ret;
		}, ItemBlock.class));
		
		parameters.add(new BasicInstanceOfParameter("foodpoints", (itemstack) -> Integer.toString(((ItemFood)itemstack.getItem()).getHealAmount(itemstack)), ItemFood.class));
		parameters.add(new BasicInstanceOfParameter("saturation", (itemstack) -> Utils.floatToString(((ItemFood)itemstack.getItem()).getSaturationModifier(itemstack) * ((ItemFood)itemstack.getItem()).getHealAmount(itemstack)), ItemFood.class));
		parameters.add(new BasicInstanceOfParameter("hunger", (itemstack) -> {
			ItemFood food = (ItemFood)itemstack.getItem();
			return "{{Shanks|" + Integer.toString(food.getHealAmount(itemstack)) + "|" + Utils.floatToString(food.getSaturationModifier(itemstack)) + "}}";
		}, ItemFood.class));
		
		parameters.add(new BasicInstanceOfParameter("armorrating", (itemstack) -> Integer.toString(((ItemArmor)itemstack.getItem()).damageReduceAmount), ItemArmor.class));
		parameters.add(new ToughnessParameter());
		parameters.add(new BasicInstanceOfParameter("damage", (itemstack) -> Utils.floatToString(((ItemSword)itemstack.getItem()).getDamageVsEntity() + 4.0F), ItemSword.class));
		parameters.add(new BasicInstanceOfParameter("durability", (itemstack) -> Utils.floatToString(((ItemTool)itemstack.getItem()).getMaxDamage(itemstack) + 1), ItemTool.class));
		parameters.add(new StackableParameter());
		parameters.add(new FlammableParameter());
	}
	
	public static String createInfobox(ItemStack itemstack){
		StringBuilder ret = new StringBuilder("{{Infobox\n");
		
		parameters.stream().filter((parameter) -> parameter.canAdd(itemstack)).forEach((parameter) -> ret.append('|').append(parameter.getParameterName()).append('=').append(parameter.getParameterText(itemstack)).append('\n'));
		
		ret.append("}}\n");
		return ret.toString();
	}
}
