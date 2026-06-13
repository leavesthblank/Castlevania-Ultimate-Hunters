package com.zr2.castlevania.entity;

import javax.vecmath.Vector2d;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityHolyWater extends EntityThrowable {

    protected Vector2d fireDirection;
    protected Vector2d zeroVector = new Vector2d(0, 0);

    public EntityHolyWater(World world) {
        super(world);
    }

    public EntityHolyWater(World world, EntityLivingBase livingBase) {
        super(world, livingBase);
        Vec3 vector = livingBase.getLookVec();
        this.fireDirection = new Vector2d(vector.xCoord, vector.zCoord);
    }

    protected float getGravityVelocity() {
        return 0.05F;
    }

    protected float func_70182_d() {
        return 0.5F;
    }

    protected float func_70183_g() {
        return -20.0F;
    }

    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        this.worldObj
            .playAuxSFX(2002, (int) Math.round(this.posX), (int) Math.round(this.posY), (int) Math.round(this.posZ), 0);
        if (!worldObj.isRemote) {
            if (fireDirection.equals(zeroVector)) {
                for (int x = -2; x < 3; x++) {
                    for (int y = -2; y < 3; y++) {
                        if (Math.abs(x) + Math.abs(y) == 4) continue;
                        EntityHolyFire holyFire = new EntityHolyFire(this.worldObj);
                        holyFire.setDormant(10);
                        holyFire.posY = this.posY - 1;
                        holyFire.posX = x + this.posX;
                        holyFire.posZ = y + this.posZ;
                        this.worldObj.spawnEntityInWorld(holyFire);
                    }
                }
            } else {
                this.fireDirection.normalize();
                for (int x = 0; x < 6; x++) {
                    for (int y = -1; y < 2; y++) {
                        EntityHolyFire holyFire = new EntityHolyFire(this.worldObj);
                        holyFire.setDormant(x * 5);
                        Vector2d dir = mutiply(x, y);
                        holyFire.posY = this.posY - 1;
                        holyFire.posX = dir.x + this.posX;
                        holyFire.posZ = dir.y + this.posZ;
                        this.worldObj.spawnEntityInWorld(holyFire);
                    }
                }
            }
        }
        this.setDead();
    }

    private Vector2d mutiply(double x, double y) {
        return new Vector2d(
            (x * fireDirection.x) - (y * fireDirection.y),
            (y * fireDirection.x) + (x * fireDirection.y));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
        super.readEntityFromNBT(nbtTagCompound);
        nbtTagCompound.setDouble("fireDirX", this.fireDirection.x);
        nbtTagCompound.setDouble("fireDirY", this.fireDirection.y);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        double x = nbtTagCompound.getDouble("fireDirX");
        double y = nbtTagCompound.getDouble("fireDirY");
        this.fireDirection = new Vector2d(x, y);
    }
}
