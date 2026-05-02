package com.zr2.castlevania.render;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.entity.EntityWhipHook;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderGrapple extends Render {

    private static final ResourceLocation GRAPPLE_TEXTURE = new ResourceLocation(Castlevania.MODID, "textures/entity/silver_grapple.png");


    private void renderLine(EntityWhipHook hook, float what) {
        GL11.glPushMatrix();
        double x = hook.posX - hook.shootingEntity.posX;
        double y = hook.posY - hook.shootingEntity.posY;
        double z = hook.posZ - hook.shootingEntity.posZ;
        GL11.glTranslatef((float) x, (float) y - 0.45F, (float) z);
        float var12 = (float) (hook.shootingEntity.posX - hook.posX - (hook.prevPosX - hook.posX) * (double) (1.0F - what));
        float var13 = (float) (hook.shootingEntity.posY - 1.0D - hook.posY - (hook.prevPosY - hook.posY) * (double) (1.0F - what));
        float var14 = (float) (hook.shootingEntity.posZ - hook.posZ - (hook.prevPosZ - hook.posZ) * (double) (1.0F - what));
        float var15 = MathHelper.sqrt_float(var12 * var12 + var14 * var14);
        GL11.glRotatef((float) (-Math.atan2((double) var14, (double) var12)) * 180.0F / 3.1415927F - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef((float) (-Math.atan2((double) var15, (double) var13)) * 180.0F / 3.1415927F - 90.0F, 1.0F, 0.0F, 0.0F);
        Tessellator var17 = Tessellator.instance;
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2884);
        this.bindTexture(GRAPPLE_TEXTURE);
        GL11.glShadeModel(7425);
        float var18 = 0.0F - (what) * 0.01F;
        float var19 = MathHelper.sqrt_float(hook.radiusSq) / 32.0F - (what) * 0.01F;
        var17.startDrawing(5);
        byte var20 = 2;

        float l = MathHelper.sqrt_float(hook.radiusSq);
        GL11.glRotatef(90, 0, 0, 1);
        for (int var21 = 0; var21 <= var20; ++var21) {
            float var22 = MathHelper.sin((float) (var21 % var20) * 3.1415927F * 2.0F / (float) var20) * 0.75F;
            float var23 = MathHelper.cos((float) (var21 % var20) * 3.1415927F * 2.0F / (float) var20) * 0.75F;
            float var24 = (float) (var21 % var20) * 1.0F / (float) var20;
            var17.addVertexWithUV((var22 * 0.05), (var23 * 0.05), 0.0D, (double) var24, (double) var19);
            var17.addVertexWithUV((var22 * 0.05), (var23 * 0.05), l, (double) var24, (double) var18);
        }
//        float l = MathHelper.sqrt_float(hook.radiusSq);
//        var17.addVertexWithUV(0, 0, 0, 0 ,0);
//        var17.addVertexWithUV(x, y, z, 0, 1);
//        var17.addVertexWithUV(0.1, 0, 0, 1, 0);
//        var17.addVertexWithUV(x + 0.1, y, z, 1, 1);

        var17.draw();
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double v, double v1, double v2, float v3, float v4) {
        EntityWhipHook hook = (EntityWhipHook) entity;
        renderLine(hook, v4);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
