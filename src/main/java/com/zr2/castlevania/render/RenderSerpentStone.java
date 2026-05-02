package com.zr2.castlevania.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import com.zr2.castlevania.entity.EntitySerpentStone;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSerpentStone extends RenderPlayer {

    @Override
    protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_,
        float p_77036_5_, float p_77036_6_, float p_77036_7_) {
        this.setModelRotation(this.modelBipedMain, p_77036_1_.ridingEntity);
        super.renderModel(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
    }

    private void setModelRotation(ModelBiped modelBiped, Entity entity) {
        if (!(entity instanceof EntitySerpentStone)) {
            return;
        }
        if (entity.riddenByEntity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity.riddenByEntity;
        }

        modelBiped.isRiding = false;
        GL11.glRotatef(90, 1, 0, 0);
    }

    @Override
    protected void func_147906_a(Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_,
        double p_147906_7_, int p_147906_9_) {
        super.func_147906_a(p_147906_1_, p_147906_2_, p_147906_3_, p_147906_5_ - 1.5, p_147906_7_, p_147906_9_);
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,
        float p_76986_9_) {
        if (!(p_76986_1_.riddenByEntity == Minecraft.getMinecraft().thePlayer
            && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)) {
            super.doRender(p_76986_1_.riddenByEntity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        }
    }
}
