package tw.allent.nexaapi.manager;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import tw.allent.nexaapi.utils.ColorUtil;

/**
 * Provides configurable message templates with prefix support.
 */
public final class MessageManager implements Manager {

    private final Plugin plugin;
    private final ConfigManager configManager;
    private String prefix = "";
    private FileConfiguration messages;

    public MessageManager(Plugin plugin) {
        this.plugin = plugin;
        this.configManager = null; // not needed directly
    }

    // Constructor injection alternative — we use plugin lookup instead.
    public MessageManager(Plugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public void initialize() {
        // Load messages.yml via ConfigManager if available, else own fallback
        FileConfiguration cfg = plugin instanceof tw.allent.nexaapi.NexaAPI api
                ? api.config().get("messages")
                : null;
        if (cfg == null) {
            // Fallback: load our own
            cfg = org.bukkit.configuration.file.YamlConfiguration
                    .loadConfiguration(new java.io.File(plugin.getDataFolder(), "messages.yml"));
        }
        this.messages = cfg;
        String p = messages.getString("prefix");
        if (p == null && plugin.getConfig() != null) {
            p = plugin.getConfig().getString("messages.prefix");
        }
        this.prefix = ColorUtil.colorize(p == null ? "" : p);
    }

    @Override
    public void shutdown() {}

    @Override
    public void reload() {
        initialize();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = ColorUtil.colorize(prefix);
    }

    /**
     * Retrieves a raw message from messages.yml with fallback support.
     */
    public String getRaw(String key) {
        if (messages == null) return null;
        String v = messages.getString(key);
        if (v == null) {
            FileConfiguration cfg = plugin.getConfig();
            if (cfg != null) v = cfg.getString("messages." + key);
        }
        return v;
    }

    /**
     * Returns a fully formatted (prefix + colored) message.
     */
    public String format(String key) {
        String raw = getRaw(key);
        if (raw == null) return prefix + ColorUtil.colorize("&cMissing message: " + key);
        return prefix + ColorUtil.colorize(raw);
    }

    public void send(CommandSender sender, String key) {
        sender.sendMessage(format(key));
    }

    public void sendRaw(CommandSender sender, String message) {
        sender.sendMessage(ColorUtil.colorize(message));
    }

    public void sendPrefixed(CommandSender sender, String message) {
        sender.sendMessage(prefix + ColorUtil.colorize(message));
    }

    public void sendActionBar(Player player, String message) {
        player.sendActionBar(ColorUtil.colorize(message));
    }
}