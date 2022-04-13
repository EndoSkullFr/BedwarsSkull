package fr.endoskull.bedwars.utils;

public enum GameEvent {
    diamond2(10, "emerald2"),
    emerald2(10, "diamond3"),
    diamond3(10, "emerald3"),
    emerald3(10, "bedDestroy"),
    bedDestroy(10, "gameOver"),
    gameOver(10, "none");

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
