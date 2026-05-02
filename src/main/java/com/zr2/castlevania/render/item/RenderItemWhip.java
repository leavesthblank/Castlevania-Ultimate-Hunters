package com.zr2.castlevania.render.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RenderItemWhip implements IItemRenderer {

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
        "textures/misc/enchanted_item_glint.png");

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType itemRenderType) {
        return itemRenderType == ItemRenderType.EQUIPPED;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType itemRenderType, ItemStack itemStack,
        ItemRendererHelper itemRendererHelper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType itemRenderType, ItemStack itemStack, Object... objects) {
        renderItem((EntityLivingBase) objects[1], itemStack, 0);
    }

    public void renderItem(EntityLivingBase p_78443_1_, ItemStack p_78443_2_, int p_78443_3_) {
        GL11.glPushMatrix();
        TextureManager texturemanager = Minecraft.getMinecraft()
            .getTextureManager();
        {
            IIcon iicon = p_78443_1_.getItemIcon(p_78443_2_, p_78443_3_);
            if (iicon == null) {
                GL11.glPopMatrix();
                return;
            }

            texturemanager.bindTexture(texturemanager.getResourceLocation(p_78443_2_.getItemSpriteNumber()));
            TextureUtil.func_152777_a(false, false, 1.0F);
            Tessellator tessellator = Tessellator.instance;
            float f = iicon.getMinU();
            float f1 = iicon.getMaxU();
            float f2 = iicon.getMinV();
            float f3 = iicon.getMaxV();
            GL11.glTranslatef(1F, 0, 0);
            GL11.glRotatef(90, 0, 0, 1);
            ItemRenderer
                .renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
            if (p_78443_2_.hasEffect(p_78443_3_)) {
                GL11.glDepthFunc(514);
                GL11.glDisable(2896);
                texturemanager.bindTexture(RES_ITEM_GLINT);
                GL11.glEnable(3042);
                OpenGlHelper.glBlendFunc(768, 1, 1, 0);
                float f7 = 0.76F;
                GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
                GL11.glMatrixMode(5890);
                GL11.glPushMatrix();
                float f8 = 0.125F;
                GL11.glScalef(f8, f8, f8);
                float f9 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                GL11.glTranslatef(f9, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f8, f8, f8);
                f9 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                GL11.glTranslatef(-f9, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
                GL11.glPopMatrix();
                GL11.glMatrixMode(5888);
                GL11.glDisable(3042);
                GL11.glEnable(2896);
                GL11.glDepthFunc(515);
            }

            GL11.glDisable(32826);
            texturemanager.bindTexture(texturemanager.getResourceLocation(p_78443_2_.getItemSpriteNumber()));
            TextureUtil.func_147945_b();
        }

        GL11.glPopMatrix();
    }

}
