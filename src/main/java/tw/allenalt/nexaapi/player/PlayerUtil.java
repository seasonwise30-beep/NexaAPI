package tw.allent.nexaapi.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public final class PlayerUtil {

    private PlayerUtil() {}

    public static Player getOnline(String name) {
        if (name == null) return null;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    public static Player getOnline(UUID uuid) {
        return uuid == null ? null : Bukkit.getPlayer(uuid);
    }

    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOffline(String name) {
        return Bukkit.getOfflinePlayer(name);
    }

    public static String nameOf(UUID uuid) {
        if (uuid == null) return "unknown";
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        String name = op.getName();
        return name != null ? name : uuid.toString();
    }

    public static boolean isOnline(UUID uuid) {
        Player p = getOnline(uuid);
        return p != null && p.isOnline();
    }

    public static boolean hasPermission(Player player, String permission) {
        return player != null && player.hasPermission(permission);
    }

    /**
     * Returns all online players.
     * <p>
     * The return type is widened to {@code Collection<? extends Player>}
     * because {@code Bukkit.getOnlinePlayers()} uses a wildcard on modern APIs.
     * Callers can iterate directly; use {@code new ArrayList<>(...)} if a
     * mutable copy is needed.
     * </p>
     */
    public static Collection<? extends Player> allOnline() {
        return Bukkit.getOnlinePlayers();
    }

    public static Optional<Player> find(String name) {
        return Optional.ofNullable(getOnline(name));
    }

    public static void message(Player player, String message) {
        if (player != null && message != null) {
            player.sendMessage(tw.allent.nexaapi.utils.ColorUtil.colorize(message));
        }
    }
}