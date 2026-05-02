package com.zr2.castlevania.network.handler.server;

import com.zr2.castlevania.network.packet.PacketIEEPSync;
import com.zr2.castlevania.properties.IEEPSyncable;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class ServerIEEPSyncHandler implements IMessageHandler<PacketIEEPSync, IMessage> {

    @Override
    public IMessage onMessage(PacketIEEPSync message, MessageContext ctx) {
        sync(message, ctx.getServerHandler().playerEntity);
        return null;
    }

    protected void sync(PacketIEEPSync content, Entity entity) {
        if(entity != null) {
            IEEPSyncable ieep = (IEEPSyncable) entity.getExtendedProperties(content.getIeepId());
            try {
                ieep.loadNBTData((NBTTagCompound) JsonToNBT.func_150315_a(content.getNbt()));
            } catch (NBTException e) {
                e.printStackTrace();
            }
        }
    }

}
