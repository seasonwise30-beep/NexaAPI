package tw.allent.nexaapi.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Internal placeholder system with optional PlaceholderAPI bridging.
 * <p>
 * Placeholders use the {@code %identifier_key%} syntax. Player-specific
 * placeholders can use the player-aware overloads.
 * </p>
 */
public final class PlaceholderManager implements Manager {

    private final Plugin plugin;
    private final Map<String, Function<Player, String>> playerPlaceholders = new HashMap<>();
    private final Map<String, java.util.function.Supplier<String>> globalPlaceholders = new HashMap<>();
    private boolean placeholderApiAvailable = false;

    public PlaceholderManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        placeholderApiAvailable = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (placeholderApiAvailable) {
            plugin.getLogger().info("PlaceholderAPI integration enabled.");
        }
    }

    @Override
    public void shutdown() {
        playerPlaceholders.clear();
        globalPlaceholders.clear();
    }

    /**
     * Registers a global placeholder (no player context required).
     *
     * @param key         placeholder identifier (e.g. {@code "nexa_online"})
     * @param supplier    function producing the current value
     */
    public void registerGlobal(String key, java.util.function.Supplier<String> supplier) {
        globalPlaceholders.put(key.toLowerCase(), supplier);
    }

    /**
     * Registers a player-specific placeholder.
     */
    public void registerPlayer(String key, Function<Player, String> function) {
        playerPlaceholders.put(key.toLowerCase(), function);
    }

    /**
     * Applies all internal placeholders, optionally using the given player context.
     */
    public String apply(Player player, String text) {
        if (text == null) return null;
        String result = text;
        for (Map.Entry<String, Function<Player, String>> e : playerPlaceholders.entrySet()) {
            String token = "%" + e.getKey() + "%";
            if (result.contains(token) && player != null) {
                try {
                    String value = e.getValue().apply(player);
                    result = result.replace(token, value == null ? "" : value);
                } catch (Throwable ignored) {}
            }
        }
        for (Map.Entry<String, java.util.function.Supplier<String>> e : globalPlaceholders.entrySet()) {
            String token = "%" + e.getKey() + "%";
            if (result.contains(token)) {
                try {
                    String value = e.getValue().get();
                    result = result.replace(token, value == null ? "" : value);
                } catch (Throwable ignored) {}
            }
        }
        return result;
    }

    /**
     * Applies internal placeholders, then (if available) PlaceholderAPI placeholders.
     */
    public String applyAll(Player player, String text) {
        String result = apply(player, text);
        if (placeholderApiAvailable && player != null) {
            try {
                result = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, result);
            } catch (Throwable ignored) {}
        }
        return result;
    }

    public boolean isPlaceholderApiAvailable() {
        return placeholderApiAvailable;
    }
}