package com.zr2.castlevania.item;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.properties.ExtendedPlayerHeart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class BasicItem extends Item {

    public BasicItem(String name) {
        this.setCreativeTab(Castlevania.CASTLEVANIA_TAB);
        this.setUnlocalizedName(name);
        this.setTextureName(Castlevania.MODID + ":" + name);
    }

    protected static boolean consumeHeart(EntityPlayer player, int i) {
        if (!player.worldObj.isRemote) {
            ExtendedPlayerHeart playerHeart = (ExtendedPlayerHeart) player.getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);
            if (i > playerHeart.getCurrentHeart()) {
                return false;
            }
            playerHeart.setCurrentHeart(playerHeart.getCurrentHeart() - i);
            return true;
        }
        return false;
    }

}
