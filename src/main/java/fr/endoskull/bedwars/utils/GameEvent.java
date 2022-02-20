package fr.endoskull.bedwars.utils;

public enum GameEvent {
    diamond2(300),
    emerald2(300),
    diamond3(300),
    emerald3(300),
    bedDestroy(600),
    gameOver(600);

    GameEvent(int duration) {
        this.duration = duration;
    }

    private int duration;
}
