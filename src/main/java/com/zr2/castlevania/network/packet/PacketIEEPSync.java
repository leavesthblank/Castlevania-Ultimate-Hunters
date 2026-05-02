package com.zr2.castlevania.network.packet;

import net.minecraft.nbt.NBTTagCompound;

import com.zr2.castlevania.properties.IEEPSyncable;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketIEEPSync implements IMessage {

    private String ieepId;
    private int entityId;
    private IEEPSyncable ieep;
    private String nbt;

    public PacketIEEPSync(IEEPSyncable ieepSyncable) {
        this.ieepId = ieepSyncable.id();
        this.entityId = ieepSyncable.getIEEPOwner()
            .getEntityId();
        this.ieep = ieepSyncable;
        NBTTagCompound nbt = new NBTTagCompound();
        ieepSyncable.saveNBTData(nbt);
        this.nbt = nbt.toString();
    }

    public PacketIEEPSync() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        byte[] content = new byte[length];
        buf.readBytes(content);
        ieepId = new String(content);
        entityId = buf.readInt();

        length = buf.readInt();
        content = new byte[length];
        buf.readBytes(content);
        this.nbt = new String(content);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ieepId.length());
        buf.writeBytes(ieepId.getBytes());
        buf.writeInt(entityId);

        buf.writeInt(nbt.length());
        buf.writeBytes(nbt.getBytes());
    }

    public int getEntityId() {
        return entityId;
    }

    public String getIeepId() {
        return ieepId;
    }

    public String getNbt() {
        return nbt;
    }
}
