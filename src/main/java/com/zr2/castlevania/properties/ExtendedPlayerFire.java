package com.zr2.castlevania.properties;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.network.packet.PacketIEEPSync;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ExtendedPlayerFire implements IEEPSyncable {

    public static String EXT_PROP_NAME = "ExtendedPlayerHolyFire";

    private final Entity entity;

    private int fireTick = 0;

    public ExtendedPlayerFire(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("holyFireTick", this.getOnFireTick());
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        fireTick = nbtTagCompound.getInteger("holyFireTick");
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public int getOnFireTick() {
        return fireTick;
    }

    public void setOnFireTick(int onFireTick) {
        fireTick = onFireTick;
        Castlevania.getNetChannel().sendToAll(new PacketIEEPSync(this));
    }

    public void onFireTick() {
        fireTick--;
    }

    @Override
    public Entity getIEEPOwner() {
        return entity;
    }

    @Override
    public String id() {
        return EXT_PROP_NAME;
    }
}
