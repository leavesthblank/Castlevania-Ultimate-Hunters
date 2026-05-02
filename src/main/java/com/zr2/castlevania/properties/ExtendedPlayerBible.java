package com.zr2.castlevania.properties;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.network.packet.PacketIEEPSync;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ExtendedPlayerBible implements IEEPSyncable {

    public static final String EXT_PROP_NAME = "ExtendedPlayerBible";
    private final DataWatcher dataWatcher;
    private final Entity entity;

    private int bibleTick = 0;

    public ExtendedPlayerBible(EntityPlayer player) {
        this.dataWatcher = player.getDataWatcher();
        this.entity = player;
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("isUsingBible", bibleTick);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        bibleTick = nbtTagCompound.getInteger("isUsingBible");
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public boolean isUsingBible() {
        return bibleTick > 0;
    }

    public void useBible() {
        bibleTick = 100;
        Castlevania.getNetChannel().sendToAll(new PacketIEEPSync(this));
    }

    public void tick() {
        if (isUsingBible()) {
            bibleTick--;
        }
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
