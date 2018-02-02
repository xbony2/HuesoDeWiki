package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

import java.lang.reflect.Field;
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

			float chance = 1f;
			try {
				Field field = Utils.getField(ItemFood.class, "potionEffectProbability", "field_77858_cd");

				if(field != null){
					field.setAccessible(true);
					chance = field.getFloat(itemstack.getItem());
				}
			}catch(IllegalArgumentException | IllegalAccessException e){
				e.printStackTrace();
			}
			return formatEffect(effect, chance);
		}else{
			List<PotionEffect> effects = PotionUtils.getEffectsFromStack(itemstack);
			StringBuilder str = new StringBuilder();
			for(PotionEffect effect : effects){
				str.append(formatEffect(effect, 1f));
			}
			return str.toString();
		}
	}

	private PotionEffect getEffect(ItemStack itemstack){
		try {
			Field field = Utils.getField(ItemFood.class, "potionId", "field_77851_ca");

			if(field != null){
				field.setAccessible(true);
				return (PotionEffect) field.get(itemstack.getItem());
			}
		}catch(IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
		}
		return null;
	}

	private String formatEffect(PotionEffect effect, float chance){
		String s = "{{Effect|" + I18n.format(effect.getEffectName()) + "|" + Integer.toString(effect.getDuration()) + "|" + Integer.toString(effect.getAmplifier());
		if(chance != 1f)
			s += "|" + Utils.floatToString((1000.0f * chance) / 10f); //round to one decimal
		s += "}}";
		return s;
	}
}
