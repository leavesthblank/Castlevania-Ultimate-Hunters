package com.zr2.castlevania.render.shader;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zr2.castlevania.Castlevania;

public class ShaderZaWarudo extends ShaderGroup {

    private static final JsonElement INVERT, BLIT;
    private static final ResourceLocation FAKE_PATH = new ResourceLocation(
        Castlevania.MODID,
        "shaders/post/zawarudo.json");

    static {
        JsonParser jsonParser = new JsonParser();
        INVERT = jsonParser.parse(
            "{\"name\":\"invert\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"uniforms\":[{\"name\":\"InverseAmount\",\"values\":[0.8]}]}");
        BLIT = jsonParser.parse("{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}");
    }

    private final Framebuffer mainFramebuffer;
    private final IResourceManager resourceManager;
    private final String shaderGroupName;
    private final List listShaders = Lists.newArrayList();
    private final Map mapFramebuffers_ = new HashMap();
    private final List listFramebuffers = Lists.newArrayList();
    private Matrix4f projectionMatrix;
    private int mainFramebufferWidth;
    private int mainFramebufferHeight;
    private float field_148036_j;
    private float field_148037_k;

    public ShaderZaWarudo(TextureManager p_i1050_1_, IResourceManager p_i1050_2_, Framebuffer p_i1050_3_)
        throws JsonException {
        super(p_i1050_1_, p_i1050_2_, p_i1050_3_, FAKE_PATH);
        this.resourceManager = p_i1050_2_;
        this.mainFramebuffer = p_i1050_3_;
        this.field_148036_j = 0.0F;
        this.field_148037_k = 0.0F;
        this.mainFramebufferWidth = p_i1050_3_.framebufferWidth;
        this.mainFramebufferHeight = p_i1050_3_.framebufferHeight;
        this.shaderGroupName = FAKE_PATH.toString();
        this.resetProjectionMatrix();
        this.func_152765_a(p_i1050_1_, null);
    }

    public void func_152765_a(TextureManager p_152765_1_, ResourceLocation p_152765_2_) {
        // this.initTarget(var10); "swap"
        this.addFramebuffer("swap", this.mainFramebufferWidth, this.mainFramebufferHeight);

        // this.func_152764_a(p_152765_1_, var10);
        // {"name":"invert","intarget":"minecraft:main","outtarget":"swap","uniforms":[{"name":"InverseAmount","values":[0.8]}]}
        // this.func_152764_a(p_152765_1_, var10); {"name":"blit","intarget":"swap","outtarget":"minecraft:main"}
        try {
            this.func_152764_a(p_152765_1_, INVERT);
            this.func_152764_a(p_152765_1_, BLIT);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void initTarget(JsonElement p_initTarget_1_) throws JsonException {
        if (JsonUtils.jsonElementTypeIsString(p_initTarget_1_)) {
            this.addFramebuffer(p_initTarget_1_.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
        } else {
            JsonObject var2 = JsonUtils.getJsonElementAsJsonObject(p_initTarget_1_, "target");
            String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
            int var4 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var2, "width", this.mainFramebufferWidth);
            int var5 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var2, "height", this.mainFramebufferHeight);
            if (this.mapFramebuffers_.containsKey(var3)) {
                throw new JsonException(var3 + " is already defined");
            }

            this.addFramebuffer(var3, var4, var5);
        }

    }

    private void func_152764_a(TextureManager p_152764_1_, JsonElement p_152764_2_) throws JsonException {
        JsonObject var3 = JsonUtils.getJsonElementAsJsonObject(p_152764_2_, "pass");
        String var4 = JsonUtils.getJsonObjectStringFieldValue(var3, "name");
        String var5 = JsonUtils.getJsonObjectStringFieldValue(var3, "intarget");
        String var6 = JsonUtils.getJsonObjectStringFieldValue(var3, "outtarget");
        Framebuffer var7 = this.getFramebuffer(var5);
        Framebuffer var8 = this.getFramebuffer(var6);
        if (var7 == null) {
            throw new JsonException("Input target '" + var5 + "' does not exist");
        } else if (var8 == null) {
            throw new JsonException("Output target '" + var6 + "' does not exist");
        } else {
            Shader var9 = this.addShader(var4, var7, var8);
            JsonArray var10 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var3, "auxtargets", (JsonArray) null);
            if (var10 != null) {
                int var11 = 0;

                for (Iterator var12 = var10.iterator(); var12.hasNext(); ++var11) {
                    JsonElement var13 = (JsonElement) var12.next();

                    try {
                        JsonObject var14 = JsonUtils.getJsonElementAsJsonObject(var13, "auxtarget");
                        String var30 = JsonUtils.getJsonObjectStringFieldValue(var14, "name");
                        String var16 = JsonUtils.getJsonObjectStringFieldValue(var14, "id");
                        Framebuffer var17 = this.getFramebuffer(var16);
                        if (var17 == null) {
                            ResourceLocation var18 = new ResourceLocation("textures/effect/" + var16 + ".png");

                            try {
                                this.resourceManager.getResource(var18);
                            } catch (FileNotFoundException var24) {
                                throw new JsonException("Render target or texture '" + var16 + "' does not exist");
                            }

                            p_152764_1_.bindTexture(var18);
                            ITextureObject var19 = p_152764_1_.getTexture(var18);
                            int var20 = JsonUtils.getJsonObjectIntegerFieldValue(var14, "width");
                            int var21 = JsonUtils.getJsonObjectIntegerFieldValue(var14, "height");
                            boolean var22 = JsonUtils.getJsonObjectBooleanFieldValue(var14, "bilinear");
                            if (var22) {
                                GL11.glTexParameteri(3553, 10241, 9729);
                                GL11.glTexParameteri(3553, 10240, 9729);
                            } else {
                                GL11.glTexParameteri(3553, 10241, 9728);
                                GL11.glTexParameteri(3553, 10240, 9728);
                            }

                            var9.addAuxFramebuffer(var30, var19.getGlTextureId(), var20, var21);
                        } else {
                            var9.addAuxFramebuffer(
                                var30,
                                var17,
                                var17.framebufferTextureWidth,
                                var17.framebufferTextureHeight);
                        }
                    } catch (Exception var25) {
                        JsonException var15 = JsonException.func_151379_a(var25);
                        var15.func_151380_a("auxtargets[" + var11 + "]");
                        throw var15;
                    }
                }
            }

            JsonArray var26 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var3, "uniforms", (JsonArray) null);
            if (var26 != null) {
                int var27 = 0;

                for (Iterator var28 = var26.iterator(); var28.hasNext(); ++var27) {
                    JsonElement var29 = (JsonElement) var28.next();

                    try {
                        this.initUniform(var29);
                    } catch (Exception var23) {
                        JsonException var31 = JsonException.func_151379_a(var23);
                        var31.func_151380_a("uniforms[" + var27 + "]");
                        throw var31;
                    }
                }
            }

        }
    }

    private void initUniform(JsonElement p_initUniform_1_) throws JsonException {
        JsonObject var2 = JsonUtils.getJsonElementAsJsonObject(p_initUniform_1_, "uniform");
        String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
        ShaderUniform var4 = ((Shader) this.listShaders.get(this.listShaders.size() - 1)).getShaderManager()
            .func_147991_a(var3);
        if (var4 == null) {
            throw new JsonException("Uniform '" + var3 + "' does not exist");
        } else {
            float[] var5 = new float[4];
            int var6 = 0;
            JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(var2, "values");

            for (Iterator var8 = var7.iterator(); var8.hasNext(); ++var6) {
                JsonElement var9 = (JsonElement) var8.next();

                try {
                    var5[var6] = JsonUtils.getJsonElementFloatValue(var9, "value");
                } catch (Exception var12) {
                    JsonException var11 = JsonException.func_151379_a(var12);
                    var11.func_151380_a("values[" + var6 + "]");
                    throw var11;
                }
            }

            switch (var6) {
                case 0:
                default:
                    break;
                case 1:
                    var4.func_148090_a(var5[0]);
                    break;
                case 2:
                    var4.func_148087_a(var5[0], var5[1]);
                    break;
                case 3:
                    var4.func_148095_a(var5[0], var5[1], var5[2]);
                    break;
                case 4:
                    var4.func_148081_a(var5[0], var5[1], var5[2], var5[3]);
            }

        }
    }

    public void addFramebuffer(String p_addFramebuffer_1_, int p_addFramebuffer_2_, int p_addFramebuffer_3_) {
        Framebuffer var4 = new Framebuffer(p_addFramebuffer_2_, p_addFramebuffer_3_, true);
        var4.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
        System.out.println(this.mapFramebuffers_ + " " + p_addFramebuffer_1_ + " " + var4);
        this.mapFramebuffers_.put(p_addFramebuffer_1_, var4);
        if (p_addFramebuffer_2_ == this.mainFramebufferWidth && p_addFramebuffer_3_ == this.mainFramebufferHeight) {
            this.listFramebuffers.add(var4);
        }

    }

    public void deleteShaderGroup() {
        Iterator var1 = this.mapFramebuffers_.values()
            .iterator();

        while (var1.hasNext()) {
            Framebuffer var2 = (Framebuffer) var1.next();
            var2.deleteFramebuffer();
        }

        var1 = this.listShaders.iterator();

        while (var1.hasNext()) {
            Shader var3 = (Shader) var1.next();
            var3.deleteShader();
        }

        this.listShaders.clear();
    }

    public Shader addShader(String p_addShader_1_, Framebuffer p_addShader_2_, Framebuffer p_addShader_3_)
        throws JsonException {
        Shader var4 = new Shader(this.resourceManager, p_addShader_1_, p_addShader_2_, p_addShader_3_);
        this.listShaders.add(this.listShaders.size(), var4);
        return var4;
    }

    private void resetProjectionMatrix() {
        this.projectionMatrix = new Matrix4f();
        this.projectionMatrix.setIdentity();
        this.projectionMatrix.m00 = 2.0F / (float) this.mainFramebuffer.framebufferTextureWidth;
        this.projectionMatrix.m11 = 2.0F / (float) (-this.mainFramebuffer.framebufferTextureHeight);
        this.projectionMatrix.m22 = -0.0020001999F;
        this.projectionMatrix.m33 = 1.0F;
        this.projectionMatrix.m03 = -1.0F;
        this.projectionMatrix.m13 = 1.0F;
        this.projectionMatrix.m23 = -1.0001999F;
    }

    public void createBindFramebuffers(int p_createBindFramebuffers_1_, int p_createBindFramebuffers_2_) {
        this.mainFramebufferWidth = this.mainFramebuffer.framebufferTextureWidth;
        this.mainFramebufferHeight = this.mainFramebuffer.framebufferTextureHeight;
        this.resetProjectionMatrix();
        Iterator var3 = this.listShaders.iterator();

        while (var3.hasNext()) {
            Shader var4 = (Shader) var3.next();
            var4.setProjectionMatrix(this.projectionMatrix);
        }

        var3 = this.listFramebuffers.iterator();

        while (var3.hasNext()) {
            Framebuffer var5 = (Framebuffer) var3.next();
            var5.createBindFramebuffer(p_createBindFramebuffers_1_, p_createBindFramebuffers_2_);
        }

    }

    public void loadShaderGroup(float p_loadShaderGroup_1_) {
        if (p_loadShaderGroup_1_ < this.field_148037_k) {
            this.field_148036_j += 1.0F - this.field_148037_k;
            this.field_148036_j += p_loadShaderGroup_1_;
        } else {
            this.field_148036_j += p_loadShaderGroup_1_ - this.field_148037_k;
        }

        for (this.field_148037_k = p_loadShaderGroup_1_; this.field_148036_j > 20.0F; this.field_148036_j -= 20.0F) {}

        Iterator var2 = this.listShaders.iterator();

        while (var2.hasNext()) {
            Shader var3 = (Shader) var2.next();
            var3.loadShader(this.field_148036_j / 20.0F);
        }

    }

    // @Override
    // public void func_152765_a(TextureManager p_152765_1_, ResourceLocation p_152765_2_) throws JsonException {
    // this.initTarget(var10); "swap"
    // this.addFramebuffer("swap", this.mainFramebufferWidth, this.mainFramebufferHeight);

    // this.func_152764_a(p_152765_1_, var10);
    // {"name":"invert","intarget":"minecraft:main","outtarget":"swap","uniforms":[{"name":"InverseAmount","values":[0.8]}]}
    // this.func_152764_a(p_152765_1_, var10); {"name":"blit","intarget":"swap","outtarget":"minecraft:main"}

    // }

    private Framebuffer getFramebuffer(String p_getFramebuffer_1_) {
        if (p_getFramebuffer_1_ == null) {
            return null;
        } else {
            return p_getFramebuffer_1_.equals("minecraft:main") ? this.mainFramebuffer
                : (Framebuffer) this.mapFramebuffers_.get(p_getFramebuffer_1_);
        }
    }

}
