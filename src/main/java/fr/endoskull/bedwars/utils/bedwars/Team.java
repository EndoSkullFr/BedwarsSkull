package fr.endoskull.bedwars.utils.bedwars;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private String name;
    private String displayName;
    private Color color;
    private ChatColor chatColor;
    private boolean hasBed = true;
    private boolean available = false;

    public Team(String name, String displayName, Color color, ChatColor chatColor) {
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.chatColor = chatColor;
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

    public boolean isHasBed() {
        return hasBed;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
