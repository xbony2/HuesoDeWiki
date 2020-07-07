package xbony2.huesodewiki.infobox;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.infobox.BasicConditionParameter;
import xbony2.huesodewiki.api.infobox.BasicInstanceOfParameter;
import xbony2.huesodewiki.infobox.parameters.*;

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
				// Forge recommends not using getExplosionResistance, but it's not clear what the alternative is.
				// Possibly requires a BlockState which we don't have since this is an item.
				//ret = Utils.floatToString(((BlockItem) itemstack.getItem()).getBlock().getExplosionResistance() * 5);
				ret = Utils.floatToString(((BlockItem) itemstack.getItem()).getBlock().getDefaultState().getExplosionResistance(null, null, null) * 5); // XXX
			}catch(NullPointerException e){ // In case of a null pointer
				ret = "?";
			}
			
			return ret;
		}, BlockItem.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("hardness", (itemstack) -> {
			String ret;

			try {
				// Forge recommends using the BlockState instead, but we don't have access to this, since this is an item.
				// ret = Utils.floatToString(((BlockItem) itemstack.getItem()).getBlock().getBlockHardness(null, null, null));
				ret = Utils.floatToString(((BlockItem) itemstack.getItem()).getBlock().getDefaultState().getBlockHardness(null, null)); // XXX
			}catch(NullPointerException e){ // In case of a null pointer
				ret = "?";
			}
			
			return ret;
		}, BlockItem.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicConditionParameter("foodpoints", (itemstack) -> Integer.toString(itemstack.getItem().getFood().getHealing()), ItemStack::isFood));
		
		HuesoDeWikiAPI.parameters.add(new BasicConditionParameter("saturation", (itemstack) -> {
			Food food = itemstack.getItem().getFood();
			return Utils.floatToString(food.getSaturation() * food.getHealing() * 2.0F);
		}, ItemStack::isFood));
		
		HuesoDeWikiAPI.parameters.add(new BasicConditionParameter("hunger", (itemstack) -> {
			Food food = itemstack.getItem().getFood();
			return "{{Shanks|" + food.getHealing() + "|" + Utils.floatToString(food.getSaturation()) + "}}";
		}, ItemStack::isFood));
		
		HuesoDeWikiAPI.parameters.add(new EffectsParameter());
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("armorrating", (itemstack) -> Integer.toString(((ArmorItem) itemstack.getItem()).getDamageReduceAmount()), ArmorItem.class));
		HuesoDeWikiAPI.parameters.add(new ToughnessParameter());
		
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("damage", (itemstack) -> {
			Item item = itemstack.getItem();
			
			Multimap<Attribute, AttributeModifier> attributes = null;
			
			if(item instanceof ToolItem)
				attributes = ((ToolItem) item).getAttributeModifiers(EquipmentSlotType.MAINHAND);
			else if(item instanceof SwordItem)
				attributes = ((SwordItem) item).getAttributeModifiers(EquipmentSlotType.MAINHAND);
			
			float damage = 1.0f; // default
			
			for(Attribute attribute : attributes.keySet())
				if(attribute.equals(Attributes.field_233823_f_)) // This is Attributes.ATTACK_DAMAGE
					for(AttributeModifier modifier : attributes.get(attribute))
						damage += modifier.getAmount();
			
			return Utils.floatToString(damage);
		}, ToolItem.class, SwordItem.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("aspeed", (itemstack) -> {
			Item item = itemstack.getItem();
			
			Multimap<Attribute, AttributeModifier> attributes = null;
			
			if(item instanceof ToolItem)
				attributes = ((ToolItem) item).getAttributeModifiers(EquipmentSlotType.MAINHAND);
			else if(item instanceof SwordItem){
				attributes = ((SwordItem) item).getAttributeModifiers(EquipmentSlotType.MAINHAND);
			}
			
			if(attributes == null)
				return "?";
			
			float speed = 4.0f; //default
			
			for(Attribute attribute : attributes.keySet())
				if(attribute.equals(Attributes.field_233825_h_)) // Attributes.ATTACK_SPEED
					for(AttributeModifier modifier : attributes.get(attribute))
						speed += modifier.getAmount();
			
			return String.format("%.2g", speed);
		}, ToolItem.class, SwordItem.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicConditionParameter("durability", (itemstack) -> Utils.floatToString(itemstack.getItem().getMaxDamage(itemstack) + 1), ItemStack::isDamageable));
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
