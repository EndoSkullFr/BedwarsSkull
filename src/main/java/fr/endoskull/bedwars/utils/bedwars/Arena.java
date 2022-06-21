package fr.endoskull.bedwars.utils.bedwars;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.Hologram;
import fr.endoskull.api.spigot.utils.Title;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.tasks.GoulagTask;
import fr.endoskull.bedwars.tasks.RespawnTask;
import fr.endoskull.bedwars.utils.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;
import java.util.*;

public class Arena {

    private String name;
    private String oldWorld;
    private World world;
    private int borderSize;
    private BedwarsLocation lobby;
    private BedwarsLocation corner1;
    private BedwarsLocation corner2;
    private BedwarsLocation goulagSpawn1;
    private BedwarsLocation goulagSpawn2;
    private BedwarsLocation goulagLoc1;
    private BedwarsLocation goulagLoc2;
    private GameState gameState = GameState.waiting;
    private int startTimer = 5;
    private GameEvent gameEvent = GameEvent.diamond2;
    private int eventTimer = 0;
    private int spawnProtection;
    private int baseRadius;
    private int heightLimit;
    private int maxTeamSize;
    private int min;
    private int timer = 0;
    private boolean needColoration;
    private SlimeWorld slimeWorld;

    private final List<Team> teams = new ArrayList<>();
    private final HashMap<Team, BedwarsLocation> spawns = new HashMap<>();
    private final HashMap<Team, BedwarsLocation> beds = new HashMap<>();
    private final HashMap<Team, BedwarsLocation> generators = new HashMap<>();
    private final HashMap<Team, ArmorStand> asGenerators = new HashMap<>();
    private final HashMap<BedwarsLocation, ArmorStand> asDiamonds = new HashMap<>();
    private final HashMap<BedwarsLocation, Hologram> diamondsHologram = new HashMap<>();
    private final HashMap<BedwarsLocation, ArmorStand> asEmeralds = new HashMap<>();
    private final HashMap<BedwarsLocation, Hologram> emeraldsHologram = new HashMap<>();
    private final HashMap<Team, BedwarsLocation> shops = new HashMap<>();
    private final HashMap<Team, BedwarsLocation> upgrades = new HashMap<>();
    private final List<BedwarsLocation> emeraldGenerators = new ArrayList<>();
    private final List<BedwarsLocation> diamondGenerators = new ArrayList<>();
    private final List<BedwarsPlayer> players = new ArrayList<>();
    private final HashMap<Team, Integer> emeraldAtBaseTimer = new HashMap<>();

    private int diamondTimer;
    private int emeraldTimer;
    private int diamondTier;
    private int emeraldTier;
    private final List<Block> placedBlocks = new ArrayList<>();
    private final List<Item> splitItems = new ArrayList<>();
    private final List<UUID> alreadyHypixel = new ArrayList<>();
    private final List<ShopItems.ShopMaterial> genRest = new ArrayList<>();
    private final List<BedwarsPlayer> waitingGoulag = new ArrayList<>();
    private final List<BedwarsPlayer> inGoulag = new ArrayList<>();

    private int goulagTimer;
    private boolean goulagOpen = true;
    private boolean goulaging = false;
    private GoulagTask goulagTask;
    private Team winner = null;

    public Arena() {}

    /*public Arena(String name, World world, int borderSize, BedwarsLocation lobby, BedwarsLocation corner1, BedwarsLocation corner2, GameState gameState, int startTimer, GameEvent gameEvent, int eventTimer, int spawnProtection, int baseRadius, int heightLimit, List<Team> teams, HashMap<Team, BedwarsLocation> spawns, HashMap<Team, BedwarsLocation> beds, HashMap<Team, BedwarsLocation> generators, HashMap<Team, BedwarsLocation> shops, HashMap<Team, BedwarsLocation> upgrades, List<BedwarsLocation> emeraldGenerators, List<BedwarsLocation> diamondGenerators) {
        this.name = name;
        this.world = world;
        this.borderSize = borderSize;
        this.lobby = lobby;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.gameState = gameState;
        this.startTimer = startTimer;
        this.gameEvent = gameEvent;
        this.eventTimer = eventTimer;
        this.spawnProtection = spawnProtection;
        this.baseRadius = baseRadius;
        this.heightLimit = heightLimit;
        this.teams = teams;
        this.spawns = spawns;
        this.beds = beds;
        this.generators = generators;
        this.shops = shops;
        this.upgrades = upgrades;
        this.emeraldGenerators = emeraldGenerators;
        this.diamondGenerators = diamondGenerators;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

    public BedwarsLocation getLobby() {
        return lobby;
    }

    public void setLobby(BedwarsLocation lobby) {
        this.lobby = lobby;
    }

    public BedwarsLocation getCorner1() {
        return corner1;
    }

    public void setCorner1(BedwarsLocation corner1) {
        this.corner1 = corner1;
    }

    public BedwarsLocation getCorner2() {
        return corner2;
    }

    public void setCorner2(BedwarsLocation corner2) {
        this.corner2 = corner2;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getStartTimer() {
        return startTimer;
    }

    public void setStartTimer(int startTimer) {
        this.startTimer = startTimer;
    }

    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public void setGameEvent(GameEvent gameEvent) {
        this.gameEvent = gameEvent;
    }

    public int getEventTimer() {
        return eventTimer;
    }

    public void setEventTimer(int eventTimer) {
        this.eventTimer = eventTimer;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public HashMap<Team, BedwarsLocation> getSpawns() {
        return spawns;
    }

    public HashMap<Team, BedwarsLocation> getBeds() {
        return beds;
    }

    public HashMap<Team, BedwarsLocation> getGenerators() {
        return generators;
    }

    public HashMap<Team, BedwarsLocation> getShops() {
        return shops;
    }

    public HashMap<Team, BedwarsLocation> getUpgrades() {
        return upgrades;
    }

    public List<BedwarsLocation> getEmeraldGenerators() {
        return emeraldGenerators;
    }


    public List<BedwarsLocation> getDiamondGenerators() {
        return diamondGenerators;
    }

    public int getSpawnProtection() {
        return spawnProtection;
    }

    public void setSpawnProtection(int spawnProtection) {
        this.spawnProtection = spawnProtection;
    }

    public int getBaseRadius() {
        return baseRadius;
    }

    public void setBaseRadius(int baseRadius) {
        this.baseRadius = baseRadius;
    }

    public Team getTeamByName(String name) {
        for (Team team : teams) {
            if (team.getColor().toString().equalsIgnoreCase(name)) return team;
        }
        return null;
    }

    public int getHeightLimit() {
        return heightLimit;
    }

    public void setHeightLimit(int heightLimit) {
        this.heightLimit = heightLimit;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    public List<BedwarsPlayer> getPlayers() {
        return players;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void addPlayer(Player player) {
        player.teleport(lobby.getLocation(world));
        InventoryUtils.setWaitingInv(player);
        for (BedwarsPlayer bedwarsPlayer : players) {
            Player gamePlayer = bedwarsPlayer.getPlayer();
            gamePlayer.showPlayer(player);
            player.showPlayer(gamePlayer);
        }
        players.add(new BedwarsPlayer(player, null, true, false, false, this));
        if (gameState == GameState.waiting && players.size() >= min) {
            gameState = GameState.starting;
        }
    }

    public List<BedwarsPlayer> getPlayersPerTeam(Team team) {
        List<BedwarsPlayer> result = new ArrayList<>();
        for (BedwarsPlayer bwPlayer : players) {
            if (bwPlayer.getTeam() != null) {
                if (bwPlayer.getTeam().equals(team)) result.add(bwPlayer);
            }
        }
        return result;
    }

    public void start() {
        gameState = GameState.playing;
        eventTimer = gameEvent.getDuration();
        diamondTier = 1;
        diamondTimer = ConfigUtils.getTieredGeneratorTimer(ShopItems.ShopMaterial.diamond, diamondTier);
        emeraldTier = 1;
        emeraldTimer = ConfigUtils.getTieredGeneratorTimer(ShopItems.ShopMaterial.emerald, emeraldTier);
        for (BedwarsLocation diamondGenerator : diamondGenerators) {
            ArmorStand as = world.spawn(diamondGenerator.getLocation(world).clone().add(0, 2, 0), ArmorStand.class);
            as.setVisible(false);
            as.setGravity(false);
            as.setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
            asDiamonds.put(diamondGenerator, as);
            Hologram hologram = new Hologram(diamondGenerator.getLocation(world).clone().add(0, 6, 0), "");
            hologram.spawn();
            diamondsHologram.put(diamondGenerator, hologram);
        }
        for (BedwarsLocation emeraldGenerator : emeraldGenerators) {
            ArmorStand as = world.spawn(emeraldGenerator.getLocation(world).clone().add(0, 2, 0), ArmorStand.class);
            as.setVisible(false);
            as.setGravity(false);
            as.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
            asEmeralds.put(emeraldGenerator, as);
            Hologram hologram = new Hologram(emeraldGenerator.getLocation(world).clone().add(0, 6, 0), "");
            hologram.spawn();
            emeraldsHologram.put(emeraldGenerator, hologram);
        }
        Collections.shuffle(players);
        int i = 0;
        for (Team team : teams) {
            if (getPlayersPerTeam(team).size() >= maxTeamSize) continue;
            if (players.size() <= i) break;
            players.get(i).setTeam(team);
            i++;
        }
        for (Team team : teams) {
            if (!getPlayersPerTeam(team).isEmpty()) team.setAvailable(true);
            if (!shops.get(team).getLocation(world).getChunk().isLoaded()) shops.get(team).getLocation(world).getChunk().load();
            NmsUtils.spawnVillager(shops.get(team).getLocation(world), "§a§lSHOP");
            if (!upgrades.get(team).getLocation(world).getChunk().isLoaded()) upgrades.get(team).getLocation(world).getChunk().load();
            NmsUtils.spawnVillager(upgrades.get(team).getLocation(world), "§a§lUPGRADES");
            ArmorStand as = world.spawn(generators.get(team).getLocation(world), ArmorStand.class);
            as.setVisible(false);
            as.setGravity(false);
            as.setMarker(true);
            asGenerators.put(team, as);
            team.setUpgrades(new TeamUpgrade(team));

            String name = oldWorld;
            if (name.length() > 8) name = name.substring(0, 8);
            org.bukkit.scoreboard.Team sbTeam = Main.getInstance().getScoreboard().registerNewTeam(teams.indexOf(team) + name + "-" + team.getName());
            sbTeam.setPrefix(team.getColor().chat() + "§l" + team.getName().substring(0, 1).toUpperCase() + " " + team.getColor().chat());
            sbTeam.setCanSeeFriendlyInvisibles(true);

            if (!team.isAvailable()) {
                beds.get(team).getLocation(world).getBlock().setType(Material.AIR);
                team.setHasBed(false);
            }
        }
        for (BedwarsPlayer bwPlayer : players) {
            bwPlayer.reset();
            final String name = oldWorld.length() > 8 ? oldWorld.substring(0, 8) : oldWorld;
            org.bukkit.scoreboard.Team team = Main.getInstance().getScoreboard().getTeams().stream().filter(t -> t.getName().equalsIgnoreCase(teams.indexOf(bwPlayer.getTeam()) + name + "-" + bwPlayer.getTeam().getName())).findFirst().orElse(null);
            if (team != null) {
                team.addPlayer(bwPlayer.getPlayer());
            }
            bwPlayer.getPlayer().getEnderChest().clear();
        }
        if (needColoration) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                for (Team team : teams) {
                    for (Block block : new Cuboid(new Location(world, spawns.get(team).getX() + baseRadius, 0, spawns.get(team).getZ() + baseRadius), new Location(world, spawns.get(team).getX() - baseRadius, 255, spawns.get(team).getZ() - baseRadius))) {
                        if (block.getType() == Material.WOOL || block.getType() == Material.STAINED_GLASS_PANE || block.getType() == Material.STAINED_GLASS || block.getType() == Material.STAINED_CLAY || block.getType() == Material.CARPET) {
                            block.setData(team.getColor().dye().getWoolData());
                        }
                        if (block.getType() == Material.BANNER) {
                            block.setData(team.getColor().dye().getDyeData());
                        }
                    }
                }
            });
        }
        for (Block block : new Cuboid(corner1.getLocation(world), corner2.getLocation(world))) {
            if (block.getType() != Material.AIR) block.setType(Material.AIR);
        }
        for (Block block : new Cuboid(goulagLoc1.getLocation(world), goulagLoc2.getLocation(world))) {
            block.setType(Material.STAINED_GLASS_PANE);
            block.setData(DyeColor.RED.getWoolData());
        }

        world.getWorldBorder().setCenter(lobby.getLocation(world));
        world.getWorldBorder().setSize(borderSize);
        int teamAmount = 0;
        for (Team team : teams) {
            if (team.isAvailable()) teamAmount++;
        }
        if (teamAmount < 3) {
            closeGoulag();
        }
    }

    public void closeGoulag() {
        goulagOpen = false;
        for (BedwarsPlayer bwPlayer : players) {
            Player player = bwPlayer.getPlayer();
            if (player == null) continue;
            if (inGoulag.contains(bwPlayer) || waitingGoulag.contains(bwPlayer)) {
                player.setHealth(0);
            }
            player.sendMessage("");
            player.sendMessage(MessagesUtils.GOULAG_CLOSE.getMessage(player));
            player.sendMessage("");
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
        }
        /**
         * todo check players in goulag
         */
    }

    public void nextEvent() {
        for (Player player : getAllPlayers()) {
            player.sendMessage(MessagesUtils.getEventBroadcast(player, gameEvent));
        }
        if (gameEvent.getNext().equalsIgnoreCase("none")) {
            goulagOpen = true;
            eventTimer = -1;
            for (BedwarsPlayer bwPlayer : players) {
                Player player = bwPlayer.getPlayer();
                if (player == null) continue;
                player.setHealth(0);
            }
            return;
        }
        if (gameEvent == GameEvent.diamond2) {
            setDiamondTier(2);
            for (Player player : getAllPlayers()) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            }
        }
        if (gameEvent == GameEvent.diamond3) {
            setDiamondTier(3);
            for (Player player : getAllPlayers()) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            }
        }
        if (gameEvent == GameEvent.emerald2) {
            setEmeraldTier(2);
            for (Player player : getAllPlayers()) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            }
        }
        if (gameEvent == GameEvent.emerald3) {
            setEmeraldTier(3);
            for (Player player : getAllPlayers()) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            }
        }
        if (gameEvent == GameEvent.bedDestroy) {
            for (Team team : teams) {
                if (team.isHasBed()) {
                    beds.get(team).getLocation(world).getBlock().setType(Material.AIR);
                    team.setHasBed(false);
                    for (BedwarsPlayer bwPlayer : getPlayersPerTeam(team)) {
                        Player player = bwPlayer.getPlayer();
                        if (player == null) return;
                        Title.sendTitle(player, 10, 40, 10, MessagesUtils.BED_BREAK_TITLE.getMessage(player), MessagesUtils.BED_BREAK_SUBTITLE.getMessage(player));
                    }
                }
            }
            for (Player player : getAllPlayers()) {
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
            }
        }
        gameEvent = GameEvent.valueOf(gameEvent.getNext());
        eventTimer = gameEvent.getDuration();
    }

    /*private void setNoAI(Entity bukkitEntity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }

    private void setSilent(Entity bukkitEntity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEntity.c(tag);
        tag.setInt("Silent", 1);
        nmsEntity.f(tag);
    }*/

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public HashMap<Team, ArmorStand> getAsGenerators() {
        return asGenerators;
    }

    public int getDiamondTimer() {
        return diamondTimer;
    }

    public void setDiamondTimer(int diamondTimer) {
        this.diamondTimer = diamondTimer;
    }

    public int getEmeraldTimer() {
        return emeraldTimer;
    }

    public void setEmeraldTimer(int emeraldTimer) {
        this.emeraldTimer = emeraldTimer;
    }

    public int getDiamondTier() {
        return diamondTier;
    }

    public void setDiamondTier(int diamondTier) {
        this.diamondTier = diamondTier;
    }

    public int getEmeraldTier() {
        return emeraldTier;
    }

    public void setEmeraldTier(int emeraldTier) {
        this.emeraldTier = emeraldTier;
    }

    public HashMap<BedwarsLocation, ArmorStand> getAsDiamonds() {
        return asDiamonds;
    }

    public HashMap<BedwarsLocation, ArmorStand> getAsEmeralds() {
        return asEmeralds;
    }

    public HashMap<BedwarsLocation, Hologram> getDiamondsHologram() {
        return diamondsHologram;
    }

    public HashMap<BedwarsLocation, Hologram> getEmeraldsHologram() {
        return emeraldsHologram;
    }

    public List<Block> getPlacedBlocks() {
        return placedBlocks;
    }

    public List<UUID> getAlreadyHypixel() {
        return alreadyHypixel;
    }

    public BedwarsLocation getGoulagSpawn1() {
        return goulagSpawn1;
    }

    public void setGoulagSpawn1(BedwarsLocation goulagSpawn1) {
        this.goulagSpawn1 = goulagSpawn1;
    }

    public BedwarsLocation getGoulagSpawn2() {
        return goulagSpawn2;
    }

    public void setGoulagSpawn2(BedwarsLocation goulagSpawn2) {
        this.goulagSpawn2 = goulagSpawn2;
    }

    public BedwarsLocation getGoulagLoc1() {
        return goulagLoc1;
    }

    public void setGoulagLoc1(BedwarsLocation goulagLoc1) {
        this.goulagLoc1 = goulagLoc1;
    }

    public BedwarsLocation getGoulagLoc2() {
        return goulagLoc2;
    }

    public void setGoulagLoc2(BedwarsLocation goulagLoc2) {
        this.goulagLoc2 = goulagLoc2;
    }

    public List<Player> getAllPlayers() {
        List<Player> result = new ArrayList<>();
        for (BedwarsPlayer bwPlayer : players) {
            Player player = bwPlayer.getPlayer();
            if (player == null) continue;
            result.add(player);
        }
        return result;
    }
    public List<Player> getIngamePlayers() {
        List<Player> result = new ArrayList<>();
        for (BedwarsPlayer bwPlayer : players) {
            Player player = bwPlayer.getPlayer();
            if (player == null || bwPlayer.isSpectator()) continue;
            result.add(player);
        }
        return result;
    }

    public boolean isAvaibleBlock(Block block) {
        if (block.getY() > heightLimit) {
            return false;
        }
        for (Team team : spawns.keySet()) {
            BedwarsLocation bwLoc = spawns.get(team);
            Location loc = bwLoc.getLocation(world);
            if (loc.distance(block.getLocation().clone().add(0.5, 0.5, 0.5)) <= spawnProtection) return false;
        }
        for (BedwarsLocation diamondGenerator : diamondGenerators) {
            Location loc = diamondGenerator.getLocation(world);
            if (loc.distance(block.getLocation().clone().add(0.5, 0.5, 0.5)) < 3) {
                return false;
            }
        }
        for (BedwarsLocation emeraldGenerator : emeraldGenerators) {
            Location loc = emeraldGenerator.getLocation(world);
            if (loc.distance(block.getLocation().clone().add(0.5, 0.5, 0.5)) < 3) {
                return false;
            }
        }
        return true;
    }

    public Player getPlayerByUUID(UUID uuid) {
        return getIngamePlayers().stream().filter(player -> player.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    public List<Item> getSplitItems() {
        return splitItems;
    }

    public List<ShopItems.ShopMaterial> getGenRest() {
        return genRest;
    }

    public HashMap<Team, Integer> getEmeraldAtBaseTimer() {
        return emeraldAtBaseTimer;
    }

    public int getGoulagTimer() {
        return goulagTimer;
    }

    public void setGoulagTimer(int goulagTimer) {
        this.goulagTimer = goulagTimer;
    }

    public boolean isGoulagOpen() {
        return goulagOpen;
    }

    public BedwarsPlayer getBwPlayerByUUID(UUID uuid) {
        return players.stream().filter(bedwarsPlayer -> bedwarsPlayer.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public void checkTeam(Team team) {
        int i = 0;
        for (BedwarsPlayer bedwarsPlayer : getPlayersPerTeam(team)) {
            if (bedwarsPlayer.isAlive()) i++;
        }
        if (i == 0) {
            for (Player player : getAllPlayers()) {
                player.sendMessage(MessagesUtils.TEAM_ELIMINATE.getMessage(player).replace("%team%", team.getColor().chat() + MessagesUtils.getTeamDisplayName(player, team.getName())));
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1);
            }
            checkWin();
        }
    }

    public void setGoulagOpen(boolean goulagOpen) {
        this.goulagOpen = goulagOpen;
    }

    public void checkWin() {
        if (gameState != GameState.playing) return;
        int teamNumber = 0;
        Team lastTeam = null;
        for (Team team : teams) {
            if (!team.isAvailable()) continue;
            int pls = 0;
            for (BedwarsPlayer bedwarsPlayer : getPlayersPerTeam(team)) {
                if (bedwarsPlayer.isAlive()) pls++;
            }
            if (pls > 0) {
                teamNumber++;
                lastTeam = team;
            }
        }
        if (teamNumber < 2) {
            gameState = GameState.finish;
            List<BedwarsPlayer> bestPlayers = new ArrayList<>();
            for (BedwarsPlayer bedwarsPlayer : players) {
                if (bedwarsPlayer.getTeam() == null) continue;
                bestPlayers.add(bedwarsPlayer);
            }
            bestPlayers.sort(new KillComparator());
            /*for (BedwarsPlayer bwP : players) {
                if (bwP.getTeam() == null) continue;
                BedwarsPlayer best = null;
                for (BedwarsPlayer bedwarsPlayer : players) {
                    if (bedwarsPlayer.getTeam() == null) continue;
                    if (bestPlayers.contains(bedwarsPlayer)) continue;
                    if (best == null) {
                        best = bedwarsPlayer;
                    } else if (bedwarsPlayer.getKill() > best.getKill()) {
                        best = bedwarsPlayer;
                    }
                }
                if (best != null) bestPlayers.add(best);
            }*/

            Team finalLastTeam = lastTeam;
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                for (Player pls : getAllPlayers()) {
                    pls.setAllowFlight(true);
                    pls.setFlying(true);
                    InventoryUtils.setSpectateInv(pls, true);
                    for (PotionEffect potionEffect : pls.getActivePotionEffects()) {
                        pls.removePotionEffect(potionEffect.getType());
                    }
                    pls.playSound(pls.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
                    pls.playSound(pls.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
                    String message = MessagesUtils.END_BROADCAST.getMessage(pls);
                    winner = finalLastTeam;
                    if (finalLastTeam == null) {
                        message = message.replace("%team%", MessagesUtils.ANYONE.getMessage(pls));
                    } else {
                        message = message.replace("%team%", finalLastTeam.getColor().chat() + MessagesUtils.getTeamDisplayName(pls, finalLastTeam.getName()));
                    }
                    if (bestPlayers.size() >= 1) {
                        message = message.replace("%first%", EndoSkullAPI.getPrefix(bestPlayers.get(0).getUuid()) + bestPlayers.get(0).getName() + " §8- §7" + bestPlayers.get(0).getKill());
                    } else {
                        message = message.replace("%first%", MessagesUtils.ANYONE.getMessage(pls));
                    }
                    if (bestPlayers.size() >= 2) {
                        message = message.replace("%second%", EndoSkullAPI.getPrefix(bestPlayers.get(1).getUuid()) + bestPlayers.get(1).getName() + " §8- §7" + bestPlayers.get(1).getKill());
                    } else {
                        message = message.replace("%second%", MessagesUtils.ANYONE.getMessage(pls));
                    }
                    if (bestPlayers.size() >= 3) {
                        message = message.replace("%third%", EndoSkullAPI.getPrefix(bestPlayers.get(2).getUuid()) + bestPlayers.get(2).getName() + " §8- §7" + bestPlayers.get(2).getKill());
                    } else {
                        message = message.replace("%third%", MessagesUtils.ANYONE.getMessage(pls));
                    }
                    pls.sendMessage(message);
                }
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    for (Player allPlayer : getAllPlayers()) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF("Lobby");
                        allPlayer.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
                    }
                    for (Player allPlayer : getAllPlayers()) {
                        if (!allPlayer.isOnline()) continue;
                        allPlayer.kickPlayer("");
                    }
                    for (Team team : teams) {
                        String name = oldWorld;
                        if (name.length() > 8) name = name.substring(0, 8);
                        org.bukkit.scoreboard.Team sbTeam = Main.getInstance().getScoreboard().getTeam(teams.indexOf(team) + name + "-" + team.getName());
                        sbTeam.unregister();

                    }
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
                        final SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
                        SlimeLoader fileLoader = plugin.getLoader("file");
                        try {
                            Bukkit.unloadWorld(slimeWorld.getName(), true);
                            fileLoader.deleteWorld(slimeWorld.getName());
                        } catch (UnknownWorldException | IOException e) {
                            e.printStackTrace();
                        }
                        //MapManager.cloneArenaWorld(this);
                        //gameState = GameState.waiting;
                        Main.getInstance().getGames().remove(this);
                        MapManager.loadArena(oldWorld);
                    }, 50);
                }, 100);
            }, 1);
        }

    }

    public List<BedwarsPlayer> getWaitingGoulag() {
        return waitingGoulag;
    }

    public void sendToGoulag(BedwarsPlayer bwPlayer) {
        Player player = bwPlayer.getPlayer();
        if (player == null) return;
        if (goulaging) {
            // TODO: goulag already
            if (!waitingGoulag.contains(bwPlayer)) waitingGoulag.add(bwPlayer);
            player.sendMessage("");
            player.sendMessage(MessagesUtils.GOULAG_ALREADY.getMessage(player));
            player.sendMessage("");
            return;
        }
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setFallDistance(0);
        player.getInventory().setHeldItemSlot(0);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setChestplate(new CustomItemStack(Material.LEATHER_CHESTPLATE).setLeatherColor(bwPlayer.getTeam().getColor().bukkitColor()).setUnbreakable());
        if (isFinalGoulag()) {
            if (bwPlayer.getTeam().getUpgrades().getMap().containsKey(Upgrades.GOULAG)) {
                player.getInventory().addItem(new CustomItemStack(Material.WOOD_AXE).addCustomEnchantment(Enchantment.DAMAGE_ALL, bwPlayer.getTeam().getUpgrades().getMap().get(Upgrades.GOULAG)).setUnbreakable());
            } else {
                player.getInventory().addItem(new CustomItemStack(Material.WOOD_AXE).setUnbreakable());
            }
        } else {
            player.getInventory().addItem(new CustomItemStack(Material.WOOD_AXE).setUnbreakable());
        }
        player.getInventory().addItem(new ItemStack(Material.BOW, 1, (byte) Material.BOW.getMaxDurability()));
        player.getInventory().addItem(new ItemStack(Material.ARROW));
        if (inGoulag.isEmpty()) {
            if (!isFinalGoulag()) {
                player.sendMessage("");
                player.sendMessage(MessagesUtils.GOULAG_WAITING.getMessage(player));
                player.sendMessage("");
            }
            player.teleport(goulagSpawn1.getLocation(world));
        } else {
            player.teleport(goulagSpawn2.getLocation(world));
        }
        inGoulag.add(bwPlayer);
        if (inGoulag.size() == 2) {
            startGoulag();
        }

    }

    private void startGoulag() {
        goulaging = true;
        goulagTask = new GoulagTask(this);
    }

    public boolean isGoulaging() {
        return goulaging;
    }

    public void setGoulaging(boolean goulaging) {
        this.goulaging = goulaging;
    }

    public List<BedwarsPlayer> getInGoulag() {
        return inGoulag;
    }

    public GoulagTask getGoulagTask() {
        return goulagTask;
    }

    public void winGoulag(BedwarsPlayer bwWinner) {
        for (Player p : getAllPlayers()) {
            p.sendMessage("");
            p.sendMessage(MessagesUtils.GOULAG_WIN.getMessage(p).replace("{PlayerColor}", bwWinner.getTeam().getColor().chat().toString()).replace("{PlayerName}", bwWinner.getPlayer().getDisplayName()));
            p.sendMessage("");
        }
        inGoulag.remove(bwWinner);
        goulaging = false;
        goulagTask.cancel();
        goulagTask = null;
        for (Block block : new Cuboid(goulagLoc1.getLocation(world), goulagLoc2.getLocation(world))) {
            block.setType(Material.STAINED_GLASS_PANE);
            block.setData(DyeColor.RED.getWoolData());
        }int teamAmount = 0;
        for (Team team : teams) {
            int size = 0;
            for (BedwarsPlayer bwPLayer : getPlayersPerTeam(team)) {
                if (bwPLayer.isAlive()) size++;
            }
            if (size > 0) teamAmount++;
        }
        if (teamAmount < 3 && !isFinalGoulag()) {
            closeGoulag();
        }
        for (BedwarsPlayer bedwarsPlayer : waitingGoulag) {
            sendToGoulag(bedwarsPlayer);
        }
        if (isFinalGoulag()) {
            if (gameState == GameState.playing) sendToGoulag(bwWinner);
        } else {
            bwWinner.reset();
        }
    }

    public boolean isFinalGoulag() {
        return (gameEvent == GameEvent.gameOver && eventTimer == -1);
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public boolean isNeedColoration() {
        return needColoration;
    }

    public void setNeedColoration(boolean needColoration) {
        this.needColoration = needColoration;
    }

    public String getOldWorld() {
        return oldWorld;
    }

    public void setOldWorld(String oldWorld) {
        this.oldWorld = oldWorld;
    }

    public SlimeWorld getSlimeWorld() {
        return slimeWorld;
    }

    public void setSlimeWorld(SlimeWorld slimeWorld) {
        this.slimeWorld = slimeWorld;
    }
}
