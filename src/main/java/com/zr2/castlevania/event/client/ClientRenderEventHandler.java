package com.zr2.castlevania.event.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.properties.ExtendedPlayerBible;
import com.zr2.castlevania.properties.ExtendedPlayerFire;
import com.zr2.castlevania.render.RenderHolyFire;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientRenderEventHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final RenderHolyFire renderHolyFire = new RenderHolyFire();

    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Post event) {
        ExtendedPlayerBible bible = (ExtendedPlayerBible) event.entityPlayer
            .getExtendedProperties(ExtendedPlayerBible.EXT_PROP_NAME);

        if (bible.isUsingBible()) {
            renderBible(event.entityPlayer);
        }
    }

    @SubscribeEvent
    public void onRender(RenderHandEvent event) {
        EntityPlayer player = mc.thePlayer;
        ExtendedPlayerBible bible = (ExtendedPlayerBible) player
            .getExtendedProperties(ExtendedPlayerBible.EXT_PROP_NAME);
        if (bible.isUsingBible() && mc.gameSettings.thirdPersonView == 0/* && mc.currentScreen == null */) {
            renderBible(player);
            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
        }
    }

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Post event) {
        ExtendedPlayerFire extendedPlayerFire = (ExtendedPlayerFire) event.entity
            .getExtendedProperties(ExtendedPlayerFire.EXT_PROP_NAME);
        if (extendedPlayerFire.getOnFireTick() > 0) {
            renderHolyFire.doRender(event.entity, event.x, event.y, event.z, 0, 0);
        }
    }

    private void renderBible(EntityPlayer player) {
        IIcon var10 = Castlevania.BIBLE.getIconFromDamage(0);
        if (var10 != null) {
            for (int i = 0; i < 6; i++) {
                GL11.glPushMatrix();
                float angle = (float) Math.toRadians(player.ticksExisted * 5 + i * 60 % 360);
                float x = MathHelper.sin(angle);
                float z = MathHelper.cos(angle);
                GL11.glTranslatef(x, -0.3F, z);
                double dx = player.posX - mc.thePlayer.posX;
                double dy = player.posY - mc.thePlayer.posY;
                double dz = player.posZ - mc.thePlayer.posZ;
                GL11.glTranslated(dx, dy == 0 ? 0 : dy + player.getEyeHeight() - 0.3F, dz);

                GL11.glEnable(32826);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                Minecraft.getMinecraft()
                    .getTextureManager()
                    .bindTexture(TextureMap.locationItemsTexture);
                Tessellator var11 = Tessellator.instance;
                this.func_77026_a(var11, var10);
                GL11.glDisable(32826);
                GL11.glPopMatrix();
            }
        }
    }

    private void func_77026_a(Tessellator p_77026_1_, IIcon p_77026_2_) {
        float var3 = p_77026_2_.getMinU();
        float var4 = p_77026_2_.getMaxU();
        float var5 = p_77026_2_.getMinV();
        float var6 = p_77026_2_.getMaxV();
        float var7 = 1.0F;
        float var8 = 0.5F;
        float var9 = 0.25F;
        GL11.glRotatef(180.0F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
        p_77026_1_.startDrawingQuads();
        p_77026_1_.setNormal(0.0F, 1.0F, 0.0F);
        p_77026_1_.addVertexWithUV((double) (0.0F - var8), (double) (0.0F - var9), 0.0D, (double) var3, (double) var6);
        p_77026_1_.addVertexWithUV((double) (var7 - var8), (double) (0.0F - var9), 0.0D, (double) var4, (double) var6);
        p_77026_1_.addVertexWithUV((double) (var7 - var8), (double) (var7 - var9), 0.0D, (double) var4, (double) var5);
        p_77026_1_.addVertexWithUV((double) (0.0F - var8), (double) (var7 - var9), 0.0D, (double) var3, (double) var5);
        p_77026_1_.draw();
    }

}
