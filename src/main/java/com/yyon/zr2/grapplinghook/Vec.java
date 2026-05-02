package com.yyon.zr2.grapplinghook;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class Vec {

    public double x;
    public double y;
    public double z;

    public Vec(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec(Vec3 vec) {
        this.x = vec.xCoord;
        this.y = vec.yCoord;
        this.z = vec.zCoord;
    }

    public static Vec positionvec(Entity e) {
        return new Vec(e.posX, e.posY, e.posZ);
    }

    public static Vec motionvec(Entity e) {
        return new Vec(e.motionX, e.motionY, e.motionZ);
    }

    public Vec add(Vec v2) {
        return new Vec(this.x + v2.x, this.y + v2.y, this.z + v2.z);
    }

    public void add_ip(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void add_ip(Vec v2) {
        this.x += v2.x;
        this.y += v2.y;
        this.z += v2.z;
    }

    public Vec sub(Vec v2) {
        return new Vec(this.x - v2.x, this.y - v2.y, this.z - v2.z);
    }

    public void sub_ip(Vec v2) {
        this.x -= v2.x;
        this.y -= v2.y;
        this.z -= v2.z;
    }

    public Vec rotate_yaw(double a) {
        return new Vec(
            this.x * Math.cos(a) - this.z * Math.sin(a),
            this.y,
            this.x * Math.sin(a) + this.z * Math.cos(a));
    }

    public Vec rotate_pitch(double pitch) {
        return new Vec(
            this.x,
            this.y * Math.cos(pitch) + this.z * Math.sin(pitch),
            this.z * Math.cos(pitch) - this.y * Math.sin(pitch));
    }

    public Vec mult(double changefactor) {
        return new Vec(this.x * changefactor, this.y * changefactor, this.z * changefactor);
    }

    public void mult_ip(double changefactor) {
        this.x *= changefactor;
        this.y *= changefactor;
        this.z *= changefactor;
    }

    public double length() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public Vec normalize() {
        return this.mult(1.0 / this.length());
    }

    public void normalize_ip() {
        this.mult_ip(1.0 / this.length());
    }

    public double dot(Vec v2) {
        return this.x * v2.x + this.y * v2.y + this.z * v2.z;
    }

    public Vec changelen(double l) {
        double oldl = this.length();
        if (oldl != 0) {
            double changefactor = l / oldl;
            return this.mult(changefactor);
        } else {
            return this;
        }
    }

    public void changelen_ip(double l) {
        double oldl = this.length();
        if (oldl != 0) {
            double changefactor = l / oldl;
            this.mult_ip(changefactor);
        }
    }

    public Vec proj(Vec v2) {
        Vec v3 = v2.normalize();
        double dot = this.dot(v3);
        return v3.changelen(dot);
    }

    public Vec removealong(Vec v2) {
        return this.sub(this.proj(v2));
    }

    public void print() {
        System.out.print("<");
        System.out.print(this.x);
        System.out.print(",");
        System.out.print(this.y);
        System.out.print(",");
        System.out.print(this.z);
        System.out.print(">\n");
    }

    public Vec add(double x, double y, double z) {
        return new Vec(this.x + x, this.y + y, this.z + z);
    }

    public double getYaw() {
        Vec norm = this.normalize();
        return Math.toDegrees(-Math.atan2(norm.x, norm.z));
    }

    public double getPitch() {
        Vec norm = this.normalize();
        return Math.toDegrees(-Math.asin(norm.y));
    }
}
