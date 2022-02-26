package fr.endoskull.bedwars.utils;

public enum GameEvent {
    diamond2(300, "emerald2"),
    emerald2(300, "diamond3"),
    diamond3(300, "emerald3"),
    emerald3(300, "bedDestroy"),
    bedDestroy(600, "gameOver"),
    gameOver(600, "none");

    GameEvent(int duration, String next) {
        this.duration = duration;
        this.next = next;
    }

    private int duration;
    private String next;

    public int getDuration() {
        return duration;
    }

    public String getNext() {
        return next;
    }
}
