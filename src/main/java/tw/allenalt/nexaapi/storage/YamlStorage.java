package tw.allent.nexaapi.storage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Simple YAML-backed storage that stores data under {@code data/<namespace>.yml}.
 */
public final class YamlStorage implements StorageProvider {

    private final Plugin plugin;
    private final File dataFolder;

    public YamlStorage(Plugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "data");
    }

    @Override
    public void initialize() {
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().warning("Could not create data folder for YAML storage.");
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }

    @Override
    public CompletableFuture<Void> set(String namespace, String key, String value) {
        return CompletableFuture.runAsync(() -> {
            File file = new File(dataFolder, namespace + ".yml");
            YamlConfiguration cfg = file.exists()
                    ? YamlConfiguration.loadConfiguration(file)
                    : new YamlConfiguration();
            cfg.set(key, value);
            try {
                cfg.save(file);
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to save " + file.getName() + ": " + e.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<String> get(String namespace, String key) {
        return CompletableFuture.supplyAsync(() -> {
            File file = new File(dataFolder, namespace + ".yml");
            if (!file.exists()) return null;
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            return cfg.getString(key);
        });
    }

    @Override
    public CompletableFuture<Boolean> remove(String namespace, String key) {
        return CompletableFuture.supplyAsync(() -> {
            File file = new File(dataFolder, namespace + ".yml");
            if (!file.exists()) return false;
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            if (!cfg.contains(key)) return false;
            cfg.set(key, null);
            try {
                cfg.save(file);
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to save " + file.getName() + ": " + e.getMessage());
            }
            return true;
        });
    }

    @Override
    public StorageType getType() {
        return StorageType.YAML;
    }
}