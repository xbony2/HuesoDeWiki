package xbony2.huesodewiki.infobox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
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
		parameters.add(new OreDictNameParameter());
		parameters.add(new RegistryNameParameter());
		parameters.add(new UnlocalizedNameParameter());
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
		parameters.add(new BasicInstanceOfParameter("damage", (itemstack) -> {
			Item item = itemstack.getItem();
			if(item instanceof ItemTool){
				try{
					Field field = ItemTool.class.getDeclaredField("attackDamage");
					field.setAccessible(true);
					return Utils.floatToString(field.getFloat((ItemTool)item) + 1.0f);
				}catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException e){//Do not complain or you will be smote.
					e.printStackTrace();
				}
			}else if(item instanceof ItemSword){
				Multimap<String, AttributeModifier> multimap = ((ItemSword)item).getItemAttributeModifiers(EntityEquipmentSlot.MAINHAND);
				float damage = 1.0f; //default
				for(String name : multimap.keySet())
					if(name == SharedMonsterAttributes.ATTACK_DAMAGE.getName())
						for(AttributeModifier modifier : multimap.get(name))
							damage += modifier.getAmount();
				return Utils.floatToString(damage);
			}
			return "?";
		}, ItemTool.class, ItemSword.class));
		parameters.add(new BasicInstanceOfParameter("aspeed", (itemstack) -> {
			Item item = itemstack.getItem();
			if(item instanceof ItemTool){
				try{
					Field field = ItemTool.class.getDeclaredField("attackSpeed");
					field.setAccessible(true);
					return String.format("%.2g", field.getFloat((ItemTool)item) + 4.0f);
				}catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException e){//Do not complain or you will be smote.
					e.printStackTrace();
				}
			}else if(item instanceof ItemSword){
				Multimap<String, AttributeModifier> multimap = ((ItemSword)item).getItemAttributeModifiers(EntityEquipmentSlot.MAINHAND);
				float speed = 4.0f; //default
				for(String name : multimap.keySet())
					if(name == SharedMonsterAttributes.ATTACK_SPEED.getName())
						for(AttributeModifier modifier : multimap.get(name))
							speed += modifier.getAmount();
				
				return String.format("%.2g", speed);
			}
			return "?";
		}, ItemTool.class, ItemSword.class));
		parameters.add(new BasicInstanceOfParameter("durability", (itemstack) -> Utils.floatToString(((ItemTool)itemstack.getItem()).getMaxDamage(itemstack) + 1), ItemTool.class));
		parameters.add(new StackableParameter());
		parameters.add(new FlammableParameter());
		parameters.add(new LuminanceParameter());
	}
	
	public static String createInfobox(ItemStack itemstack){
		StringBuilder ret = new StringBuilder("{{Infobox\n");
		
		parameters.stream().filter((parameter) -> parameter.canAdd(itemstack)).forEach((parameter) -> ret.append('|').append(parameter.getParameterName()).append('=').append(parameter.getParameterText(itemstack)).append('\n'));
		
		ret.append("}}\n");
		return ret.toString();
	}
}
