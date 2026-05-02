package com.zr2.castlevania.event.server;

import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.zr2.castlevania.entity.AbstractEntityProjectile;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ServerKnifeHurtEventEventHandler {

    @SubscribeEvent
    public void onHit(LivingHurtEvent event) {
        if (event.source.getSourceOfDamage() instanceof AbstractEntityProjectile) {
            AbstractEntityProjectile entity = (AbstractEntityProjectile) event.source.getSourceOfDamage();
            event.ammount = (float) entity.getDamage();
        }
    }

}
