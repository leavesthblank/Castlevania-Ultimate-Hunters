package com.zr2.castlevania.network.handler.client;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonSyntaxException;
import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.event.server.ServerTickEventHandler;
import com.zr2.castlevania.network.packet.PacketTimeStopStage;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientTimeStopHandler implements IMessageHandler<PacketTimeStopStage, IMessage> {

    private static final ResourceLocation SHADER_DESATURATE = new ResourceLocation("shaders/post/desaturate.json");
    private static final ResourceLocation SHADER_INVERT = new ResourceLocation(
        Castlevania.MODID,
        "shaders/post/zawarudo.json");

    @Override
    public IMessage onMessage(PacketTimeStopStage timeStopStage, MessageContext messageContext) {
        switch (timeStopStage.getStage()) {
            case 0:
                setShader(SHADER_INVERT);
                if (ServerTickEventHandler.TICK_RATE == 0) {
                    ServerTickEventHandler.TICK_RATE = 100;
                }
                break;
            case 1:
                setShader(SHADER_DESATURATE);
                break;
            case 2:
                setShader(null);
                break;
        }
        return null;
    }

    private static void setShader(ResourceLocation shader) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityRenderer entityRenderer = mc.entityRenderer;
        if (OpenGlHelper.shadersSupported) {
            if (entityRenderer.theShaderGroup != null) {
                entityRenderer.theShaderGroup.deleteShaderGroup();
            }

            if (shader != null) {
                try {
                    entityRenderer.theShaderGroup = new ShaderGroup(
                        mc.getTextureManager(),
                        mc.getResourceManager(),
                        mc.getFramebuffer(),
                        shader);
                    entityRenderer.theShaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
                } catch (IOException | JsonSyntaxException var2) {
                    Castlevania.LOGGER.warn("Failed to load shader: " + shader, var2);
                    entityRenderer.theShaderGroup = null;
                }
            } else {
                entityRenderer.theShaderGroup = null;
            }
        }
    }

}
