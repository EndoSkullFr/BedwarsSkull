package fr.endoskull.bedwars.utils.bedwars;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.Cuboid;
import fr.endoskull.bedwars.utils.GameEvent;
import fr.endoskull.bedwars.utils.GameState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
    private int startTimer = 10;
    private GameEvent gameEvent = GameEvent.diamond2;
    private int eventTimer = 0;
    private int spawnProtection;
    private int baseRadius;
    private int heightLimit;
    private int maxTeamSize;
    private int min;

    private List<Team> teams = new ArrayList<>();
    private HashMap<Team, BedwarsLocation> spawns = new HashMap<>();
    private HashMap<Team, BedwarsLocation> beds = new HashMap<>();
    private HashMap<Team, BedwarsLocation> generators = new HashMap<>();
    private HashMap<Team, BedwarsLocation> shops = new HashMap<>();
    private HashMap<Team, BedwarsLocation> upgrades = new HashMap<>();
    private List<BedwarsLocation> emeraldGenerators = new ArrayList<>();
    private List<BedwarsLocation> diamondGenerators = new ArrayList<>();
    private HashMap<Player, Team> players = new HashMap<>();

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
        List<Player> pls = new ArrayList<>(this.players.keySet());
        Collections.shuffle(pls);
        int i = 0;
        for (Team team : teams) {
            if (getPlayersPerTeam(team).size() >= maxTeamSize) continue;
            if (this.players.get(pls.get(i)) == null) {
                this.players.put(pls.get(i), team);
            }
        }
        for (Team team : teams) {
            if (!getPlayersPerTeam(team).isEmpty()) team.setAvaible(true);
        }
        for (Player player : players.keySet()) {
            player.teleport(spawns.get(players.get(player)).getLocation(world));
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
    }
}
