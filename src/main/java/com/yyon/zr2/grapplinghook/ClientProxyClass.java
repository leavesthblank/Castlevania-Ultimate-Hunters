package com.yyon.zr2.grapplinghook;

import com.yyon.zr2.grapplinghook.controllers.GrappleController;
import com.yyon.zr2.grapplinghook.entities.GrappleArrow;
import com.yyon.zr2.grapplinghook.entities.RenderGrappleArrow;
import com.yyon.zr2.grapplinghook.items.GrappleBow;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import org.lwjgl.input.Keyboard;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

public class ClientProxyClass extends CommonProxyClass {
    public boolean leftclick = false;
    public boolean prevleftclick = false;
    public HashMap<Integer, Long> enderlaunchtimer = new HashMap<Integer, Long>();
    public final int reusetime = 50;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }
	
	/*
	public ModelResourceLocation grapplinghookloc = new ModelResourceLocation("GrappleMod:grapplinghook", "inventory");
	public ModelResourceLocation hookshotloc = new ModelResourceLocation("GrappleMod:hookshot", "inventory");
	public ModelResourceLocation smarthookloc = new ModelResourceLocation("GrappleMod:smarthook", "inventory");
	public ModelResourceLocation smarthookropeloc = new ModelResourceLocation("GrappleMod:smarthookrope", "inventory");
	public ModelResourceLocation enderhookloc = new ModelResourceLocation("GrappleMod:enderhook", "inventory");
	public ModelResourceLocation magnetbowloc = new ModelResourceLocation("GrappleMod:magnetbow", "inventory");
	public ModelResourceLocation ropeloc = new ModelResourceLocation("GrappleMod:rope", "inventory");
	public ModelResourceLocation hookshotropeloc = new ModelResourceLocation("GrappleMod:hookshotrope", "inventory");
	public ModelResourceLocation repellerloc = new ModelResourceLocation("GrappleMod:repeller", "inventory");
	public ModelResourceLocation repelleronloc = new ModelResourceLocation("GrappleMod:repelleron", "inventory");
	public ModelResourceLocation multihookloc = new ModelResourceLocation("GrappleMod:multihook", "inventory");
	public ModelResourceLocation multihookropeloc = new ModelResourceLocation("GrappleMod:multihookrope", "inventory");
	
	private void setgrapplebowtextures(Item item, final ModelResourceLocation notinusetexture, final ModelResourceLocation inusetexture) {
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
		    @Override
		    public ModelResourceLocation getModelLocation(ItemStack stack) {
		    	if (ClientProxyClass.isactive(stack)) {
		    		return inusetexture;
		    	}
		    	return notinusetexture;
		    }
		});
		ModelBakery.registerItemVariants(item, notinusetexture);
		ModelBakery.registerItemVariants(item, inusetexture);
	}
	
	private void registerItemModels() {
		setgrapplebowtextures(GrappleMod.grapplebowitem, grapplinghookloc, ropeloc);
		setgrapplebowtextures(GrappleMod.hookshotitem, hookshotloc, hookshotropeloc);
		registerItemModel(GrappleMod.launcheritem);
		registerItemModel(GrappleMod.longfallboots);
		setgrapplebowtextures(GrappleMod.enderhookitem, enderhookloc, ropeloc);
		setgrapplebowtextures(GrappleMod.magnetbowitem, magnetbowloc, ropeloc);
		setgrapplebowtextures(GrappleMod.repelleritem, repellerloc, repelleronloc);
		setgrapplebowtextures(GrappleMod.multihookitem, multihookloc, multihookropeloc);
		setgrapplebowtextures(GrappleMod.smarthookitem, smarthookloc, smarthookropeloc);
	}

	private void registerItemModel(Item item) {
		registerItemModel(item, Item.REGISTRY.getNameForObject(item).toString());
	}

	private void registerItemModel(Item item, String modelLocation) {
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
//		ModelBakery.registerItemVariants(item, fullModelLocation); // Ensure the custom model is loaded and prevent the default model from being loaded
		ModelLoader.setCustomModelResourceLocation(item, 0, fullModelLocation);
	}
	*/

    @Override
    public void init(FMLInitializationEvent event, GrappleMod grappleModInst) {
        super.init(event, grappleModInst);
        RenderGrappleArrow rga = new RenderGrappleArrow(Items.iron_pickaxe);
        RenderingRegistry.registerEntityRenderingHandler(GrappleArrow.class, rga);
//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(GrappleMod.grapplebowitem, 0, new ModelResourceLocation("GrappleMod:grapplinghook", "inventory"));
//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(GrappleMod.hookshotitem, 0, new ModelResourceLocation("GrappleMod:hookshot", "inventory"));
//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(GrappleMod.launcheritem, 0, new ModelResourceLocation("GrappleMod:launcheritem", "inventory"));
//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(GrappleMod.longfallboots, 0, new ModelResourceLocation("GrappleMod:longfallboots", "inventory"));
//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(GrappleMod.enderhookitem, 0, new ModelResourceLocation("GrappleMod:enderhook", "inventory"));
    }

    @Override
    public void getplayermovement(GrappleController control, int playerid) {
        Entity entity = control.entity;
        if (entity instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) entity;
            control.receivePlayerMovementMessage(player.moveStrafing, player.moveForward, player.movementInput.jump);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            if (!Minecraft.getMinecraft().isGamePaused() || !Minecraft.getMinecraft().isSingleplayer()) {
                try {
                    Collection<GrappleController> controllers = GrappleMod.controllers.values();
                    for (GrappleController controller : controllers) {
                        controller.doClientTick();
                    }
                } catch (ConcurrentModificationException e) {
                    System.out.println("ConcurrentModificationException caught");
                }

                leftclick = (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindAttack) && Minecraft.getMinecraft().currentScreen == null);
                if (prevleftclick != leftclick) {
                    if (player != null) {
                        ItemStack stack = player.getHeldItem();
                        if (stack != null) {
                            Item item = stack.getItem();
                        }
                    }
                }

                prevleftclick = leftclick;

                if (player.onGround) {
                    if (enderlaunchtimer.containsKey(player.getEntityId())) {
                        long timer = player.worldObj.getTotalWorldTime() - enderlaunchtimer.get(player.getEntityId());
                        if (timer > 10) {
                            this.resetlaunchertime(player.getEntityId());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void launchplayer(EntityPlayer player) {

    }

    @Override
    public void resetlaunchertime(int playerid) {
        if (enderlaunchtimer.containsKey(playerid)) {
            enderlaunchtimer.put(playerid, (long) 0);
        }
    }

    @Override
    public boolean isSneaking(Entity entity) {
        if (entity == Minecraft.getMinecraft().thePlayer) {
            return (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak));
        } else {
            return entity.isSneaking();
        }
    }

    @Override
    public void blockbreak(BreakEvent event) {
        BlockPos pos = new BlockPos(event.x, event.y, event.z);
        if (pos != null) {
            if (GrappleMod.controllerpos.containsKey(pos)) {
                GrappleController control = GrappleMod.controllerpos.get(pos);

                control.unattach();

                GrappleMod.controllerpos.remove(pos);
            }
        }
    }

    @Override
    public void handleDeath(Entity entity) {
        int id = entity.getEntityId();
        if (GrappleMod.controllers.containsKey(id)) {
            GrappleController controller = GrappleMod.controllers.get(id);
            controller.unattach();
        }
    }

    @Override
    public String getkeyname(CommonProxyClass.keys keyenum) {
        KeyBinding binding = null;

        GameSettings gs = Minecraft.getMinecraft().gameSettings;

        if (keyenum == keys.keyBindAttack) {
            binding = gs.keyBindAttack;
        } else if (keyenum == keys.keyBindBack) {
            binding = gs.keyBindBack;
        } else if (keyenum == keys.keyBindForward) {
            binding = gs.keyBindForward;
        } else if (keyenum == keys.keyBindJump) {
            binding = gs.keyBindJump;
        } else if (keyenum == keys.keyBindLeft) {
            binding = gs.keyBindLeft;
        } else if (keyenum == keys.keyBindRight) {
            binding = gs.keyBindRight;
        } else if (keyenum == keys.keyBindSneak) {
            binding = gs.keyBindSneak;
        } else if (keyenum == keys.keyBindUseItem) {
            binding = gs.keyBindUseItem;
        }

        if (binding == null) {
            return "";
        }

        String displayname;
        int keycode = binding.getKeyCode();
        if (keycode == -99) {
            return "Right Click";
        } else if (keycode == -100) {
            return "Left Click";
        }
        try {
            displayname = Keyboard.getKeyName(keycode);
        } catch (ArrayIndexOutOfBoundsException e) {
            displayname = "???";
        }
        return displayname;
    }

    public static boolean isactive(ItemStack stack) {
        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
//		if (p.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) == stack || p.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND) == stack) {
        int entityid = p.getEntityId();
        if (GrappleMod.controllers.containsKey(entityid)) {
            Item item = stack.getItem();
            GrappleController controller = GrappleMod.controllers.get(entityid);
            return item.getClass() == GrappleBow.class && controller.controllerid == GrappleMod.GRAPPLEID;
        }
//		}
        return false;
    }
}
