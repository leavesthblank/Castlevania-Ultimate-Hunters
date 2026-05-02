package com.zr2.castlevania.event.client;

import java.util.List;

import javax.vecmath.Vector2d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.input.Keyboard;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.entity.EntitySerpentStone;
import com.zr2.castlevania.event.StoneAbilityEvent;
import com.zr2.castlevania.network.packet.PacketUseStoneAbility;
import com.zr2.castlevania.render.RenderSerpentStone;
import com.zr2.castlevania.render.model.ModelPlayerSerpent;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientStoneAbilityEventHandler extends StoneAbilityEvent
    implements IMessageHandler<PacketUseStoneAbility, IMessage> {

    private static final Vector2d[] DIRECTIONS = new Vector2d[4];

    private static final KeyBinding KEY_DASH = new KeyBinding("key.dash", Keyboard.KEY_F, "key.categories.movement");
    private static final KeyBinding KEY_HIGH_JUMP = new KeyBinding(
        "key.high_jump",
        Keyboard.KEY_B,
        "key.categories.movement");

    private int dashCooldown = 0;
    private boolean canDoubleJump = true;
    private int inAirTime = 0;

    private final Minecraft mc = Minecraft.getMinecraft();

    public static ClientStoneAbilityEventHandler INSTANCE = new ClientStoneAbilityEventHandler();

    private ClientStoneAbilityEventHandler() {
        ClientRegistry.registerKeyBinding(KEY_DASH);
        ClientRegistry.registerKeyBinding(KEY_HIGH_JUMP);
    }

    protected void clientTick(EntityPlayer player, AbilityStone stone) {
        if (mc.thePlayer != player) {
            return;
        }
        if (!player.onGround) {
            inAirTime++;
        } else {
            inAirTime = 0;
        }
        switch (stone) {
            case SPIDER:
                List<Vec3> aabbs = this.checkClimbingWall(mc.thePlayer);
                double x = 0;
                double z = 0;
                if (!aabbs.isEmpty()) {
                    if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
                        player.motionY = 0;
                        if (isKeyPressed(mc.gameSettings.keyBindJump)) {
                            for (Vec3 aabb : aabbs) {
                                Vector2d vector = new Vector2d(aabb.xCoord, aabb.zCoord);
                                vector.normalize();
                                vector = findClosest(vector);
                                x += vector.x;
                                z += vector.y;
                            }
                            System.out.println(x / aabbs.size() + " " + z / aabbs.size());
                            mc.thePlayer.motionX = -x / aabbs.size();
                            mc.thePlayer.motionZ = -z / aabbs.size();
                            mc.thePlayer.motionY = 0.4;

                        }
                    } /*
                       * else if (player.motionY < -0.2) {
                       * player.motionY = -0.2;
                       * }
                       */
                }
                break;
            case LEAP:
                if (inAirTime > 5) {
                    if (isKeyPressed(mc.gameSettings.keyBindJump) && canDoubleJump) {
                        player.jump();
                        canDoubleJump = false;
                    }
                } else {
                    canDoubleJump = !mc.gameSettings.keyBindJump.getIsKeyPressed();
                }
                break;
            case DASH:
                if (dashCooldown > 0) {
                    dashCooldown--;
                } else if (isKeyPressed(KEY_DASH)) {
                    // Vec3 vector = player.getLookVec();
                    // player.motionX = vector.xCoord * 1.5;
                    // if (player.onGround) {
                    // player.motionY += 0.2;
                    // }
                    // player.motionZ = vector.zCoord * 1.5;
                    // dashCooldown = 10;
                    Castlevania.getNetChannel()
                        .sendToServer(new PacketUseStoneAbility(AbilityStone.DASH));
                }
                break;
            case GRYPHON:
                if (isKeyPressed(KEY_HIGH_JUMP)/* && player.onGround */) {
                    player.motionY = 1.5;
                }
                break;
        }
    }

    private ModelBiped model;
    private final ModelBiped serpentModel = new ModelPlayerSerpent();

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Pre event) {
        if (event.entity.ridingEntity instanceof EntitySerpentStone
            && !(event.renderer instanceof RenderSerpentStone)) {
            event.setCanceled(true);
        }
    }

    private static boolean isKeyPressed(KeyBinding keyBinding) {
        return keyBinding.getIsKeyPressed() && keyBinding.isPressed();
    }

    @Override
    public IMessage onMessage(PacketUseStoneAbility message, MessageContext ctx) {
        return null;
    }

    public static float orientCameraHook() {
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0
            && Minecraft.getMinecraft().thePlayer.ridingEntity instanceof EntitySerpentStone) {
            return 1.8F;
        }
        return 0;
    }

    private static Vector2d findClosest(Vector2d vector) {
        Vector2d r = null;
        double diff = 1;
        for (Vector2d dir : DIRECTIONS) {
            double dot = vector.dot(dir);
            if (dot == 1) {
                return dir;
            }
            if ((vector.x > 0 == dir.x > 0) && (vector.y > 0 == dir.y > 0)) {
                double diffn = Math.abs(1 - dot);
                if (diffn < diff) {
                    diff = diffn;
                    r = dir;
                }
            }
        }
        return r;
    }

    static {
        DIRECTIONS[0] = new Vector2d(1, 0);
        DIRECTIONS[1] = new Vector2d(0, 1);
        DIRECTIONS[2] = new Vector2d(-1, 0);
        DIRECTIONS[3] = new Vector2d(0, -1);
    }

}
