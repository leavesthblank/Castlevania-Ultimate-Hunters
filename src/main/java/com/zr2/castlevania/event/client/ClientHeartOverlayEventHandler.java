package com.zr2.castlevania.event.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.config.ModConfig;
import com.zr2.castlevania.properties.ExtendedPlayerHeart;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientHeartOverlayEventHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final RenderItem renderItem = new RenderItem();
    private final ItemStack heart = new ItemStack(Castlevania.SMALL_HEART);

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        ExtendedPlayerHeart player = (ExtendedPlayerHeart) mc.thePlayer
            .getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);

        RenderHelper.enableGUIStandardItemLighting();
        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, heart, 0, 0);
        String text = getString(player);
        mc.fontRenderer.drawStringWithShadow(text, 16, 4, 0xFFFFFF);
        RenderHelper.disableStandardItemLighting();
        this.mc.getTextureManager()
            .bindTexture(Gui.icons); // Fix hunger bar
    }

    private static String getString(ExtendedPlayerHeart player) {
        String text;
        // Priority: infinite max-cap display, then infinite consumption display, else normal
        if (ModConfig.enableInfiniteHeartLimit && ModConfig.infiniteHeartsInSurvival) {
            text = "∞ / ∞";
        } else if (ModConfig.enableInfiniteHeartLimit) {
            text = player.getCurrentHeart() + " / ∞"; // show 0 / ∞ when max is infinite
        } else if (ModConfig.infiniteHeartsInSurvival) {
            text = "∞ / " + player.getMaxHeart(); // show ∞ / max when infinite hearts in survival
        } else {
            text = player.getCurrentHeart() + " / " + player.getMaxHeart();
        }
        return text;
    }

}
