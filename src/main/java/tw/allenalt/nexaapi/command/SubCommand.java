package tw.allent.nexaapi.command;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * Foundation for subcommands attached to a {@link NexaCommand}.
 */
public abstract class SubCommand {

    private final String name;
    private final String permission;
    private final String[] aliases;

    protected SubCommand(String name, String permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    public boolean matches(String input) {
        if (name.equalsIgnoreCase(input)) return true;
        for (String alias : aliases) {
            if (alias.equalsIgnoreCase(input)) return true;
        }
        return false;
    }

    public String getName() { return name; }
    public String getPermission() { return permission; }
}