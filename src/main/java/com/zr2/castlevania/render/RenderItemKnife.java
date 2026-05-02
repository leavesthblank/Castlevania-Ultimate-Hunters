package com.zr2.castlevania.render;

import com.zr2.castlevania.entity.EntityHolyCross;
import com.zr2.castlevania.entity.EntityKnife;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderItemKnife extends Render {

    private Random random = new Random();
    public boolean renderWithColor = true;
    public static final boolean renderInFrame = false;

    private final ItemStack itemstack;

    public RenderItemKnife(Item item) {
        this.itemstack = new ItemStack(item);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    public void doRender(Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_9_, float ok) {
        if (itemstack.getItem() != null) {
            this.bindEntityTexture(entity);
            TextureUtil.func_152777_a(false, false, 1.0F);
            this.random.setSeed(187L);
            GL11.glPushMatrix();
            float f2 = this.shouldBob() ? MathHelper.sin((p_76986_9_) / 10.0F) * 0.1F + 0.1F : 0.0F;

            byte b0 = this.getMiniBlockCount(itemstack, (byte) 1);
            GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_ + f2, (float) p_76986_6_);
            GL11.glEnable(32826);
            float f6;
            float f7;
            int k;
            int l;
            float f8;
            float f5;
            if (itemstack.getItem().requiresMultipleRenderPasses()) {
                if (renderInFrame) {
                    GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                    GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                } else {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                }


                for (int j = 0; j < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++j) {
                    this.random.setSeed(187L);
                    IIcon iicon1 = itemstack.getItem().getIcon(itemstack, j);
                    if (this.renderWithColor) {
                        k = itemstack.getItem().getColorFromItemStack(itemstack, j);
                        f5 = (float) (k >> 16 & 255) / 255.0F;
                        f6 = (float) (k >> 8 & 255) / 255.0F;
                        f7 = (float) (k & 255) / 255.0F;
                        GL11.glColor4f(f5, f6, f7, 1.0F);
                        this.renderDroppedItem(entity, iicon1, b0, p_76986_9_, f5, f6, f7);
                    } else {
                        this.renderDroppedItem(entity, iicon1, b0, p_76986_9_, 1.0F, 1.0F, 1.0F);
                    }
                }
            } else {
                if (renderInFrame) {
                    GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                    GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                } else {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                }

                IIcon iicon = itemstack.getIconIndex();
                if (this.renderWithColor) {
                    l = itemstack.getItem().getColorFromItemStack(itemstack, 0);
                    f8 = (float) (l >> 16 & 255) / 255.0F;
                    f5 = (float) (l >> 8 & 255) / 255.0F;
                    f6 = (float) (l & 255) / 255.0F;
                    this.renderDroppedItem(entity, iicon, b0, p_76986_9_, f8, f5, f6);
                } else {
                    this.renderDroppedItem(entity, iicon, b0, p_76986_9_, 1.0F, 1.0F, 1.0F);
                }
            }

            GL11.glDisable(32826);
            GL11.glPopMatrix();
            this.bindEntityTexture(entity);
            TextureUtil.func_147945_b();
        }

    }

    protected ResourceLocation getEntityTexture() {
        return this.renderManager.renderEngine.getResourceLocation(itemstack.getItemSpriteNumber());
    }


    private void renderDroppedItem(Entity entity, IIcon p_77020_2_, int p_77020_3_, float p_77020_4_, float p_77020_5_, float p_77020_6_, float p_77020_7_) {
        Tessellator tessellator = Tessellator.instance;
        if (p_77020_2_ == null) {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(this.itemstack.getItemSpriteNumber());
            p_77020_2_ = ((TextureMap) texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        float f14 = ((IIcon) p_77020_2_).getMinU();
        float f15 = ((IIcon) p_77020_2_).getMaxU();
        float f4 = ((IIcon) p_77020_2_).getMinV();
        float f5 = ((IIcon) p_77020_2_).getMaxV();
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        float f10;
        if (this.renderManager.options.fancyGraphics) {
            GL11.glPushMatrix();
            if (renderInFrame) {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            } else {
                if (entity instanceof EntityHolyCross) {
                    //GL11.glRotatef(90, 0,0, 1);
                    GL11.glRotatef(90, 1, 0, 0);
                    GL11.glRotatef(entity.ticksExisted * 8, 0, 0, 1);
                } else {
                    Vec3 vector = Vec3.createVectorHelper((float) -entity.motionX, (float) -entity.motionY, (float) -entity.motionZ);
                    vector = vector.normalize();
                    GL11.glTranslated(vector.xCoord * 0.5, vector.yCoord * 0.5, vector.zCoord * 0.5);
                    GL11.glRotatef(entity.rotationYaw + 270, 0, 1, 0);
                    GL11.glRotatef(entity.rotationPitch - 90, 0, 0, 1);
                    if (entity instanceof EntityKnife) {
                        GL11.glRotatef(90, 0, 0, 1);
                        GL11.glRotatef(90, 1, 0, 0);
                    }
                }
            }

            float f9 = 0.0625F;
            f10 = 0.021875F;
            int j = itemstack.stackSize;
            byte b0 = this.getMiniItemCount(itemstack, (byte) 1);
            GL11.glTranslatef(-f7, -f8, -((f9 + f10) * (float) b0 / 2.0F));

            for (int k = 0; k < b0; ++k) {
                float f11;
                float f12;
                float f13;
                if (k > 0 && this.shouldSpreadItems()) {
                    f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    f12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
                    GL11.glTranslatef(f11, f12, f9 + f10);
                } else {
                    GL11.glTranslatef(0.0F, 0.0F, f9 + f10);
                }

                if (itemstack.getItemSpriteNumber() == 0) {
                    this.bindTexture(TextureMap.locationBlocksTexture);
                } else {
                    this.bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, f15, f4, f14, f5, ((IIcon) p_77020_2_).getIconWidth(), ((IIcon) p_77020_2_).getIconHeight(), f9);
            }

            GL11.glPopMatrix();
        } else {
            for (int l = 0; l < p_77020_3_; ++l) {
                GL11.glPushMatrix();
                if (l > 0) {
                    f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f16 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f10, f16, f17);
                }

                if (!renderInFrame) {
                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                tessellator.addVertexWithUV((double) (0.0F - f7), (double) (0.0F - f8), 0.0D, (double) f14, (double) f5);
                tessellator.addVertexWithUV((double) (f6 - f7), (double) (0.0F - f8), 0.0D, (double) f15, (double) f5);
                tessellator.addVertexWithUV((double) (f6 - f7), (double) (1.0F - f8), 0.0D, (double) f15, (double) f4);
                tessellator.addVertexWithUV((double) (0.0F - f7), (double) (1.0F - f8), 0.0D, (double) f14, (double) f4);
                tessellator.draw();
                GL11.glPopMatrix();
            }
        }

    }

    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.getEntityTexture();
    }

    public boolean shouldSpreadItems() {
        return true;
    }

    public boolean shouldBob() {
        return true;
    }

    public byte getMiniBlockCount(ItemStack stack, byte original) {
        return original;
    }

    public byte getMiniItemCount(ItemStack stack, byte original) {
        return original;
    }

}

