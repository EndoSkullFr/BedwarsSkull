package fr.endoskull.bedwars.guis;

import fr.endoskull.bedwars.utils.*;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class UpgradesGui extends CustomGui {
    public UpgradesGui(Player p) {
        super(6, MessagesUtils.UPGRADES_TEXT.getMessage(p));
        for (int i = 0; i < 54; i++) {
            setItem(i, CustomItemStack.getPane(7).setName("§r"));
        }
        Arena game = GameUtils.getGame(p);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(p.getUniqueId());
        Team team = bwPlayer.getTeam();
        if (team == null) return;
        for (Upgrades value : Upgrades.values()) {
            setItem(value.getSlot(), getItem(p, value, game, team), player -> {
                int tier = 0;
                if (team.getUpgrades().getMap().containsKey(value)) {
                    tier = team.getUpgrades().getMap().get(value);
                }
                if (tier != value.getPrice().length) {
                    if (hasMaterial(player, Material.DIAMOND, value.getPrice()[tier])) {
                        clear(player, Material.DIAMOND, value.getPrice()[tier]);
                        if (team.getUpgrades().getMap().containsKey(value)) {
                            team.getUpgrades().getMap().put(value, team.getUpgrades().getMap().get(value) + 1);
                        } else {
                            team.getUpgrades().getMap().put(value, 1);
                        }
                        for (BedwarsPlayer bwMember : game.getPlayersPerTeam(team)) {
                            bwMember.checkUpgrades();
                            Player teamMember = bwMember.getPlayer();
                            if (teamMember == null) continue;
                            teamMember.sendMessage(MessagesUtils.BOUGHT_UPGRADE.getMessage(teamMember).replace("%player%", player.getName()).replace("%upgrade%", MessagesUtils.getUpgradeName(player, value, tier)));
                        }
                        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);
                    } else {
                        player.sendMessage(MessagesUtils.CANT_AFFORD.getMessage(player));
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                    }
                }
                new UpgradesGui(player).open(player);
            });
        }
        for (Traps value : Traps.values()) {
            setItem(value.getSlot(), getTrap(p, value, team).setGlow(team.getUpgrades().getTraps().contains(value)), player -> {
                if (team.getUpgrades().getTraps().contains(value)) return;
                int traps = team.getUpgrades().getTraps().size();
                int price;
                if (traps == 0) {
                    price = 1;
                } else if (traps == 1) {
                    price = 3;
                } else if (traps == 2) {
                    price = 5;
                } else {
                    price = -1;
                }
                if (price == -1) {
                    return;
                }
                if (hasMaterial(player, Material.DIAMOND, price)) {
                    clear(player, Material.DIAMOND, price);
                    team.getUpgrades().getTraps().add(value);
                    for (BedwarsPlayer bwMember : game.getPlayersPerTeam(team)) {
                        bwMember.checkUpgrades();
                        Player teamMember = bwMember.getPlayer();
                        if (teamMember == null) continue;
                        teamMember.sendMessage(MessagesUtils.BOUGHT_UPGRADE.getMessage(teamMember).replace("%player%", player.getName()).replace("%upgrade%", MessagesUtils.getTrapName(player, value)));
                    }
                    player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);
                } else {
                    player.sendMessage(MessagesUtils.CANT_AFFORD.getMessage(player));
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                }
                new UpgradesGui(player).open(player);
            });
        }
    }

    private CustomItemStack getTrap(Player player, Traps trap, Team team) {
        if (team.getUpgrades().getTraps().contains(trap)) {
            return new CustomItemStack(trap.getItem()).setGlow().setCustomAmount(team.getUpgrades().getTraps().indexOf(trap) + 1).setName("§c" + MessagesUtils.getTrapName(player, trap)).setLore("§7" + MessagesUtils.split(MessagesUtils.getTrapDesc(player, trap)).replace("\n", "\n§7") + "\n\n" + MessagesUtils.TRAP_ALREADY.getMessage(player));
        }
        int traps = team.getUpgrades().getTraps().size();
        int price;
        if (traps == 0) {
            price = 1;
        } else if (traps == 1) {
            price = 3;
        } else if (traps == 2) {
            price = 5;
        } else {
            price = -1;
        }
        if (price == -1) {
            return new CustomItemStack(trap.getItem()).setCustomAmount(0).setName("§c" + MessagesUtils.getTrapName(player, trap)).setLore("§7" + MessagesUtils.split(MessagesUtils.getTrapDesc(player, trap)).replace("\n", "\n§7") + "\n\n" + MessagesUtils.TRAP_MAX.getMessage(player));
        }
        if (hasMaterial(player, Material.DIAMOND, price)) {
            return new CustomItemStack(trap.getItem()).setCustomAmount(0).setName("§a" + MessagesUtils.getTrapName(player, trap)).setLore("§7" + MessagesUtils.split(MessagesUtils.getTrapDesc(player, trap)).replace("\n", "\n§7") + "\n\n§7" + MessagesUtils.COST.getMessage(player) + ": §b" + price + " " + MessagesUtils.getMaterial(player, ShopItems.ShopMaterial.diamond, price) + "\n\n" + MessagesUtils.UPGRADES_BUY.getMessage(player));
        } else {
            return new CustomItemStack(trap.getItem()).setCustomAmount(0).setName("§c" + MessagesUtils.getTrapName(player, trap)).setLore("§7" + MessagesUtils.split(MessagesUtils.getTrapDesc(player, trap)).replace("\n", "\n§7") + "\n\n§7" + MessagesUtils.COST.getMessage(player) + ": §b" + price + " " + MessagesUtils.getMaterial(player, ShopItems.ShopMaterial.diamond, price) + "\n\n" + MessagesUtils.UPGRADES_LESS.getMessage(player));
        }
    }

    private CustomItemStack getItem(Player player, Upgrades value, Arena game, Team team) {
        int tier = 0;
        if (team.getUpgrades().getMap().containsKey(value)) {
            tier = team.getUpgrades().getMap().get(value);
        }
        String color;
        if (tier == value.getPrice().length || hasMaterial(player, Material.DIAMOND, value.getPrice()[tier])) {
            color = "§a";
        } else {
            color = "§c";
        }
        return new CustomItemStack(value.getItem()).setCustomAmount(tier).setGlow(tier > 0).setName(color + MessagesUtils.getUpgradeName(player, value, tier)).setLore(getDescription(player, value, game, team, tier));
    }

    private String getDescription(Player player, Upgrades value, Arena game, Team team, int tier) {
        String lore = "§7" + MessagesUtils.split(MessagesUtils.getUpgradeDesc(player, value)).replace("\n", "\n§7") + "\n\n";
        boolean tiered = value.getPrice().length > 1;
        if (tiered) {
            for (int i = 0; i < value.getPrice().length; i++) {
                boolean hasUpgrade = false;
                if (team.getUpgrades().getMap().containsKey(value)) {
                    if (team.getUpgrades().getMap().get(value) > i) {
                        hasUpgrade = true;
                    }
                }
                lore += (hasUpgrade ? "§a" : "§7") + "Tier " + (i + 1) + ": " + MessagesUtils.getUpgradeSubDesc(player, value, i+1) + ", §b" + value.getPrice()[i] + " " + MessagesUtils.getMaterial(player, ShopItems.ShopMaterial.diamond, value.getPrice()[i]) + "\n";
            }
        } else {
            lore += "§7" + MessagesUtils.COST.getMessage(player) + ": §b" + value.getPrice()[0] + " " + MessagesUtils.getMaterial(player, ShopItems.ShopMaterial.diamond, value.getPrice()[0]) + "\n";
        }
        lore += "\n";

        if (tier == value.getPrice().length) {
            return lore + MessagesUtils.UPGRADES_MAX.getMessage(player);
        }
        if (hasMaterial(player, Material.DIAMOND, value.getPrice()[tier])) {
            return lore + MessagesUtils.UPGRADES_BUY.getMessage(player);
        }
        return lore + MessagesUtils.UPGRADES_LESS.getMessage(player);
    }

    private static boolean hasMaterial(Player player, Material material, int amount) {
        PlayerInventory inv = player.getInventory();
        return inv.contains(material, amount);
    }

    private static void clear(Player player, Material material, int amount) {
        PlayerInventory inv = player.getInventory();
        inv.removeItem(new ItemStack(material, amount));
    }
}
