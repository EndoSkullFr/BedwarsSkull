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

package fr.endoskull.bedwars.utils.bedwars;

import fr.endoskull.bedwars.utils.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LastHit {

    private UUID victim;
    private UUID damager;
    private DamageType type;
    private long time;
    private static ConcurrentHashMap<UUID, LastHit> lastHit = new ConcurrentHashMap<>();

    public LastHit(Player victim, UUID damager, DamageType type, long time) {
        this.victim = victim.getUniqueId();
        this.damager = damager;
        this.type = type;
        this.time = time;
        lastHit.put(victim.getUniqueId(), this);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDamager(UUID damager) {
        this.damager = damager;
    }

    public UUID getDamager() {
        return damager;
    }

    public UUID getVictim() {
        return victim;
    }

    public void remove() {
        lastHit.remove(victim);
    }

    public long getTime() {
        return time;
    }

    public static LastHit getLastHit(Player player) {
        return lastHit.getOrDefault(player.getUniqueId(), null);
    }

    public DamageType getType() {
        return type;
    }

    public void setType(DamageType type) {
        this.type = type;
    }

    public static ConcurrentHashMap<UUID, LastHit> getLastHit() {
        return lastHit;
    }
}
