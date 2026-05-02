package com.zr2.castlevania.event.client;

import com.zr2.castlevania.Castlevania;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.play.server.S23PacketBlockChange;

public class ClientConnectToServerEventHandler {

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Castlevania.LOGGER.info("Injecting ChannelHandler");
        event.manager.channel().pipeline().addFirst(new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof S23PacketBlockChange) {
                    S23PacketBlockChange packet = (S23PacketBlockChange) msg;
                    if (packet.field_148883_d == Castlevania.FAKE_WALL) {
                        return;
                    }
                }
                super.channelRead(ctx, msg);
            }
        });
    }

}
