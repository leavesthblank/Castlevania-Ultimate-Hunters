package com.zr2.castlevania.properties;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.item.ItemStone;
import com.zr2.castlevania.network.packet.PacketIEEPSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ExtendedPlayerStones implements IEEPSyncable {

    public static final String EXT_PROP_NAME = "ExtendedPlayerStones";
    private final Entity entity;

    private short abilityStones = 0;

    public ExtendedPlayerStones(EntityPlayer player) {
        this.entity = player;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        compound.setShort("abilityStone", abilityStones);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        abilityStones = compound.getShort("abilityStone");
    }

    @Override
    public void init(Entity entity, World world) {

    }

    public boolean isActive(ItemStone itemStone) {
        return ((abilityStones >> (itemStone.getIndex())) & 1) == 1;
    }

    public void setActive(ItemStone itemStone) {
        abilityStones |= (1 << itemStone.getIndex());
        Castlevania.getNetChannel().sendToAll(new PacketIEEPSync(this));
    }

    public void setInactive(ItemStone itemStone) {
        abilityStones &= ~(1 << itemStone.getIndex());
        Castlevania.getNetChannel().sendToAll(new PacketIEEPSync(this));
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
