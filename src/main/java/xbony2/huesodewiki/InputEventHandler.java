package xbony2.huesodewiki;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
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

		if(mc.world == null)
			return;

		int eventKey = event.getKeyCode();
		int scanKey = event.getScanCode();
		InputMappings.Input input = InputMappings.getInputByCode(eventKey, scanKey);

		if(HuesoDeWiki.copyPageKey.isActiveAndMatches(input)){
			ItemStack stack = Utils.getHoveredItemStack();
			
			if(stack.isEmpty())
				return;

			if(Screen.hasControlDown()){
				Utils.copyString(RecipeCreator.createRecipes(stack));
				Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new TranslationTextComponent("msg.huesodewiki.copiedrecipe", stack.getDisplayName()));
			}else{
				Utils.copyString(PageCreator.createPage(stack));
				Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new TranslationTextComponent("msg.huesodewiki.copiedpage", stack.getDisplayName()));
			}

		}else if(HuesoDeWiki.copyNameKey.isActiveAndMatches(input)){
			ItemStack stack = Utils.getHoveredItemStack();

			if(!stack.isEmpty()){
				Utils.copyString(stack.getDisplayName().getString());
				Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new TranslationTextComponent("msg.huesodewiki.copieditemname", stack.getDisplayName()));
			}
		}
	}
}
