/*
 * BedWars1058 - A bed wars mini-game.
 * Copyright (C) 2021 Andrei Dascălu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact e-mail: andrew.dascalu@gmail.com
 */

package fr.endoskull.bedwars.utils.mobs;

import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Despawnable {

    private static ConcurrentHashMap<UUID, Despawnable> despawnableMap = new ConcurrentHashMap<>();

    private LivingEntity e;
    private Team team;
    private Arena arena;
    private int despawn = 250;
    private final String name;
    private UUID uuid;
    private Player player;

    public Despawnable(LivingEntity e, Team team, Arena arena, int despawn, String name, Player player) {
        this.e = e;
        this.name = name;
        if (e == null) return;
        this.uuid = e.getUniqueId();
        this.team = team;
        this.arena = arena;
        this.player = player;
        if (despawn != 0) {
            this.despawn = despawn;
        }
        despawnableMap.put(uuid, this);
        this.setName();
    }

    public static ConcurrentHashMap<UUID, Despawnable> getDespawnableMap() {
        return despawnableMap;
    }

    public void refresh() {
        if (e.isDead() || e == null || team == null || arena == null) {
            despawnableMap.remove(uuid);
            if (arena == null){
                e.damage(e.getHealth()+100);
            }
            return;
        }
        setName();
        despawn--;
        if (despawn == 0) {
            e.damage(e.getHealth()+100);
            despawnableMap.remove(e.getUniqueId());
        }
    }

    public void setName() {
        int percentuale = (int) ((e.getHealth() * 100) / e.getMaxHealth() / 10);
        String health1 = new String(new char[percentuale]).replace("\0", "▮ ");
        String health2 = new String(new char[10 - percentuale]).replace("\0", "§7" + "▮ ");
        String health = health1 + health2;
        String name = this.name.replace("{despawn}", String.valueOf(despawn)).replace("{health}", health);
        if (team != null) {
            name = name.replace("{TeamColor}", team.getColor().chat().toString()).replace("{TeamName}", team.getName());
        }
        System.out.println(this.name);
        e.setCustomName(name);
    }

    public LivingEntity getEntity() {
        return e;
    }

    public Team getTeam() {
        return team;
    }

    public int getDespawn() {
        return despawn;
    }


    public void destroy(){
        if (getEntity() != null){
            getEntity().damage(Integer.MAX_VALUE);
        }
        team = null;
        despawnableMap.remove(uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LivingEntity) return ((LivingEntity) obj).getUniqueId().equals(e.getUniqueId());
        return false;
    }

    public Player getPlayer() {
        return player;
    }
}
