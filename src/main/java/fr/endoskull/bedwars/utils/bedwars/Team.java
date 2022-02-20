package fr.endoskull.bedwars.utils.bedwars;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.List;

public class Team {

    private String name;
    private Color color;
    private ChatColor chatColor;
    private List<Player> members;
    private boolean hasBed;

    public Team(String name, Color color, ChatColor chatColor, List<Player> members, boolean hasBed) {
        this.name = name;
        this.color = color;
        this.chatColor = chatColor;
        this.members = members;
        this.hasBed = hasBed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public List<Player> getMembers() {
        return members;
    }

    public void setMembers(List<Player> members) {
        this.members = members;
    }

    public boolean isHasBed() {
        return hasBed;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }
}
