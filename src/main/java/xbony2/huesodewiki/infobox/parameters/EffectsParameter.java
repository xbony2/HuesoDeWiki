package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import org.apache.commons.lang3.tuple.Pair;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

import java.util.List;
import java.util.stream.Collectors;

public class EffectsParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return (itemstack.isFood() && itemstack.getItem().getFood().getEffects().isEmpty()) || itemstack.getItem() instanceof PotionItem;
	}

	@Override
	public String getParameterName(){
		return "effects";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		if(itemstack.isFood()){
			List<Pair<EffectInstance, Float>> effects = itemstack.getItem().getFood().getEffects();
			return effects.stream().map(pair -> formatEffect(pair.getLeft(), pair.getRight())).collect(Collectors.joining());
		}else{
			List<EffectInstance> effects = PotionUtils.getEffectsFromStack(itemstack);
			return effects.stream().map(effect -> formatEffect(effect, 1)).collect(Collectors.joining());
		}
	}


	private static String formatEffect(EffectInstance effect, float chance){
		String s = "{{Effect|" + I18n.format(effect.getEffectName()) + "|" + effect.getDuration() + "|" + effect.getAmplifier();
		if(chance != 1f)
			s += "|" + Utils.floatToString((1000.0f * chance) / 10f); //round to one decimal
		s += "}}";
		return s;
	}
}
