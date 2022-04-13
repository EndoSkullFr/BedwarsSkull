package fr.endoskull.bedwars.utils.bedwars;

import fr.endoskull.api.spigot.utils.Hologram;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.material.Wool;

import java.util.*;

public class Arena {

    private String name;
    private World world;
    private int borderSize;
    private BedwarsLocation lobby;
    private BedwarsLocation corner1;
    private BedwarsLocation corner2;
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

    private List<Team> teams = new ArrayList<>();
    private HashMap<Team, BedwarsLocation> spawns = new HashMap<>();
    private HashMap<Team, BedwarsLocation> beds = new HashMap<>();
    private HashMap<Team, BedwarsLocation> generators = new HashMap<>();
    private HashMap<Team, ArmorStand> asGenerators = new HashMap<>();
    private HashMap<BedwarsLocation, ArmorStand> asDiamonds = new HashMap<>();
    private HashMap<BedwarsLocation, Hologram> diamondsHologram = new HashMap<>();
    private HashMap<BedwarsLocation, ArmorStand> asEmeralds = new HashMap<>();
    private HashMap<BedwarsLocation, Hologram> emeraldsHologram = new HashMap<>();
    private HashMap<Team, BedwarsLocation> shops = new HashMap<>();
    private HashMap<Team, BedwarsLocation> upgrades = new HashMap<>();
    private List<BedwarsLocation> emeraldGenerators = new ArrayList<>();
    private List<BedwarsLocation> diamondGenerators = new ArrayList<>();
    private HashMap<Player, Team> players = new HashMap<>();
    private HashMap<Player, TierTool> itemsTier = new HashMap<>();

    private int diamondTimer;
    private int emeraldTimer;
    private int diamondTier;
    private int emeraldTier;
    private List<Block> placedBlocks = new ArrayList<>();
    private HashMap<Player, List<ShopItems>> alreadyBought = new HashMap<>();

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

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public HashMap<Team, BedwarsLocation> getSpawns() {
        return spawns;
    }

    public void setSpawns(HashMap<Team, BedwarsLocation> spawns) {
        this.spawns = spawns;
    }

    public HashMap<Team, BedwarsLocation> getBeds() {
        return beds;
    }

    public void setBeds(HashMap<Team, BedwarsLocation> beds) {
        this.beds = beds;
    }

    public HashMap<Team, BedwarsLocation> getGenerators() {
        return generators;
    }

    public void setGenerators(HashMap<Team, BedwarsLocation> generators) {
        this.generators = generators;
    }

    public HashMap<Team, BedwarsLocation> getShops() {
        return shops;
    }

    public void setShops(HashMap<Team, BedwarsLocation> shops) {
        this.shops = shops;
    }

    public HashMap<Team, BedwarsLocation> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(HashMap<Team, BedwarsLocation> upgrades) {
        this.upgrades = upgrades;
    }

    public List<BedwarsLocation> getEmeraldGenerators() {
        return emeraldGenerators;
    }

    public void setEmeraldGenerators(List<BedwarsLocation> emeraldGenerators) {
        this.emeraldGenerators = emeraldGenerators;
    }

    public List<BedwarsLocation> getDiamondGenerators() {
        return diamondGenerators;
    }

    public void setDiamondGenerators(List<BedwarsLocation> diamondGenerators) {
        this.diamondGenerators = diamondGenerators;
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
            if (team.getName().equalsIgnoreCase(name)) return team;
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

    public HashMap<Player, Team> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<Player, Team> players) {
        this.players = players;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void addPlayer(Player player) {
        player.teleport(lobby.getLocation(world));
        players.put(player, null);
        if (gameState == GameState.waiting && players.size() >= min) {
            gameState = GameState.starting;
        }
    }

    public List<Player> getPlayersPerTeam(Team team) {
        List<Player> result = new ArrayList<>();
        for (Player player : players.keySet()) {
            if (players.get(player) != null) {
                if (players.get(player).equals(team)) result.add(player);
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
        List<Player> pls = new ArrayList<>(this.players.keySet());
        Collections.shuffle(pls);
        int i = 0;
        for (Team team : teams) {
            if (getPlayersPerTeam(team).size() >= maxTeamSize) continue;
            players.putIfAbsent(pls.get(i), team);
        }
        for (Team team : teams) {
            if (!getPlayersPerTeam(team).isEmpty()) team.setAvailable(true);
            Villager villager = world.spawn(shops.get(team).getLocation(world), Villager.class);
            villager.setCustomName("§aSHOP");
            villager.setCustomNameVisible(true);
            villager.setProfession(Villager.Profession.FARMER);
            setNoAI(villager);
            setSilent(villager);
            ArmorStand as = world.spawn(generators.get(team).getLocation(world), ArmorStand.class);
            as.setVisible(false);
            as.setGravity(false);
            as.setMarker(true);
            asGenerators.put(team, as);
        }
        for (Player player : players.keySet()) {
            player.teleport(spawns.get(players.get(player)).getLocation(world));
            itemsTier.put(player, new TierTool(player));
            alreadyBought.put(player, new ArrayList<>());
        }
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            for (Team team : teams) {
                for (Block block : new Cuboid(new Location(world, spawns.get(team).getX() + baseRadius, 0, spawns.get(team).getZ() + baseRadius), new Location(world, spawns.get(team).getX() - baseRadius, 255, spawns.get(team).getZ() - baseRadius))) {
                    if (block.getType() == Material.WOOL || block.getType() == Material.STAINED_GLASS_PANE || block.getType() == Material.STAINED_GLASS || block.getType() == Material.STAINED_CLAY || block.getType() == Material.CARPET) {
                        block.setData(DyeColor.getByColor(team.getColor()).getWoolData());
                    }
                    if (block.getType() == Material.BANNER) {
                        block.setData(DyeColor.getByColor(team.getColor()).getDyeData());
                    }
                }
            }
        });
        for (Block block : new Cuboid(corner1.getLocation(world), corner2.getLocation(world))) {
            if (block.getType() != Material.AIR) block.setType(Material.AIR);
        }
    }

    public void nextEvent() {
        if (gameEvent.getNext().equalsIgnoreCase("none")) {
            /**
             * todo end the game
             */
            return;
        }
        gameEvent = GameEvent.valueOf(gameEvent.getNext());
        eventTimer = gameEvent.getDuration();
        /**
         * todo broadcast event
         */
    }

    private void setNoAI(Entity bukkitEntity) {
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
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public HashMap<Team, ArmorStand> getAsGenerators() {
        return asGenerators;
    }

    public HashMap<Player, TierTool> getItemsTier() {
        return itemsTier;
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

    public HashMap<Player, List<ShopItems>> getAlreadyBought() {
        return alreadyBought;
    }
}
