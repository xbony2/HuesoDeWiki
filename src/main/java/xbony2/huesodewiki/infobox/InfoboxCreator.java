package xbony2.huesodewiki.infobox;

import com.google.common.collect.Multimap;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
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
//		parameters.add(new OreDictNameParameter()); todo tags
		//parameters.add(new RegistryNameParameter());
		//parameters.add(new UnlocalizedNameParameter()); // Disabled until issue resolved
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("blastresistance", (itemstack) -> {
			String ret;
			
			try {
				ret = Utils.floatToString(((BlockItem) itemstack.getItem()).getBlock().getExplosionResistance() * 5); //Minecraft is weird with it, don't ask
			}catch(Exception e){ //In case of a null pointer
				ret = "?";
			}
			
			return ret;
		}, BlockItem.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("hardness", (itemstack) -> {
			String ret;

			try {
				ret = Utils.floatToString(((BlockItem) itemstack.getItem()).getBlock().getBlockHardness(null, null, null));
			}catch(Exception e){ //In case of a null pointer
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
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("damage", (itemstack) -> { //todo use attribute on tools for both 
			Item item = itemstack.getItem();
			if(item instanceof ToolItem)
				return Utils.floatToString(ObfuscationReflectionHelper.getPrivateValue(ToolItem.class, (ToolItem) item, "field_77865_bY")); //attackDamage
			else if(item instanceof SwordItem){
				Multimap<String, AttributeModifier> multimap = ((SwordItem) item).getAttributeModifiers(EquipmentSlotType.MAINHAND);
				float damage = 1.0f; //default
				for(String name : multimap.keySet())
					if(name.equals(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
						for(AttributeModifier modifier : multimap.get(name))
							damage += modifier.getAmount();
				return Utils.floatToString(damage);
			}
			return "?";
		}, ToolItem.class, SwordItem.class));
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("aspeed", (itemstack) -> {
			Item item = itemstack.getItem();
			if(item instanceof ToolItem)
				return Utils.floatToString(ObfuscationReflectionHelper.getPrivateValue(ToolItem.class, (ToolItem) item, "field_185065_c")); //attackSpeed
			else if(item instanceof SwordItem){
				Multimap<String, AttributeModifier> multimap = ((SwordItem) item).getAttributeModifiers(EquipmentSlotType.MAINHAND);
				float speed = 4.0f; //default
				for(String name : multimap.keySet())
					if(name.equals(SharedMonsterAttributes.ATTACK_SPEED.getName()))
						for(AttributeModifier modifier : multimap.get(name))
							speed += modifier.getAmount();
				
				return String.format("%.2g", speed);
			}
			return "?";
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
		
		HuesoDeWikiAPI.parameters.stream().filter((parameter) -> parameter.canAdd(itemstack)).forEach((parameter) -> ret.append('|').append(parameter.getParameterName()).append('=').append(parameter.getParameterText(itemstack)).append('\n'));
		
		ret.append("}}\n");
		return ret.toString();
	}
}
