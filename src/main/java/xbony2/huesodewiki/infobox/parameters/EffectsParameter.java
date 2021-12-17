package xbony2.huesodewiki.infobox.parameters;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionUtils;
import com.mojang.datafixers.util.Pair;
import xbony2.huesodewiki.Utils;
import xbony2.huesodewiki.api.infobox.IInfoboxParameter;

import java.util.List;
import java.util.stream.Collectors;

public class EffectsParameter implements IInfoboxParameter {

	@Override
	public boolean canAdd(ItemStack itemstack){
		return (itemstack.isEdible() && !itemstack.getItem().getFoodProperties().getEffects().isEmpty()) || itemstack.getItem() instanceof PotionItem;
	}

	@Override
	public String getParameterName(){
		return "effects";
	}

	@Override
	public String getParameterText(ItemStack itemstack){
		if(itemstack.isEdible()){
			List<Pair<MobEffectInstance, Float>> effects = itemstack.getItem().getFoodProperties().getEffects();
			return effects.stream().map(pair -> formatEffect(pair.getFirst(), pair.getSecond())).collect(Collectors.joining());
		}else{
			List<MobEffectInstance> effects = PotionUtils.getMobEffects(itemstack);
			return effects.stream().map(effect -> formatEffect(effect, 1)).collect(Collectors.joining());
		}
	}


	private static String formatEffect(MobEffectInstance effect, float chance){
		String s = "{{Effect|" + I18n.get(effect.getDescriptionId()) + "|" + effect.getDuration() + "|" + effect.getAmplifier();
		
		if(chance != 1f)
			s += "|" + Utils.floatToString((1000.0f * chance) / 10f); //round to one decimal
		
		s += "}}";
		
		return s;
	}
}
