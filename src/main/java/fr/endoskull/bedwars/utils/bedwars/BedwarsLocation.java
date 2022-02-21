package fr.endoskull.bedwars.utils.bedwars;

import org.bukkit.Location;
import org.bukkit.World;

public class BedwarsLocation {
    private double x;
    private double y;
    private double z;
    private float yaw = 0;
    private float pitch = 0;

    public BedwarsLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BedwarsLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Location getLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }
}