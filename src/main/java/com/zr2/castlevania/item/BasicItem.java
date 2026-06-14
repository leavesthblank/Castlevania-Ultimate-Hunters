package com.zr2.castlevania.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.config.ModConfig;
import com.zr2.castlevania.properties.ExtendedPlayerHeart;

public class BasicItem extends Item {

    public BasicItem(String name) {
        this.setCreativeTab(Castlevania.CASTLEVANIA_TAB);
        this.setUnlocalizedName(name);
        this.setTextureName(Castlevania.MODID + ":" + name);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
        super.addInformation(stack, player, list, adv);
        if (player == null) return;
        ExtendedPlayerHeart ext = (ExtendedPlayerHeart) player.getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);
        if (stack.getItem() == Castlevania.HEART_UPGRADE) {
            int applied = ext != null ? ext.getAppliedHeartUpgrades() : 0;
            String cap = ModConfig.enableInfiniteHeartLimit ? "∞" : String.valueOf(ModConfig.heartUpgradeCap);
            list.add("Applied: " + applied);
            list.add("Cap: " + cap);
        } else if (stack.getItem() == Castlevania.HEALTH_UPGRADE) {
            int applied = ext != null ? ext.getAppliedHealthUpgrades() : 0;
            String cap = ModConfig.enableInfiniteHeartLimit ? "∞" : String.valueOf(ModConfig.healthUpgradeCap);
            list.add("Applied: " + applied);
            list.add("Cap: " + cap);
        }
    }

    protected static boolean consumeHeart(EntityPlayer player, int i) {
        if (!player.worldObj.isRemote) {
            if (player.capabilities.isCreativeMode) return true;
            // If infinite hearts in survival is enabled, treat non-creative players as allowed
            if (ModConfig.infiniteHeartsInSurvival) return true;
            ExtendedPlayerHeart playerHeart = (ExtendedPlayerHeart) player
                .getExtendedProperties(ExtendedPlayerHeart.EXT_PROP_NAME);
            if (i > playerHeart.getCurrentHeart()) {
                return false;
            }
            playerHeart.setCurrentHeart(playerHeart.getCurrentHeart() - i);
            return true;
        }
        return false;
    }

}
