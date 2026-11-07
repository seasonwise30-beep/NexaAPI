package tw.allent.nexaapi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Base class for a top-level command with subcommand support.
 * <p>
 * Subcommands are dispatched by name. Tab completion aggregates
 * subcommand completions with permission checks.
 * </p>
 */
public abstract class NexaCommand implements TabExecutor {

    private final String name;
    private final List<String> aliases;
    private final String permission;
    private final boolean playerOnly;
    private final List<SubCommand> subCommands = new ArrayList<>();

    protected NexaCommand(String name, String permission, boolean playerOnly, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.aliases = Arrays.asList(aliases);
    }

    public void addSubCommand(SubCommand sub) {
        subCommands.add(sub);
    }

    public List<SubCommand> getSubCommands() {
        return Collections.unmodifiableList(subCommands);
    }

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                                   @NotNull String label, @NotNull String[] args) {
        if (playerOnly && !(sender instanceof Player)) {
            sender.sendMessage("§cPlayer only.");
            return true;
        }
        if (permission != null && !permission.isEmpty() && !sender.hasPermission(permission)) {
            sender.sendMessage("§cNo permission.");
            return true;
        }

        if (args.length == 0) {
            return onDefault(sender);
        }

        String subName = args[0];
        for (SubCommand sub : subCommands) {
            if (sub.matches(subName)) {
                if (sub.getPermission() != null && !sender.hasPermission(sub.getPermission())) {
                    sender.sendMessage("§cNo permission.");
                    return true;
                }
                return sub.execute(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        return onUnknownSub(sender, subName);
    }

    @Override
    public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                            @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> names = new ArrayList<>();
            for (SubCommand sub : subCommands) {
                if (sub.getPermission() == null || sender.hasPermission(sub.getPermission())) {
                    names.add(sub.getName());
                }
            }
            String partial = args[0].toLowerCase();
            names.removeIf(n -> !n.toLowerCase().startsWith(partial));
            return names;
        }

        String subName = args[0];
        for (SubCommand sub : subCommands) {
            if (sub.matches(subName)) {
                return sub.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return Collections.emptyList();
    }

    /** Called when the command is run with no subcommand. */
    protected abstract boolean onDefault(CommandSender sender);

    /** Called when an unrecognized subcommand is used. */
    protected boolean onUnknownSub(CommandSender sender, String subName) {
        sender.sendMessage("§cUnknown subcommand: " + subName);
        return true;
    }

    public String getName() { return name; }
    public List<String> getAliases() { return aliases; }
    public String getPermission() { return permission; }
    public boolean isPlayerOnly() { return playerOnly; }
}