package xbony2.huesodewiki;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xbony2.huesodewiki.recipe.RecipeCreator;

@Mod.EventBusSubscriber(modid = HuesoDeWiki.MODID, value = Dist.CLIENT)
public class InputEventHandler {
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void buttonPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Post event){
		Minecraft mc = Minecraft.getInstance();

		if(mc.level == null)
			return;

		int eventKey = event.getKeyCode();
		int scanKey = event.getScanCode();
		InputConstants.Key input = InputConstants.getKey(eventKey, scanKey);

		if(HuesoDeWiki.copyPageKey.isActiveAndMatches(input)){
			ItemStack stack = Utils.getHoveredItemStack();
			
			if(stack.isEmpty())
				return;

			if(Screen.hasControlDown()){
				Utils.copyString(RecipeCreator.createRecipes(stack));
				Minecraft.getInstance().gui.getChat().addMessage(new TranslatableComponent("msg.huesodewiki.copiedrecipe", stack.getHoverName()));
			}else{
				Utils.copyString(PageCreator.createPage(stack));
				Minecraft.getInstance().gui.getChat().addMessage(new TranslatableComponent("msg.huesodewiki.copiedpage", stack.getHoverName()));
			}

		}else if(HuesoDeWiki.copyNameKey.isActiveAndMatches(input)){
			ItemStack stack = Utils.getHoveredItemStack();

			if(!stack.isEmpty()){
				Utils.copyString(stack.getHoverName().getString());
				Minecraft.getInstance().gui.getChat().addMessage(new TranslatableComponent("msg.huesodewiki.copieditemname", stack.getHoverName()));
			}
		}
	}
}
