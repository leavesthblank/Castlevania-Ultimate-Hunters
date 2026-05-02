package com.zr2.castlevania.properties;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.IExtendedEntityProperties;

public interface IEEPSyncable extends IExtendedEntityProperties {

    public Entity getIEEPOwner();
    public String id();

}
