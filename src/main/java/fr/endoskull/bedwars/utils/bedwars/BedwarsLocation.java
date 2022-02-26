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
    public BedwarsLocation(String[] s) {
        this.x = Double.parseDouble(s[0]);
        this.y = Double.parseDouble(s[1]);
        this.z = Double.parseDouble(s[2]);
        if (s.length > 3) {
            this.yaw = Float.parseFloat(s[3]);
            this.pitch = Float.parseFloat(s[4]);
        }
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
