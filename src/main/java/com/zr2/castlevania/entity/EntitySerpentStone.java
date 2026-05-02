package com.zr2.castlevania.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import javax.vecmath.Vector2d;

public class EntitySerpentStone extends EntityThrowable {

    private double originalX = Double.NaN;
    private double originalY = Double.NaN;
    private double originalZ = Double.NaN;

    public EntitySerpentStone(World p_i1776_1_) {
        super(p_i1776_1_);
    }

    public EntitySerpentStone(EntityLivingBase entity) {
        super(entity.worldObj);
        this.setSize(0.1F, 0.1F);
        this.setPosition(entity.posX, entity.posY + 0.5, entity.posZ);
        this.originalX = entity.posX;
        this.originalY = entity.posY + 0.0189;
        this.originalZ = entity.posZ;

        Vec3 vec3 = entity.getLookVec();
        Vector2d vector = new Vector2d(vec3.xCoord, vec3.zCoord);
        vector.normalize();
        //vector.scale(1);
        this.motionX = vector.x;
        this.motionZ = vector.y;
        entity.mountEntity(this);

        this.rotationYaw = entity.rotationYaw;
    }

    @Override
    protected void onImpact(MovingObjectPosition p_70184_1_) {
        if (p_70184_1_.entityHit != this.riddenByEntity || p_70184_1_.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            this.setDead();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.riddenByEntity == null) {
            this.setDead();
            return;
        }
        if (!this.worldObj.isRemote && this.getDistanceSq(originalX, originalY, originalZ) > 16) {
            this.setDead();
            return;
        }
        if (this.motionX == 0 && this.motionY == 0 && this.motionZ == 0) {
            this.setDead();
            return;
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public boolean isInvisibleToPlayer(EntityPlayer p_98034_1_) {
        return true;
    }
}
