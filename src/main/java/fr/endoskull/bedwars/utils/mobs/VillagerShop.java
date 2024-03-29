package fr.endoskull.bedwars.utils.mobs;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;

public class VillagerShop extends EntityVillager {
        @SuppressWarnings("rawtypes")
        public VillagerShop(Location loc) {
            super(((CraftWorld) loc.getWorld()).getHandle());
            try {
                Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
                bField.setAccessible(true);
                Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
                cField.setAccessible(true);
                bField.set(this.goalSelector, new UnsafeList<>());
                bField.set(this.targetSelector, new UnsafeList());
                cField.set(this.goalSelector, new UnsafeList());
                cField.set(this.targetSelector, new UnsafeList());
            } catch (Exception ignored) {
            }
            this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            this.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            (((CraftWorld) loc.getWorld()).getHandle()).addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
            persistent = true;
        }

        public void move(double d0, double d1, double d2) {
        }

        public void collide(net.minecraft.server.v1_8_R3.Entity entity) {
        }

        public boolean damageEntity(DamageSource damagesource, float f) {
            return false;
        }

        public void g(double d0, double d1, double d2) {
        }

        public void makeSound(String s, float f, float f1) {

        }

        protected void initAttributes() {
            super.initAttributes();
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
        }
    }