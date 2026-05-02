package com.yyon.zr2.grapplinghook;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.Entity;


public class ServerProxyClass extends CommonProxyClass {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event, GrappleMod grapplemod) {
        super.init(event, grapplemod);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void handleDeath(Entity entity) {
        GrappleMod.attached.remove(new Integer(entity.getEntityId()));
    }
}
