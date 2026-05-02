package com.zr2.castlevania.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.entity.EntityHolyFire;

public class RenderHolyFire extends Render {

    private static final ResourceLocation FIRE_TEXTURES = new ResourceLocation(
        Castlevania.MODID,
        "textures/entity/holy_fire.png");
    private static RenderManager staticRenderManager;

    @Override
    public void doRender(Entity entity, double v, double v1, double v2, float v3, float v4) {
        int tick = entity.ticksExisted;
        if (entity instanceof EntityHolyFire) {
            tick -= ((EntityHolyFire) entity).getDormant();
        } else {
            tick %= 60;
        }
        if (tick >= 0) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) v, (float) v1, (float) v2);
            GL11.glEnable(32826);
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            staticRenderManager.renderEngine.bindTexture(FIRE_TEXTURES);
            Tessellator var11 = Tessellator.instance;

            if (!(entity instanceof EntityHolyFire)) {
                GL11.glScalef(entity.width + 0.5F, entity.height + 0.5F, entity.width + 0.5F);
            }

            this.drawIcon(var11, tick / 2);
            GL11.glDisable(32826);
            GL11.glPopMatrix();
        }
    }

    protected ResourceLocation getEntityTexture(Entity p_getEntityTexture_1_) {
        return FIRE_TEXTURES;
    }

    private void drawIcon(Tessellator tessellator, int stage) {
        stage %= 12;
        float minU = U_POS[stage];
        float maxU = U_POS[stage + 1];
        float minV = 0;
        float maxV = 1F;
        float var7 = 1.0F;
        float var8 = 0.5F;
        float var9 = 0.25F;
        GL11.glRotatef(180.0F - staticRenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-staticRenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        // GL11.glScalef(2F, 3.23F, 2F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double) (0.0F - var8), (double) (0.0F - var9), 0.0D, (double) minU, (double) maxV);
        tessellator.addVertexWithUV((double) (var7 - var8), (double) (0.0F - var9), 0.0D, (double) maxU, (double) maxV);
        tessellator.addVertexWithUV((double) (var7 - var8), (double) (var7 - var9), 0.0D, (double) maxU, (double) minV);
        tessellator.addVertexWithUV((double) (0.0F - var8), (double) (var7 - var9), 0.0D, (double) minU, (double) minV);
        tessellator.draw();
    }

    @Override
    public void setRenderManager(RenderManager p_setRenderManager_1_) {
        super.setRenderManager(p_setRenderManager_1_);
        staticRenderManager = p_setRenderManager_1_;
    }

    private static final float[] U_POS = new float[13];

    static {
        U_POS[0] = 0F;
        U_POS[1] = 15 / 212F;
        U_POS[2] = 31 / 212F;
        U_POS[3] = 49 / 212F;
        U_POS[4] = 67 / 212F;
        U_POS[5] = 86 / 212F;
        U_POS[6] = 106 / 212F;
        U_POS[7] = 125 / 212F;
        U_POS[8] = 143 / 212F;
        U_POS[9] = 160 / 212F;
        U_POS[10] = 177 / 212F;
        U_POS[11] = 195 / 212F;
        U_POS[12] = 1F;
    }

}
