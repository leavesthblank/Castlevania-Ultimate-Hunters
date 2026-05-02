package com.zr2.castlevania.render.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class ModelPlayerSerpent extends ModelBiped {

    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
        float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        this.isRiding = false;
        this.isSneak = false;
        System.out.println("noce");
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
    }
}
