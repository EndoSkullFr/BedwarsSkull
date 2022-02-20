package fr.endoskull.bedwars.utils.bedwars;

import fr.endoskull.bedwars.utils.GameEvent;
import fr.endoskull.bedwars.utils.GameState;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;

public class Arena {

    private String name;
    private World world;
    private int borderSize;
    private BedwarsLocation lobby;
    private BedwarsLocation corner1;
    private BedwarsLocation corner2;
    private GameState gameState;
    private int startTimer;
    private GameEvent gameEvent;
    private int eventTimer;

    private List<Team> teams;
    private HashMap<Team, BedwarsLocation> spawns;
    private HashMap<Team, BedwarsLocation> beds;
    private HashMap<Team, BedwarsLocation> generators;
    private HashMap<Team, BedwarsLocation> shops;
    private HashMap<Team, BedwarsLocation> upgrades;
    private List<BedwarsLocation> emeraldGenerators;
    private List<BedwarsLocation> diamondGenerators;

    public Arena(String name, World world, int borderSize, BedwarsLocation lobby, BedwarsLocation corner1, BedwarsLocation corner2, GameState gameState, int startTimer, GameEvent gameEvent, int eventTimer, List<Team> teams, HashMap<Team, BedwarsLocation> spawns, HashMap<Team, BedwarsLocation> beds, HashMap<Team, BedwarsLocation> generators, HashMap<Team, BedwarsLocation> shops, HashMap<Team, BedwarsLocation> upgrades, List<BedwarsLocation> emeraldGenerators, List<BedwarsLocation> diamondGenerators) {
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
        this.teams = teams;
        this.spawns = spawns;
        this.beds = beds;
        this.generators = generators;
        this.shops = shops;
        this.upgrades = upgrades;
        this.emeraldGenerators = emeraldGenerators;
        this.diamondGenerators = diamondGenerators;
    }

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
}
