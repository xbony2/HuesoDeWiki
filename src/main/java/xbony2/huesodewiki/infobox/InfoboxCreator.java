package xbony2.huesodewiki.infobox;

import com.google.common.collect.Multimap;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.infobox.BasicConditionParameter;
import xbony2.huesodewiki.api.infobox.BasicInstanceOfParameter;
import xbony2.huesodewiki.config.Config;
import xbony2.huesodewiki.infobox.parameters.*;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class InfoboxCreator {
	public static void init(){
		HuesoDeWikiAPI.parameters.add(new NameParameter());
		HuesoDeWikiAPI.parameters.add(new ImageIconParameter());
		HuesoDeWikiAPI.parameters.add(new ModParameter());
		HuesoDeWikiAPI.parameters.add(new TypeParameter());
		//parameters.add(new OreDictNameParameter()); // TODO: tags
		//parameters.add(new RegistryNameParameter());
		//parameters.add(new UnlocalizedNameParameter()); // Disabled until issue resolved
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("blastresistance", (itemstack) -> {
			String ret;
			
			try {
				ret = Utils.floatToString(((BlockItem) itemstack.getItem()).getBlock().defaultBlockState().getExplosionResistance(null, null, null));
			}catch(NullPointerException e){
				ret = "?";
			}
			
			return ret;
		}, BlockItem.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("hardness", (itemstack) -> {
			String ret;

			try {
				ret = Utils.floatToString(((BlockItem) itemstack.getItem()).getBlock().defaultBlockState().getDestroySpeed(null, null));
			}catch(NullPointerException e){ // In case of a null pointer
				ret = "?";
			}
			
			return ret;
		}, BlockItem.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicConditionParameter("foodpoints", (itemstack) -> Integer.toString(itemstack.getItem().getFoodProperties().getNutrition()), ItemStack::isEdible));
		
		HuesoDeWikiAPI.parameters.add(new BasicConditionParameter("saturation", (itemstack) -> {
			FoodProperties food = itemstack.getItem().getFoodProperties();
			return Utils.floatToString(food.getSaturationModifier() * food.getNutrition() * 2.0F);
		}, ItemStack::isEdible));
		
		HuesoDeWikiAPI.parameters.add(new BasicConditionParameter("hunger", (itemstack) -> {
			FoodProperties food = itemstack.getItem().getFoodProperties();
			return "{{Shanks|" + food.getNutrition() + "|" + Utils.floatToString(food.getSaturationModifier()) + "}}";
		}, ItemStack::isEdible));
		
		HuesoDeWikiAPI.parameters.add(new EffectsParameter());
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("armorrating", (itemstack) -> Integer.toString(((ArmorItem) itemstack.getItem()).getDefense()), ArmorItem.class));
		HuesoDeWikiAPI.parameters.add(new ToughnessParameter());
		
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("damage", (itemstack) -> {
			Item item = itemstack.getItem();
			
			Multimap<Attribute, AttributeModifier> attributes = null;
			
			if(item instanceof DiggerItem diggerItem)
				attributes = diggerItem.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
			else if(item instanceof SwordItem swordItem)
				attributes = swordItem.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
			
			float damage = 1.0f; // default
			
			for(Attribute attribute : attributes.keySet())
				if(attribute.equals(Attributes.ATTACK_DAMAGE))
					for(AttributeModifier modifier : attributes.get(attribute))
						damage += modifier.getAmount();
			
			return Utils.floatToString(damage);
		}, DiggerItem.class, SwordItem.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("aspeed", (itemstack) -> {
			Item item = itemstack.getItem();
			
			Multimap<Attribute, AttributeModifier> attributes = null;
			
			if(item instanceof DiggerItem diggerItem)
				attributes = diggerItem.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
			else if(item instanceof SwordItem swordItem){
				attributes = swordItem.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
			}
			
			if(attributes == null)
				return "?";
			
			float speed = 4.0f; //default
			
			for(Attribute attribute : attributes.keySet())
				if(attribute.equals(Attributes.ATTACK_SPEED))
					for(AttributeModifier modifier : attributes.get(attribute))
						speed += modifier.getAmount();
			
			return String.format("%.2g", speed);
		}, DiggerItem.class, SwordItem.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicConditionParameter("durability", (itemstack) -> Utils.floatToString(itemstack.getItem().getMaxDamage(itemstack)), ItemStack::isDamageableItem));
		
		if(Config.outputEnchantability.get())
			HuesoDeWikiAPI.parameters.add(new EnchantabilityParameter());
		
		HuesoDeWikiAPI.parameters.add(new MiningLevelParameter());
		HuesoDeWikiAPI.parameters.add(new MiningSpeedParameter());
		HuesoDeWikiAPI.parameters.add(new StackableParameter());
		HuesoDeWikiAPI.parameters.add(new FlammableParameter());
		HuesoDeWikiAPI.parameters.add(new LuminanceParameter());
	}
	
	public static String createInfobox(ItemStack itemstack){
		StringBuilder ret = new StringBuilder("{{Infobox\n");
		
		HuesoDeWikiAPI.parameters.stream().filter((parameter) -> 
			parameter.canAdd(itemstack)).forEach((parameter) -> 
				ret.append('|').append(parameter.getParameterName()).append('=').append(parameter.getParameterText(itemstack)).append('\n'));
		
		ret.append("}}\n");
		return ret.toString();
	}
}
