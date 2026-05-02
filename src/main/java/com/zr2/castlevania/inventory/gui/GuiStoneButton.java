package com.zr2.castlevania.inventory.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.event.client.ClientGuiEventHandler;
import com.zr2.castlevania.item.ItemStone;
import com.zr2.castlevania.properties.ExtendedPlayerStones;

public class GuiStoneButton extends GuiButton {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(
        Castlevania.MODID,
        "textures/gui/stones.png");

    public GuiStoneButton(int id, int x, int y) {
        super(id, x, y, 16, 16, "");
    }

    @Override
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.id == (329 << 16)) {
            Gui currentGui = Minecraft.getMinecraft().currentScreen;
            // move rendering to button

            if ((currentGui instanceof GuiInventory || currentGui instanceof GuiContainerCreative)) {
                GuiContainer gui = (GuiContainer) currentGui;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft()
                    .getTextureManager()
                    .bindTexture(BACKGROUND);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);

                float x = (gui.width + 212) / 2;
                if (ClientGuiEventHandler.hasEffect) {
                    x += 60;
                }
                float y = (gui.height - 160) / 2;
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(x + 0, y + 116, 0, 0, 1);
                tessellator.addVertexWithUV(x + 28, y + 116, 0, 1, 1);
                tessellator.addVertexWithUV(x + 28, y + 0, 0, 1, 0);
                tessellator.addVertexWithUV(x + 0, y + 0, 0, 0, 0);
                tessellator.draw();

                Minecraft.getMinecraft()
                    .getTextureManager()
                    .bindTexture(TextureMap.locationItemsTexture);
                x += 6;
                y += 5;

                ExtendedPlayerStones playerStones = (ExtendedPlayerStones) Minecraft.getMinecraft().thePlayer
                    .getExtendedProperties(ExtendedPlayerStones.EXT_PROP_NAME);
                for (int i = 0; i < ItemStone.getAmountofStones(); ++i) {
                    ItemStone itemStone = ItemStone.STONES.get(i);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, playerStones.isActive(itemStone) ? 1F : 0.4F);
                    IIcon icon = itemStone.getIconFromDamage(0);
                    tessellator.startDrawingQuads();
                    tessellator.addVertexWithUV(x, y + 16, 0, icon.getMinU(), icon.getMaxV());
                    tessellator.addVertexWithUV(x + 16, y + 16, 0, icon.getMaxU(), icon.getMaxV());
                    tessellator.addVertexWithUV(x + 16, y, 0, icon.getMaxU(), icon.getMinV());
                    tessellator.addVertexWithUV(x, y, 0, icon.getMinU(), icon.getMinV());
                    tessellator.draw();
                    y += 18;
                }
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_LIGHTING);
            }
        }
    }

}
