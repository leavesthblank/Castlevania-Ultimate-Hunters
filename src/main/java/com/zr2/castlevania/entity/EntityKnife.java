package com.zr2.castlevania.entity;

import com.zr2.castlevania.Castlevania;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityKnife extends AbstractEntityProjectile {

    public EntityKnife(World p_i1753_1_) {
        super(p_i1753_1_);
        this.dataWatcher.addObject(17, 0F);
        this.dataWatcher.addObject(18, 0F);
        this.dataWatcher.addObject(19, 0F);
    }

    public EntityKnife(World p_i1756_1_, EntityLivingBase p_i1756_2_) {
        super(p_i1756_1_, p_i1756_2_, 10);
        Vec3 vector = p_i1756_2_.getLookVec();
        this.dataWatcher.addObject(17, (float) vector.xCoord);
        this.dataWatcher.addObject(18, (float) vector.yCoord);
        this.dataWatcher.addObject(19, (float) vector.zCoord);
    }

    @Override
    public void onUpdate() {
        this.motionX = this.dataWatcher.getWatchableObjectFloat(17);
        this.motionY = this.dataWatcher.getWatchableObjectFloat(18);
        this.motionZ = this.dataWatcher.getWatchableObjectFloat(19);
        super.onUpdate();
    }

    @Override
    protected ItemStack itemForm() {
        return new ItemStack(Castlevania.KNIFE);
    }

    @Override
    public double getDamage() {
        return 4;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
        super.writeEntityToNBT(nbtTagCompound);
        nbtTagCompound.setFloat("initMotionX", this.dataWatcher.getWatchableObjectFloat(17));
        nbtTagCompound.setFloat("initMotionY", this.dataWatcher.getWatchableObjectFloat(18));
        nbtTagCompound.setFloat("initMotionZ", this.dataWatcher.getWatchableObjectFloat(19));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
        super.readEntityFromNBT(nbtTagCompound);
        this.dataWatcher.updateObject(17, nbtTagCompound.getFloat("initMotionX"));
        this.dataWatcher.updateObject(18, nbtTagCompound.getFloat("initMotionY"));
        this.dataWatcher.updateObject(19, nbtTagCompound.getFloat("initMotionZ"));

    }
}