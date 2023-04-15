package main.nbth;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class NamedBinaryTagHelper extends JavaPlugin {

    private static NamedBinaryTagHelper instance;

    @Override
    public void onEnable() {
        instance = this;

        Objects.requireNonNull(getCommand("nbt")).setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new BukkitEventHandler(), this);
    }

    private static final Map<String, Block> lastInteractBlock = new HashMap<>();
    private static final Map<String, Boolean> enable = new HashMap<>();


    public static Map<String, Block> getLastInteractBlock() {
        return lastInteractBlock;
    }
    public static Map<String, Boolean> getEnable() {
        return enable;
    }
    public static NamedBinaryTagHelper getInstance() {
        return instance;
    }
}
