package tw.allent.nexaapi.manager;

import org.bukkit.plugin.Plugin;
import tw.allent.nexaapi.storage.StorageProvider;
import tw.allent.nexaapi.storage.StorageType;
import tw.allent.nexaapi.storage.YamlStorage;

/**
 * Chooses and exposes the active storage provider based on config.
 */
public final class StorageManager implements Manager {

    private final Plugin plugin;
    private StorageProvider activeProvider;
    private StorageType type = StorageType.YAML;

    public StorageManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        String cfgType = plugin.getConfig().getString("storage.type", "YAML");
        try {
            this.type = StorageType.valueOf(cfgType.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Unknown storage type '" + cfgType + "', defaulting to YAML.");
            this.type = StorageType.YAML;
        }

        switch (type) {
            case YAML -> activeProvider = new YamlStorage(plugin);
            // SQLite / MySQL implementations would be added by NexaCore extension
            case SQLITE, MYSQL -> {
                plugin.getLogger().warning(type + " storage is not implemented in NexaAPI; using YAML fallback.");
                activeProvider = new YamlStorage(plugin);
                this.type = StorageType.YAML;
            }
        }

        activeProvider.initialize();
    }

    @Override
    public void shutdown() {
        if (activeProvider != null) activeProvider.close();
    }

    @Override
    public void reload() {
        if (activeProvider != null) activeProvider.close();
        initialize();
    }

    public StorageProvider getProvider() {
        return activeProvider;
    }

    public StorageType getType() {
        return type;
    }
}