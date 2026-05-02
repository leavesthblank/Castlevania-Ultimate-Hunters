package com.yyon.zr2.grapplinghook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import com.yyon.zr2.grapplinghook.controllers.AirfrictionController;
import com.yyon.zr2.grapplinghook.controllers.GrappleController;
import com.yyon.zr2.grapplinghook.controllers.HookControl;
import com.yyon.zr2.grapplinghook.entities.GrappleArrow;
import com.yyon.zr2.grapplinghook.network.GrappleAttachMessage;
import com.yyon.zr2.grapplinghook.network.GrappleAttachPosMessage;
import com.yyon.zr2.grapplinghook.network.GrappleClickMessage;
import com.yyon.zr2.grapplinghook.network.GrappleEndMessage;
import com.yyon.zr2.grapplinghook.network.MultiHookMessage;
import com.yyon.zr2.grapplinghook.network.PlayerMovementMessage;
import com.yyon.zr2.grapplinghook.network.ToolConfigMessage;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

/*
 * This file is part of GrappleMod.
 * GrappleMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * GrappleMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with GrappleMod. If not, see <http://www.gnu.org/licenses/>.
 */

@Mod(modid = GrappleMod.MODID, version = GrappleMod.VERSION)
public class GrappleMod {

    public GrappleMod() {}

    public static final String MODID = "grapplemod-castlevania";

    public static final String VERSION = "1.7.10-v10";

    public static Item grapplebowitem;

    public static Object instance;

    public static SimpleNetworkWrapper network;

    public static HashMap<Integer, GrappleController> controllers = new HashMap<Integer, GrappleController>(); // client
                                                                                                               // side
    public static HashMap<BlockPos, GrappleController> controllerpos = new HashMap<BlockPos, GrappleController>();
    public static HashSet<Integer> attached = new HashSet<Integer>(); // server side

    private static int controllerid = 0;
    public static int GRAPPLEID = controllerid++;
    public static int HOOKID = controllerid++;
    public static int MULTISUBID = controllerid++;
    public static int AIRID = controllerid++;

    public static int REPELCONFIGS = 0;
    // public static int REPELSPEED = REPELCONFIGS++;

    public static int grapplingLength = 0;
    public static boolean anyblocks = true;
    public static ArrayList<Block> grapplingblocks;
    public static boolean removeblocks = false;

    @SidedProxy(
        clientSide = "com.yyon.zr2.grapplinghook.ClientProxyClass",
        serverSide = "com.yyon.zr2.grapplinghook.ServerProxyClass")
    public static CommonProxyClass proxy;

    @EventHandler
    public void load(FMLInitializationEvent event) {}

    public void registerRenderers() {}

    public void generateNether(World world, Random random, int chunkX, int chunkZ) {}

    public void generateSurface(World world, Random random, int chunkX, int chunkZ) {}

    public int addFuel(ItemStack fuel) {
        return 0;
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.getServer()
            .worldServerForDimension(0)
            .getGameRules()
            .addGameRule("grapplingLength", "0");
        event.getServer()
            .worldServerForDimension(0)
            .getGameRules()
            .addGameRule("grapplingBlocks", "any");
        event.getServer()
            .worldServerForDimension(0)
            .getGameRules()
            .addGameRule("grapplingNonBlocks", "none");
    }

    public static void updateMaxLen(World world) {
        String s = MinecraftServer.getServer()
            .worldServerForDimension(0)
            .getGameRules()
            .getGameRuleStringValue("grapplingLength");
        if (!s.equals("")) {
            GrappleMod.grapplingLength = Integer.parseInt(s);
        }
    }

    public static void updateGrapplingBlocks(World world) {
        String s = MinecraftServer.getServer()
            .worldServerForDimension(0)
            .getGameRules()
            .getGameRuleStringValue("grapplingBlocks");
        if (s.equals("any") || s.equals("")) {
            s = MinecraftServer.getServer()
                .worldServerForDimension(0)
                .getGameRules()
                .getGameRuleStringValue("grapplingNonBlocks");
            if (s.equals("none") || s.equals("")) {
                anyblocks = true;
            } else {
                anyblocks = false;
                removeblocks = true;
            }
        } else {
            anyblocks = false;
            removeblocks = false;
        }

        if (!anyblocks) {
            String[] blockstr = s.split(",");

            grapplingblocks = new ArrayList<Block>();

            for (String str : blockstr) {
                str = str.trim();
                String modid;
                String name;
                if (str.contains(":")) {
                    String[] splitstr = str.split(":");
                    modid = splitstr[0];
                    name = splitstr[1];
                } else {
                    modid = "minecraft";
                    name = str;
                }

                Block b = GameRegistry.findBlock(modid, name);

                grapplingblocks.add(b);
            }
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        registerEntity(GrappleArrow.class, "GrappleArrow");

        proxy.preInit(event);
        network = NetworkRegistry.INSTANCE.newSimpleChannel("grapplemod-castlevania-channel");
        byte id = 0;
        network.registerMessage(PlayerMovementMessage.Handler.class, PlayerMovementMessage.class, id++, Side.SERVER);
        network.registerMessage(GrappleAttachMessage.Handler.class, GrappleAttachMessage.class, id++, Side.CLIENT);
        network.registerMessage(GrappleEndMessage.Handler.class, GrappleEndMessage.class, id++, Side.SERVER);
        network.registerMessage(GrappleClickMessage.Handler.class, GrappleClickMessage.class, id++, Side.CLIENT);
        network
            .registerMessage(GrappleAttachPosMessage.Handler.class, GrappleAttachPosMessage.class, id++, Side.CLIENT);
        network.registerMessage(MultiHookMessage.Handler.class, MultiHookMessage.class, id++, Side.SERVER);
        network.registerMessage(ToolConfigMessage.Handler.class, ToolConfigMessage.class, id++, Side.SERVER);
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        proxy.init(event, this);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    int entityID = 0;

    public void registerEntity(Class<? extends Entity> entityClass, String name) {
        EntityRegistry.registerModEntity(entityClass, name, entityID++, this, 900, 1, true);
    }

    public static void registerController(int entityId, GrappleController controller) {
        if (controllers.containsKey(entityId)) {
            controllers.get(entityId)
                .unattach();
        }

        controllers.put(entityId, controller);
    }

    public static void unregisterController(int entityId) {
        controllers.remove(entityId);
    }

    public static void receiveGrappleClick(int id, boolean leftclick) {
        GrappleController controller = controllers.get(id);
        if (controller != null) {
            controller.receiveGrappleClick(leftclick);
        } else {
            System.out.println("Couldn't find controller");
        }
    }

    public static void receiveEnderLaunch(int id, double x, double y, double z) {
        GrappleController controller = controllers.get(id);
        if (controller != null) {
            controller.receiveEnderLaunch(x, y, z);
        } else {
            System.out.println("Couldn't find controller");
        }
    }

    public static void sendtocorrectclient(IMessage message, int playerid, World w) {
        Entity entity = w.getEntityByID(playerid);
        if (entity instanceof EntityPlayerMP) {
            GrappleMod.network.sendTo(message, (EntityPlayerMP) entity);
        } else {
            System.out.println("ERROR! couldn't find player");
        }
    }

    public static GrappleController createControl(int id, int arrowid, int entityid, World world, Vec pos, int maxlen,
        BlockPos blockpos) {

        GrappleArrow arrow = null;
        Entity arrowentity = world.getEntityByID(arrowid);
        if (arrowentity != null && arrowentity instanceof GrappleArrow) {
            arrow = (GrappleArrow) arrowentity;
        }

        if (id != MULTISUBID) {
            GrappleController currentcontroller = controllers.get(entityid);
            if (currentcontroller != null) {
                currentcontroller.unattach();
            }
        }

        System.out.println(blockpos);

        GrappleController control = null;
        if (id == GRAPPLEID) {
            control = new GrappleController(arrowid, entityid, world, pos, maxlen, id);
        } else if (id == HOOKID) {
            control = new HookControl(arrowid, entityid, world, pos, maxlen, id);
        } else if (id == AIRID) {
            System.out.println("AIR FRICTION CONTROLLER");
            control = new AirfrictionController(arrowid, entityid, world, pos, maxlen, id);
        }
        if (blockpos != null && control != null) {
            GrappleMod.controllerpos.put(blockpos, control);
        }

        return control;
    }

    public static void removesubarrow(int id) {
        GrappleMod.network.sendToServer(new GrappleEndMessage(-1, id));
    }

    public static void receiveGrappleEnd(int id, World world, int arrowid) {
        if (GrappleMod.attached.contains(id)) {
            GrappleMod.attached.remove(new Integer(id));
        } else {}

        if (arrowid != -1) {
            Entity grapple = world.getEntityByID(arrowid);
            if (grapple instanceof GrappleArrow) {
                ((GrappleArrow) grapple).removeServer();
            } else {

            }
        }

        Entity entity = world.getEntityByID(id);
        if (entity != null) {
            entity.fallDistance = 0;
        }

    }

    public static void receiveMultihookMessage(int id, World w, boolean sneaking) {}

    public static void receiveToolConfigMessage(int id, World w) {}
}
