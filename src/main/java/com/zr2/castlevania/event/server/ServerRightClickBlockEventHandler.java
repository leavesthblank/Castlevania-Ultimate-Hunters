package com.zr2.castlevania.event.server;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.tile.TileBlockFakeWall;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ServerRightClickBlockEventHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.world.isRemote && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
            if (itemStack != null && itemStack.getItem() == Castlevania.FAKE_WALL_WAND) {
                Block block = event.world.getBlock(event.x, event.y, event.z);
                if (block != Castlevania.FAKE_WALL) {
                    int metadata = event.world.getBlockMetadata(event.x, event.y, event.z);
                    event.world.setBlock(event.x, event.y, event.z, Castlevania.FAKE_WALL);
                    TileBlockFakeWall tileBlockFakeWall = (TileBlockFakeWall) event.world
                        .getTileEntity(event.x, event.y, event.z);
                    tileBlockFakeWall.setBlockDisguise(block);
                    tileBlockFakeWall.setMetadata(metadata);
                }
            }
        }
    }

}
