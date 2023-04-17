package main.nbth;

import com.google.common.collect.Lists;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static org.bukkit.ChatColor.*;

public class Commands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Block block = NamedBinaryTagHelper.getLastInteractBlock().get(sender.getName());
        if (args.length == 0) {
            if (NamedBinaryTagHelper.getEnable().get(sender.getName()) == null || NamedBinaryTagHelper.getEnable().get(sender.getName()).equals(false)) {
                NamedBinaryTagHelper.getEnable().put(sender.getName(), true);
                sender.sendMessage(GRAY + "Вы включили " + YELLOW + "NamedBinaryTagHelper" + GRAY + " для себя");
            } else {
                NamedBinaryTagHelper.getEnable().put(sender.getName(), false);
                sender.sendMessage(GRAY + "Вы отключили " + YELLOW + "NamedBinaryTagHelper" + GRAY + " для себя");
            }
            return true;
        } else if (NamedBinaryTagHelper.getEnable().get(sender.getName()) != null && NamedBinaryTagHelper.getEnable().get(sender.getName())) {
            if (block != null) {
                if (args[0].equals("get")) {
                    if (args.length > 1) {
                        String key = args[1];
                        BlockState state = block.getState();
                        PersistentDataContainer container = state.getChunk().getPersistentDataContainer();
                        NamespacedKey NBTKey = new NamespacedKey(NamedBinaryTagHelper.getInstance(), key);
                        if (container.has(NBTKey, PersistentDataType.STRING)) {
                            String value = container.get(NBTKey, PersistentDataType.STRING);
                            sender.sendMessage(YELLOW + "NBT" + GRAY + " в выбранном блоке под ключем "
                                    + YELLOW + key + GRAY + " значение:" + WHITE + "\n" + value);
                        }
                    } else {
                        sender.sendMessage("/nbt get [key]");
                    }

                } else if (args[0].equals("set")) {
                    if (args.length > 2) {
                        String key = args[1];
                        StringBuilder valueBuilder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            valueBuilder.append(args[i]).append(" ");
                        }
                        String value = valueBuilder.toString();
                        BlockState state = block.getState();
                        PersistentDataContainer container = state.getChunk().getPersistentDataContainer();
                        NamespacedKey NBTKey = new NamespacedKey(NamedBinaryTagHelper.getInstance(), key);
                        container.set(NBTKey, PersistentDataType.STRING, value);
                        state.update();
                        sender.sendMessage(YELLOW + "NBT" + GRAY + " для выбранного блока под ключем "
                                + YELLOW + key + GRAY + " значение установленно на: \n" +  WHITE + value);
                    } else {
                        sender.sendMessage("/nbt set [key] [value]");
                    }
                }
            } else {
                sender.sendMessage(GRAY + "Для выбора блока используй " + YELLOW + "ПКМ");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (NamedBinaryTagHelper.getEnable().get(sender.getName()) == null) {
            return null;
        }
        if (NamedBinaryTagHelper.getEnable().get(sender.getName())) {
            if (args.length == 1) {
                return Lists.newArrayList("get", "set");
            } else if (args.length == 2) {
                return Lists.newArrayList("key");
            }
        }
        return null;
    }
}
