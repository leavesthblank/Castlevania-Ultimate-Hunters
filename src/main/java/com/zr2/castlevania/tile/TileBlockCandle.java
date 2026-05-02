package com.zr2.castlevania.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileBlockCandle extends TileEntity {

    private int burnoutTick = 0;

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        if (burnoutTick <= 60 * 20) {
            burnoutTick++;
        }
    }

    public void burn() {
        burnoutTick = 60 * 20;
    }

    public void burnout() {
        burnoutTick = 0;
    }

    public boolean isBruning() {
        return burnoutTick >= 60 * 20;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.burnoutTick = nbtTagCompound.getInteger("burnoutTick");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("burnoutTick", this.burnoutTick);
    }

}
