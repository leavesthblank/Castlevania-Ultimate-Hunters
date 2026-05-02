package com.zr2.castlevania.event.server;

import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import com.zr2.castlevania.Castlevania;
import com.zr2.castlevania.network.packet.PacketTimeStopStage;

import cpw.mods.fml.common.FMLCommonHandler;

public class ServerTickEventHandler {

    public static int TICK_RATE = 0;

    private static final Integer[] FAKE_WORLD_IDS = new Integer[0];

    public static void onEntityUpdate(Object obj) {
        Entity entity = (Entity) obj;
        if (entity.worldObj.isRemote && TICK_RATE > 0 && !canMoveInFrozenTime(entity)) {
            entity.setVelocity(0, 0, 0);
        }
    }

    private static boolean canMoveInFrozenTime(Object obj) {
        Entity entity = (Entity) obj;
        if (TICK_RATE % 3 == 0 && TICK_RATE > 80) {
            return true;
        }
        if (obj instanceof EntityPlayer) {
            return true;
        }
        return ((obj instanceof EntityArrow && ((EntityArrow) obj).shootingEntity instanceof EntityPlayer)
            || (obj instanceof EntityThrowable && ((EntityThrowable) obj).getThrower() instanceof EntityPlayer))
            && (((Entity) obj).ticksExisted++ < 2);
    }

    public static Integer[] getWorldIDs(boolean check) {
        switch (TICK_RATE) {
            case 100:
                Castlevania.getNetChannel()
                    .sendToAll(new PacketTimeStopStage(0));
                break;
            case 80:
                Castlevania.getNetChannel()
                    .sendToAll(new PacketTimeStopStage(1));
                break;
            case 0:
                Castlevania.getNetChannel()
                    .sendToAll(new PacketTimeStopStage(2));
                break;
        }

        Integer[] ids = DimensionManager.getIDs(check);
        if (TICK_RATE > 0) {
            TICK_RATE--;
            MinecraftServer server = MinecraftServer.getServer();
            for (int id : ids) {
                long j = System.nanoTime();
                if (id == 0 || server.getAllowNether()) {
                    WorldServer worldserver = DimensionManager.getWorld(id);
                    server.theProfiler.startSection(
                        worldserver.getWorldInfo()
                            .getWorldName());
                    server.theProfiler.startSection("pools");
                    server.theProfiler.endSection();

                    server.theProfiler.startSection("tick");
                    FMLCommonHandler.instance()
                        .onPreWorldTick(worldserver);

                    CrashReport crashreport;

                    try {
                        for (Object obj : worldserver.loadedEntityList.toArray()) {
                            Entity entity = (Entity) obj;
                            if (entity.isDead) {
                                continue;
                            }
                            if (canMoveInFrozenTime(entity)) {
                                entity.onUpdate();
                            } else if (entity instanceof EntityLivingBase
                                && ((EntityLivingBase) entity).hurtResistantTime > 0) {
                                    ((EntityLivingBase) entity).hurtResistantTime--;
                                }
                        }
                    } catch (Throwable var10) {
                        crashreport = CrashReport.makeCrashReport(var10, "Exception ticking world entities");
                        worldserver.addWorldInfoToCrashReport(crashreport);
                        throw new ReportedException(crashreport);
                    }

                    FMLCommonHandler.instance()
                        .onPostWorldTick(worldserver);
                    server.theProfiler.endSection();
                    server.theProfiler.startSection("tracker");
                    worldserver.getEntityTracker()
                        .updateTrackedEntities();
                    server.theProfiler.endSection();
                    server.theProfiler.endSection();
                }

                server.worldTickTimes.get(id)[server.getTickCounter() % 100] = System.nanoTime() - j;
            }
        }
        return TICK_RATE > 0 ? FAKE_WORLD_IDS : ids;
    }

}
