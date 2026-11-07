package tw.allent.nexaapi.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import tw.allent.nexaapi.manager.Manager;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Registers {@link NexaCommand} instances dynamically without needing
 * plugin.yml entries for every command.
 */
public final class CommandRegistry implements Manager {

    private final Plugin plugin;
    private CommandMap commandMap;

    public CommandRegistry(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            this.commandMap = (CommandMap) f.get(Bukkit.getServer());
        } catch (ReflectiveOperationException e) {
            plugin.getLogger().warning("Could not obtain CommandMap, falling back to plugin.yml.");
            this.commandMap = null;
        }
    }

    @Override
    public void shutdown() {}

    /**
     * Registers a command. Tries CommandMap first, then plugin.yml fallback.
     */
    public void register(NexaCommand cmd) {
        if (commandMap != null) {
            org.bukkit.command.Command bukkit = new org.bukkit.command.Command(
                    cmd.getName(),
                    "",
                    "/" + cmd.getName(),
                    cmd.getAliases()) {
                @Override
                public boolean execute(org.bukkit.command.CommandSender sender,
                                       String commandLabel, String[] args) {
                    return cmd.onCommand(sender, this, commandLabel, args);
                }

                @Override
                public java.util.List<String> tabComplete(
                        org.bukkit.command.CommandSender sender, String alias, String[] args)
                        throws IllegalArgumentException {
                    return cmd.onTabComplete(sender, this, alias, args);
                }
            };
            commandMap.register(plugin.getName().toLowerCase(), bukkit);
            plugin.getLogger().info("Registered command: /" + cmd.getName());
        } else {
            PluginCommand pc = plugin.getCommand(cmd.getName());
            if (pc != null) {
                pc.setExecutor(cmd);
                pc.setTabCompleter(cmd);
            }
        }
    }
}