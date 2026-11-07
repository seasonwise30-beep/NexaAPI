package tw.allent.nexaapi.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Common player lookups and checks.
 */
public final class PlayerUtil {

    private PlayerUtil() {}

    /** @return online player by exact (case-insensitive) name, or {@code null}. */
    public static Player getOnline(String name) {
        if (name == null) return null;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    /** @return online player by UUID, or {@code null}. */
    public static Player getOnline(UUID uuid) {
        return uuid == null ? null : Bukkit.getPlayer(uuid);
    }

    /** @return offline player by name (may be null). */
    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOffline(String name) {
        return Bukkit.getOfflinePlayer(name);
    }

    /** @return name from UUID, or the UUID string if unknown. */
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

    public static Collection<Player> allOnline() {
        return Bukkit.getOnlinePlayers();
    }

    public static Optional<Player> find(String name) {
        return Optional.ofNullable(getOnline(name));
    }

    /**
     * Sends a colorized message to the player.
     */
    public static void message(Player player, String message) {
        if (player != null && message != null) {
            player.sendMessage(tw.allent.nexaapi.utils.ColorUtil.colorize(message));
        }
    }
}