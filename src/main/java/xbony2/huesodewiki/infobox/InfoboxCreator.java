package xbony2.huesodewiki.infobox;

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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.HuesoDeWikiAPI;
import xbony2.huesodewiki.api.infobox.BasicInstanceOfParameter;
import xbony2.huesodewiki.infobox.parameters.*;

public class InfoboxCreator {

	public static void init() {
		HuesoDeWikiAPI.parameters.add(new NameParameter());
		HuesoDeWikiAPI.parameters.add(new ImageIconParameter());
		HuesoDeWikiAPI.parameters.add(new ModParameter());
		HuesoDeWikiAPI.parameters.add(new TypeParameter());
//		parameters.add(new OreDictNameParameter()); todo tags
		//parameters.add(new RegistryNameParameter());
		//parameters.add(new UnlocalizedNameParameter()); // Disabled until issue resolved
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("blastresistance", (itemstack) -> {
			String ret;
			
			try{
				ret = Utils.floatToString(((ItemBlock)itemstack.getItem()).getBlock().getExplosionResistance() * 5); //Minecraft is weird with it, don't ask
			}catch(Exception e){ //In case of a null pointer
				ret = "?";
			}
			
			return ret;
		}, ItemBlock.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("hardness", (itemstack) -> {
			String ret;
			
			try{
				ret = Utils.floatToString(((ItemBlock)itemstack.getItem()).getBlock().getBlockHardness(null, null, null));
			}catch(Exception e){ //In case of a null pointer
				ret = "?";
			}
			
			return ret;
		}, ItemBlock.class));
		
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("foodpoints", (itemstack) -> Integer.toString(((ItemFood)itemstack.getItem()).getHealAmount(itemstack)), ItemFood.class));
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("saturation", (itemstack) -> Utils.floatToString(((ItemFood)itemstack.getItem()).getSaturationModifier(itemstack) * ((ItemFood)itemstack.getItem()).getHealAmount(itemstack)), ItemFood.class));
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("hunger", (itemstack) -> {
			ItemFood food = (ItemFood)itemstack.getItem();
			return "{{Shanks|" + Integer.toString(food.getHealAmount(itemstack)) + "|" + Utils.floatToString(food.getSaturationModifier(itemstack)) + "}}";
		}, ItemFood.class));
		
		HuesoDeWikiAPI.parameters.add(new EffectsParameter());
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("armorrating", (itemstack) -> Integer.toString(((ItemArmor)itemstack.getItem()).getDamageReduceAmount()), ItemArmor.class));
		HuesoDeWikiAPI.parameters.add(new ToughnessParameter());
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("damage", (itemstack) -> { //todo use attribute on tools for both 
			Item item = itemstack.getItem();
			if(item instanceof ItemTool)
				return Utils.floatToString(ObfuscationReflectionHelper.getPrivateValue(ItemTool.class, (ItemTool) item, "field_77865_bY")); //attackDamage
			else if(item instanceof ItemSword){
				Multimap<String, AttributeModifier> multimap = ((ItemSword)item).getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
				float damage = 1.0f; //default
				for(String name : multimap.keySet())
					if(name.equals(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
						for(AttributeModifier modifier : multimap.get(name))
							damage += modifier.getAmount();
				return Utils.floatToString(damage);
			}
			return "?";
		}, ItemTool.class, ItemSword.class));
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("aspeed", (itemstack) -> {
			Item item = itemstack.getItem();
			if(item instanceof ItemTool)
				return Utils.floatToString(ObfuscationReflectionHelper.getPrivateValue(ItemTool.class, (ItemTool) item, "field_185065_c")); //attackSpeed
			else if(item instanceof ItemSword){
				Multimap<String, AttributeModifier> multimap = ((ItemSword)item).getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
				float speed = 4.0f; //default
				for(String name : multimap.keySet())
					if(name.equals(SharedMonsterAttributes.ATTACK_SPEED.getName()))
						for(AttributeModifier modifier : multimap.get(name))
							speed += modifier.getAmount();
				
				return String.format("%.2g", speed);
			}
			return "?";
		}, ItemTool.class, ItemSword.class));
		HuesoDeWikiAPI.parameters.add(new BasicInstanceOfParameter("durability", (itemstack) -> Utils.floatToString(itemstack.getItem().getMaxDamage(itemstack) + 1), ItemTool.class));
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
