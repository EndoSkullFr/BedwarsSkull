package fr.endoskull.bedwars.utils.tower;

import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.MessagesUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NewPlaceBlock {
  public NewPlaceBlock(Block b, String xyz, DyeColor color, Player p, boolean ladder, int ladderdata) {
    int x = Integer.parseInt(xyz.split(", ")[0]);
    int y = Integer.parseInt(xyz.split(", ")[1]);
    int z = Integer.parseInt(xyz.split(", ")[2]);

    Arena game = GameUtils.getGame(p);
    if (game == null) return;
    if (!game.isAvaibleBlock(b.getRelative(x, y, z))) return;
    if (b.getRelative(x, y, z).getType().equals(Material.AIR)) {
      if (!ladder) {
        b.getRelative(x, y, z).setType(Material.WOOL);
        b.getRelative(x, y, z).setData(color.getData());
        GameUtils.getGame(p).getPlacedBlocks().add(b.getRelative(x, y, z));
      } else {
        b.getRelative(x, y, z).setType(Material.LADDER);
        b.getRelative(x, y, z).setData((byte)ladderdata);
        GameUtils.getGame(p).getPlacedBlocks().add(b.getRelative(x, y, z));
      } 
    } 
  }
}
