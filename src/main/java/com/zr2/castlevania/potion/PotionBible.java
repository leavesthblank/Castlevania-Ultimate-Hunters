package com.zr2.castlevania.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import org.lwjgl.opengl.GL11;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.proxy.ClientProxy;

public class PotionBible extends Potion {

    private final ItemStack bible = new ItemStack(Castlevania.BIBLE);

    public PotionBible() {
        super(27, false, 0xFFADD8E6);
        this.setPotionName("potion.bible");
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    public boolean isUsable() {
        return false;
    }

    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        RenderHelper.enableGUIStandardItemLighting();
        ClientProxy.RENDER_ITEM.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, bible, x + 7, y + 7);
        RenderHelper.disableStandardItemLighting();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

}
