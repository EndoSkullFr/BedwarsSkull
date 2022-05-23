package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;

import java.util.Comparator;

public class KillComparator implements Comparator<BedwarsPlayer> {

    @Override
    public int compare(BedwarsPlayer o1, BedwarsPlayer o2) {
        return Double.compare(o2.getKill(), o1.getKill());
    }
}
