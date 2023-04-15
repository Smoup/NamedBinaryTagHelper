package main.nbth;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.ChatColor.*;


public class BukkitEventHandler implements Listener {
    private final Map<String, Boolean> interactFlag = new HashMap<>();
    @EventHandler
    public void onClickBlock (PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (NamedBinaryTagHelper.getEnable().get(player.getName()) == null) {
            return;
        }
        if (NamedBinaryTagHelper.getEnable().get(player.getName())) {
            if (interactFlag.getOrDefault(player.getName(), false)) {
                event.setCancelled(true);
                return;
            }
            interactFlag.put(player.getName(), true);

            if (event.hasBlock()) {
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    Block block = event.getClickedBlock();
                    assert block != null;
                    NamedBinaryTagHelper.getLastInteractBlock().put(player.getName(), block);
                    player.sendMessage(GRAY + "Для изменения " + YELLOW + "NBT "
                            + GRAY + "выбран блок " + YELLOW + block.getBlockData().getMaterial());
                }
            }
            Bukkit.getScheduler().runTaskLater(NamedBinaryTagHelper.getInstance(), () -> interactFlag.put(player.getName(), false), 1);
        }
    }
}
