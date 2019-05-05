package xbony2.huesodewiki;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xbony2.huesodewiki.recipe.RecipeCreator;

@Mod.EventBusSubscriber(modid = HuesoDeWiki.MODID, value = Dist.CLIENT)
public class InputEventHandler {
	@SubscribeEvent
	public static void buttonPressed(InputEvent.KeyInputEvent event){
		Minecraft mc = Minecraft.getInstance();
		int eventKey = event.getKey();
		int scanKey = event.getScanCode();

		if(mc.world == null /*|| !Keyboard.getEventKeyState() || Keyboard.isRepeatEvent()*/)
			return;

		if(HuesoDeWiki.copyPageKey.isActiveAndMatches(InputMappings.getInputByCode(eventKey, scanKey))){
			ItemStack stack = Utils.getHoveredItemStack();
			if(stack.isEmpty())
				return;

			if(GuiScreen.isCtrlKeyDown()){
				Utils.copyString(RecipeCreator.createRecipes(stack));
				Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("msg.copiedrecipe", stack.getDisplayName()));
			}else{
				Utils.copyString(PageCreator.createPage(stack));
				Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("msg.copiedpage", stack.getDisplayName()));
			}

		}else if(HuesoDeWiki.copyNameKey.isActiveAndMatches(InputMappings.getInputByCode(eventKey, scanKey))){
			ItemStack stack = Utils.getHoveredItemStack();

			if(!stack.isEmpty()){
				Utils.copyString(stack.getDisplayName().getUnformattedComponentText());
				Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("msg.copieditemname", stack.getDisplayName()));
			}
		}
	}
}
