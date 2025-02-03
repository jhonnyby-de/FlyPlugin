package org.jhonnyby.flyPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.EventHandler;

public class FlyPlugin extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private static final String DEFAULT_PREFIX = "§9[Minedrop] §r";

    @Override
    public void onEnable() {
        // Plugin-Initialisierung
        saveDefaultConfig();
        config = getConfig();
        String prefix = config.getString("prefix", DEFAULT_PREFIX);

        getLogger().info(prefix + config.getString("messages.enable", "FlyPlugin aktiviert!"));
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        String prefix = config.getString("prefix", DEFAULT_PREFIX);
        getLogger().info(prefix + config.getString("messages.disable", "FlyPlugin deaktiviert!"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = config.getString("prefix", DEFAULT_PREFIX);

        if (command.getName().equalsIgnoreCase("fly")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("flyplugin.admin.reload")) {
                    reloadConfig();
                    config = getConfig();
                    sender.sendMessage(prefix + "Config wurde neu geladen!");
                    getLogger().info(prefix + "Config wurde neu geladen!");
                    return true;
                } else {
                    sender.sendMessage(prefix + "Du hast keine Berechtigung, diesen Befehl auszuführen!");
                    return true;
                }
            } else if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("flyplugin.fly")) {
                    if (player.getAllowFlight()) {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        player.sendMessage(prefix + config.getString("messages.flightDisabled", "Fliegen deaktiviert!"));
                    } else {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.sendMessage(prefix + config.getString("messages.flightEnabled", "Fliegen aktiviert!"));
                    }
                } else {
                    player.sendMessage(prefix + config.getString("messages.noPermission", "Du hast keine Berechtigung, um diesen Befehl zu verwenden."));
                }
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        String prefix = config.getString("prefix", DEFAULT_PREFIX);

        if (!player.hasPermission("flyplugin.fly")) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(prefix + config.getString("messages.noPermissionFlight", "Du hast keine Berechtigung zum Fliegen."));
        }
    }
}
