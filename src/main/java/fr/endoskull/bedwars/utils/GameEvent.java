package fr.endoskull.bedwars.utils;

public enum GameEvent {
    diamond2(360, "emerald2"),
    emerald2(360, "diamond3"),
    diamond3(360, "emerald3"),
    emerald3(360, "bedDestroy"),
    bedDestroy(360, "gameOver"),
    gameOver(600, "none");
    /*diamond2(1, "emerald2"),
    emerald2(1, "diamond3"),
    diamond3(1, "emerald3"),
    emerald3(1, "bedDestroy"),
    bedDestroy(3, "gameOver"),
    gameOver(5, "none")*/

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
