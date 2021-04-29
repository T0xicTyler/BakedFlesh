package io.github.t0xictyler.bakedflesh;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;

@RequiredArgsConstructor
public class BakedFleshCommand implements CommandExecutor, TabCompleter {

    @NonNull
    private final BakedFleshPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PluginDescriptionFile pdFile = plugin.getDescription();
        String versionStr = String.format("BakedFlesh v%s by %s", pdFile.getVersion(), "T0xicTyler");

        if (args.length == 0) {
            sender.sendMessage(versionStr);
        } else {
            String arg1 = args[0];

            if (arg1.equalsIgnoreCase("reload") || arg1.equalsIgnoreCase("load")) {
                if (sender.hasPermission("bakedflesh.command")) {
                    plugin.reload(sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "You are not allowed to do that!");
                }
            } else if (arg1.equalsIgnoreCase("version") || arg1.equalsIgnoreCase("v")) {
                sender.sendMessage(versionStr);
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // TODO Tab completion
        return null;
    }
}
