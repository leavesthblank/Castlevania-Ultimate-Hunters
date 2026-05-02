package com.zr2.castlevania.event.client;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.properties.ExtendedPlayerHeart;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class ClientHeartOverlayEventHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final RenderItem renderItem = new RenderItem();
    private final ItemStack heart = new ItemStack(Castlevania.SMALL_HEART);

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        ExtendedPlayerHeart player = (ExtendedPlayerHeart) mc.thePlayer.getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);

        RenderHelper.enableGUIStandardItemLighting();
        renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, heart, 0, 0);
        mc.fontRenderer.drawStringWithShadow(player.getCurrentHeart() + " / " + player.getMaxHeart(), 16, 4, 0xFFFFFF);
        RenderHelper.disableStandardItemLighting();
        this.mc.getTextureManager().bindTexture(Gui.icons); //Fix hunger bar
    }

}
