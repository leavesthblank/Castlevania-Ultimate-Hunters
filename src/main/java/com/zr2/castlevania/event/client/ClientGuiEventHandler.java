package com.zr2.castlevania.event.client;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.inventory.gui.GuiStoneButton;
import com.zr2.castlevania.item.ItemStone;
import com.zr2.castlevania.network.packet.PacketUseStone;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;

public class ClientGuiEventHandler extends GuiScreen {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean hasEffect = false;

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.InitGuiEvent.Pre event) {
        if(event.gui instanceof GuiInventory || event.gui instanceof GuiContainerCreative) {
            hasEffect = !mc.thePlayer.getActivePotionEffects().isEmpty();
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
        Gui currentGui = mc.currentScreen;
        //move rendering to button

//        if((currentGui instanceof GuiInventory || currentGui instanceof GuiContainerCreative)) {
//            GuiContainer gui = (GuiContainer) currentGui;
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            mc.getTextureManager().bindTexture(BACKGROUND);
//            GL11.glDisable(GL11.GL_LIGHTING);
//            GL11.glDisable(GL11.GL_DEPTH_TEST);
//            GL11.glEnable(GL11.GL_BLEND);
//
//            float x = (gui.width + 212) / 2;
//            if(hasEffect) {
//                x += 60;
//            }
//            float y = (gui.height - 160) / 2;
//            Tessellator tessellator = Tessellator.instance;
//            tessellator.startDrawingQuads();
//            tessellator.addVertexWithUV(x + 0, y + 116, 0, 0, 1);
//            tessellator.addVertexWithUV(x + 28, y + 116, 0, 1, 1);
//            tessellator.addVertexWithUV(x + 28, y + 0, 0, 1, 0);
//            tessellator.addVertexWithUV(x + 0, y + 0, 0, 0, 0);
//            tessellator.draw();
//
//            mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
//            x += 6;
//            y += 5;
//
//            ExtendedPlayerStones playerStones = (ExtendedPlayerStones) mc.thePlayer.getExtendedProperties(ExtendedPlayerStones.EXT_PROP_NAME);
//            for(int i = 0; i < ItemStone.getAmountofStones(); ++i) {
//                ItemStone itemStone = ItemStone.STONES.get(i);
//                GL11.glColor4f(1.0F, 1.0F, 1.0F, playerStones.isActive(itemStone) ? 1F : 0.4F);
//                IIcon icon = itemStone.getIconFromDamage(0);
//                tessellator.startDrawingQuads();
//                tessellator.addVertexWithUV(x, y + 16, 0, icon.getMinU(), icon.getMaxV());
//                tessellator.addVertexWithUV(x + 16, y + 16, 0, icon.getMaxU(), icon.getMaxV());
//                tessellator.addVertexWithUV(x + 16, y, 0, icon.getMaxU(), icon.getMinV());
//                tessellator.addVertexWithUV(x, y, 0, icon.getMinU(), icon.getMinV());
//                tessellator.draw();
//                y += 18;
//            }
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            GL11.glEnable(GL11.GL_LIGHTING);
//        }
    }

    @SubscribeEvent
    public void onGuiCreate(GuiScreenEvent.InitGuiEvent.Post event) {
        if(event.gui instanceof GuiInventory || event.gui instanceof GuiContainerCreative) {
            for(int i = 0; i < ItemStone.getAmountofStones(); i++) {
                float x = (event.gui.width + 212) / 2;
                if(!mc.thePlayer.getActivePotionEffects().isEmpty()) {
                    x += 60;
                }
                float y = (event.gui.height - 160) / 2;
                GuiStoneButton button = new GuiStoneButton(329 << 16 | i, (int) x + 6, (int) y + 5 + 18 * i);
                event.buttonList.add(button);
            }
        }
    }

    @SubscribeEvent
    public void onButtonPressed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if(event.button instanceof GuiStoneButton) {
            int i = event.button.id & 0xFFFF;
            ItemStack draggedItem = mc.thePlayer.inventory.getItemStack();
            if(draggedItem != null && draggedItem.getItem() instanceof ItemStone) {
                if(((ItemStone) draggedItem.getItem()).getIndex() == i) {
                    Castlevania.getNetChannel().sendToServer(new PacketUseStone(i, true));
                    if(event.gui instanceof GuiContainerCreative) {
                        mc.thePlayer.inventory.setItemStack(null);
                    }
                }
            } else if (draggedItem == null) {
                Castlevania.getNetChannel().sendToServer(new PacketUseStone(i, false));
                if(mc.thePlayer.capabilities.isCreativeMode) {
                    mc.thePlayer.inventory.setItemStack(null);
                }
            }
        }
    }

}
