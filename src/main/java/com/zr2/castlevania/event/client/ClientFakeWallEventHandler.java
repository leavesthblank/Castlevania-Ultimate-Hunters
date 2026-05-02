package com.zr2.castlevania.event.client;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.network.packet.PacketDemandUpdate;
import com.zr2.castlevania.tile.TileBlockFakeWall;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ClientFakeWallEventHandler {

    @SubscribeEvent
    public void onWorldTick(TickEvent.ClientTickEvent event) {
        World world = Minecraft.getMinecraft().theWorld;
        if (world != null && world.isRemote && event.phase == TickEvent.Phase.START) {
            for (Object obj : world.loadedTileEntityList) {
                if (obj instanceof TileBlockFakeWall) {
                    TileBlockFakeWall tileBlockFakeWall = (TileBlockFakeWall) obj;
                    if (tileBlockFakeWall.getBlockDisguise() == Blocks.air) {
                        PacketDemandUpdate packet = new PacketDemandUpdate(tileBlockFakeWall.xCoord, tileBlockFakeWall.yCoord, tileBlockFakeWall.zCoord);
                        Castlevania.getNetChannel().sendToServer(packet);
                    } else {
                        tileBlockFakeWall.disguiseSelf();
                    }
                }
            }
        }
    }

}
