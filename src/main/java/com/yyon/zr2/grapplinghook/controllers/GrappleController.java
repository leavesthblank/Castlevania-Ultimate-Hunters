package com.yyon.zr2.grapplinghook.controllers;

import com.yyon.zr2.grapplinghook.GrappleMod;
import com.yyon.zr2.grapplinghook.Vec;
import com.yyon.zr2.grapplinghook.entities.GrappleArrow;
import com.yyon.zr2.grapplinghook.network.GrappleEndMessage;
import com.yyon.zr2.grapplinghook.network.PlayerMovementMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/*
 * This file is part of GrappleMod.

    GrappleMod is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    GrappleMod is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with GrappleMod.  If not, see <http://www.gnu.org/licenses/>.
 */

public class GrappleController {
    public int arrowId;
    public int entityId;
    public World world;
    public Vec pos;

    public GrappleArrow arrow;
    public Entity entity;

    public boolean attached = true;

    public double r;
    public Vec motion;

    public double playerforward = 0;
    public double playerstrafe = 0;
    public boolean playerjump = false;
    public boolean waitingonplayerjump = false;
    public Vec playermovement_unrotated = new Vec(0, 0, 0);
    public Vec playermovement = new Vec(0, 0, 0);

    //	public int counter = 0;
    public int ongroundtimer = 0;
    public int maxongroundtimer = 3;

    public int maxlen;

    public int controllerid;

    public final double playermovementmult = 0.5;

    public GrappleController(int arrowId, int entityId, World world, Vec pos, int maxlen, int controllerid) {
        this.arrowId = arrowId;
        this.entityId = entityId;
        this.world = world;
        this.pos = pos;
        this.maxlen = maxlen;

        this.controllerid = controllerid;

        this.entity = world.getEntityByID(entityId);

        this.r = this.pos.sub(Vec.positionvec(entity)).length();
        this.motion = Vec.motionvec(entity);

        this.ongroundtimer = 0;

        GrappleMod.registerController(this.entityId, this);

        if (arrowId != -1) {
            Entity arrowentity = world.getEntityByID(arrowId);
            if (arrowentity != null && !arrowentity.isDead && arrowentity instanceof GrappleArrow) {
                this.arrow = (GrappleArrow) arrowentity;
            }
        }
    }

    public void unattach() {
        if (GrappleMod.controllers.containsValue(this)) {
            this.attached = false;

            GrappleMod.unregisterController(this.entityId);

            if (this.controllerid != GrappleMod.AIRID) {
                GrappleMod.network.sendToServer(new GrappleEndMessage(this.entityId, this.arrowId));
                GrappleMod.createControl(GrappleMod.AIRID, -1, this.entityId, this.entity.worldObj, new Vec(0, 0, 0), 0, null);
            }
        }
    }

    public GrappleArrow getArrow() {
        return (GrappleArrow) world.getEntityByID(arrowId);
    }

    public void doClientTick() {
        if (this.attached) {
            if (this.entity == null || this.entity.isDead) {
                this.unattach();
            } else {
                GrappleMod.proxy.getplayermovement(this, this.entityId);
                this.updatePlayerPos();
            }
        }
    }

    public void receivePlayerMovementMessage(float strafe,
                                             float forward, boolean jump) {
        playerforward = forward;
        playerstrafe = strafe;
        if (!jump) {
            playerjump = false;
        } else if (jump && !playerjump) {
            playerjump = true;
            waitingonplayerjump = true;
        }
        playermovement_unrotated = new Vec(strafe, 0, forward);
        playermovement = playermovement_unrotated.rotate_yaw((float) (this.entity.rotationYaw * (Math.PI / 180.0)));
    }

    public boolean isjumping() {
        if (playerjump && waitingonplayerjump) {
            waitingonplayerjump = false;
            return true;
        }
        return false;
    }

    public void updatePlayerPos() {
        Entity entity = this.entity;

        if (this.attached) {
            if (entity != null) {
                if (true) {
                    this.normalGround();
                    this.normalCollisions();
//					this.applyAirFriction();

                    Vec arrowpos = this.pos;//this.getPositionVector();
                    Vec playerpos = Vec.positionvec(entity);
//					Vec3 playermotion = new Vec3(entity.motionX, entity.motionY, entity.motionZ);

                    Vec oldspherevec = playerpos.sub(arrowpos);
                    Vec spherevec = oldspherevec.changelen(r);
                    Vec spherechange = spherevec.sub(oldspherevec);
//					Vec3 spherepos = spherevec.add(arrowpos);

                    Vec additionalmotion;
                    if (arrowpos.sub(playerpos).length() < this.r) {
                        additionalmotion = new Vec(0, 0, 0);
                    } else {
                        additionalmotion = spherechange;//new Vec3(0,0,0);
                    }

                    double dist = oldspherevec.length();
                    this.calctaut(dist);

                    if (entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) entity;
                        if (this.isjumping()) {
                            this.dojump(player, spherevec);
                            return;
                        } else if (GrappleMod.proxy.isSneaking(entity)) {
                            if (arrowpos.y > playerpos.y) {
                                //							motion = multvec(motion, 0.9);
                                Vec motiontorwards = spherevec.changelen(-0.1);
                                motiontorwards = new Vec(motiontorwards.x, 0, motiontorwards.z);
                                if (motion.dot(motiontorwards) < 0) {
                                    motion.add_ip(motiontorwards);
                                }

                                Vec newmotion = dampenmotion(motion, motiontorwards);
                                motion = new Vec(newmotion.x, motion.y, newmotion.z);
                                //							motion = multvec(motion, 0.98);

                                if (this.playerforward != 0) {
                                    if (dist < maxlen || this.playerforward > 0 || maxlen == 0) {
//											double motionup = this.playerforward;
                                        additionalmotion = new Vec(0, this.playerforward, 0);
//											this.r = dist;
                                        this.r = dist;
                                        this.r -= this.playerforward * 0.3;
                                        if (this.r < 0) {
                                            this.r = dist;
                                        }
                                    }
                                }
                            }
                        } else {
                            applyPlayerMovement();
                        }
                    }

                    if (!(this.ongroundtimer > 0)) {
                        motion.add_ip(0, -0.05, 0);
                    }

                    Vec newmotion = motion.add(additionalmotion);

                    if (arrowpos.sub(playerpos.add(motion)).length() > r) { // moving away
                        motion = motion.removealong(spherevec);
                    }

//					entity.setVelocity(newmotion.xCoord, newmotion.yCoord, newmotion.zCoord);
                    entity.motionX = newmotion.x;
                    entity.motionY = newmotion.y;
                    entity.motionZ = newmotion.z;

//					if (entity instanceof EntityPlayerMP) {

//						((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity));
						
						/*
						counter++;
						if (counter > 100) {
							counter = 0;
							GrappleMod.network.sendTo(new PlayerPosMessage(entity.getEntityId(), entity.posX, entity.posY, entity.posZ), (EntityPlayerMP) entity);
						}
						*/
//					}

//					entity.fallDistance = 0;

                    this.updateServerPos();
                }
            }
        }
    }

    public void calctaut(double dist) {
        if (this.arrow != null) {
            if (dist < this.r) {
                double taut = 1 - ((this.r - dist) / 5);
                if (taut < 0) {
                    taut = 0;
                }
                this.arrow.taut = taut;
            } else {
                this.arrow.taut = 1;
            }
        }
    }

    public void normalCollisions() {
        // stop if collided with object
        if (entity.isCollidedHorizontally) {
            if (Math.abs(entity.motionX) < 0.1) {
                this.motion.x = 0;
            }
            if (Math.abs(entity.motionZ) < 0.1) {
                this.motion.z = 0;
            }
        }
        if (entity.isCollidedVertically) {
            if (Math.abs(entity.motionY) < 0.1) {
                this.motion.y = 0;
            }
        }
    }

    public void normalGround() {
        if (entity.onGround) {
            ongroundtimer = maxongroundtimer;
            if (this.motion.y < 0) {
                this.motion.y = 0;
            }
        } else {
            if (this.ongroundtimer > 0) {
                ongroundtimer--;
            }
        }
        if (this.ongroundtimer > 0) {
            if (!GrappleMod.proxy.isSneaking(entity)) {
                this.motion = Vec.motionvec(entity);
            }
        }
    }

    public void dojump(Entity player, double jumppower) {
        double maxjump = 1;
        if (ongroundtimer > 0) { // on ground: jump normally
            ongroundtimer = 20;
            return;
        }
        if (player.onGround) {
            jumppower = 0;
        }
        if (player.isCollided) {
            jumppower = maxjump;
        }
        if (jumppower < 0) {
            jumppower = 0;
        }

        this.unattach();

        if (jumppower > 0) {
            if (jumppower > player.motionY + jumppower) {
                player.motionY = jumppower;
            } else {
                player.motionY += jumppower;
            }
        }

        this.updateServerPos();
    }

    public void dojump(Entity player, Vec spherevec) {
        double maxjump = 1;
        Vec jump = new Vec(0, maxjump, 0);
        if (spherevec != null) {
            jump = jump.proj(spherevec);
        }
        double jumppower = jump.y;

        if (spherevec != null && spherevec.y > 0) {
            jumppower = 0;
        }
        if ((this.arrow != null) && r < 1 && (player.posY < this.arrow.posY)) {
            jumppower = maxjump;
        }

        this.dojump(player, jumppower);
    }

    public Vec dampenmotion(Vec motion, Vec forward) {
        Vec newmotion = motion.proj(forward);
        double dampening = 0.05;
        return new Vec(newmotion.x * dampening + motion.x * (1 - dampening), newmotion.y * dampening + motion.y * (1 - dampening), newmotion.z * dampening + motion.z * (1 - dampening));
    }

    public void updateServerPos() {
        GrappleMod.network.sendToServer(new PlayerMovementMessage(this.entityId, this.entity.posX, this.entity.posY, this.entity.posZ, this.entity.motionX, this.entity.motionY, this.entity.motionZ));
    }

    // Vector stuff:

    public void receiveGrappleClick(boolean leftclick) {
        if (!leftclick) {
            this.unattach();
        }
    }

    public void receiveEnderLaunch(double x, double y, double z) {
        this.motion.add_ip(x, y, z);
        this.entity.motionX = this.motion.x;
        this.entity.motionY = this.motion.y;
        this.entity.motionZ = this.motion.z;
    }

    public void applyAirFriction() {
        double vel = this.motion.length();
        double dragforce = vel * vel / 200;

        Vec airfric = new Vec(this.motion.x, this.motion.y, this.motion.z);
        airfric.changelen_ip(-dragforce);
        this.motion.add_ip(airfric);
    }

    public void applyPlayerMovement() {
        //System.out.println(motion.length());
        motion.add_ip(this.playermovement.changelen(0.015 + motion.length() * 0.01));//0.02 * playermovementmult));
    }
}
