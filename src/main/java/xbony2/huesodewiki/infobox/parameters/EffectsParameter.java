package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

import java.util.List;

public class EffectsParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return (itemstack.getItem() instanceof ItemFood && getEffect(itemstack) != null) || itemstack.getItem() instanceof ItemPotion;
	}

	@Override
	public String getParameterName(){
		return "effects";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		if(itemstack.getItem() instanceof ItemFood){
			PotionEffect effect = getEffect(itemstack);
			float chance = ObfuscationReflectionHelper.getPrivateValue(ItemFood.class, (ItemFood) itemstack.getItem(), "field_77858_cd"); //potionEffectProbability
			return formatEffect(effect, chance);
		}else{
			List<PotionEffect> effects = PotionUtils.getEffectsFromStack(itemstack);
			StringBuilder str = new StringBuilder();
			effects.forEach(effect -> str.append(formatEffect(effect, 1f)));
			return str.toString();
		}
	}

	private static PotionEffect getEffect(ItemStack itemstack){
		return ObfuscationReflectionHelper.getPrivateValue(ItemFood.class, (ItemFood) itemstack.getItem(), "field_77851_ca"); //potionId
	}

	private static String formatEffect(PotionEffect effect, float chance){
		String s = "{{Effect|" + I18n.format(effect.getEffectName()) + "|" + Integer.toString(effect.getDuration()) + "|" + Integer.toString(effect.getAmplifier());
		if(chance != 1f)
			s += "|" + Utils.floatToString((1000.0f * chance) / 10f); //round to one decimal
		s += "}}";
		return s;
	}
}
