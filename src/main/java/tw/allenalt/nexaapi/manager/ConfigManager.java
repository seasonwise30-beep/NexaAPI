package tw.allent.nexaapi.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class ConfigManager implements Manager {

    private final Plugin plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    private final Map<String, File> files = new HashMap<>();

    private FileConfiguration main;
    private File mainFile;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.main = plugin.getConfig();
        this.mainFile = new File(plugin.getDataFolder(), "config.yml");
        registerConfig("config", mainFile, main);
    }

    @Override
    public void shutdown() {
        // Nothing to release
    }

    @Override
    public void reload() {
        for (Map.Entry<String, FileConfiguration> e : configs.entrySet()) {
            File file = files.get(e.getKey());
            if (file != null && file.exists()) {
                e.getValue().load(file);
            }
        }
        plugin.reloadConfig();
        this.main = plugin.getConfig();
    }

    public FileConfiguration getMain() {
        return main;
    }

    public FileConfiguration get(String name) {
        if ("config".equals(name)) return main;
        FileConfiguration cfg = configs.get(name);
        if (cfg == null) {
            File file = new File(plugin.getDataFolder(), name + ".yml");
            if (!file.exists()) {
                try {
                    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                    file.createNewFile();
                } catch (IOException ex) {
                    plugin.getLogger().warning("Could not create " + name + ".yml");
                }
            }
            cfg = YamlConfiguration.loadConfiguration(file);
            InputStream in = plugin.getResource(name + ".yml");
            if (in != null) {
                try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                    cfg.setDefaults(YamlConfiguration.loadConfiguration(reader));
                    cfg.options().copyDefaults(true);
                    try { cfg.save(file); } catch (IOException ignored) {}
                } catch (FileNotFoundException e) {
                    plugin.getLogger().warning("Could not read default " + name + ".yml from jar.");
                } catch (IOException e) {
                    plugin.getLogger().warning("Could not read default " + name + ".yml: " + e.getMessage());
                }
            }
            registerConfig(name, file, cfg);
        }
        return cfg;
    }

    private void registerConfig(String name, File file, FileConfiguration cfg) {
        configs.put(name, cfg);
        files.put(name, file);
    }

    public void save(String name) {
        FileConfiguration cfg = configs.get(name);
        File file = files.get(name);
        if (cfg == null || file == null) return;
        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save " + name + ".yml: " + e.getMessage());
        }
    }

    public void saveAll() {
        for (String name : configs.keySet()) save(name);
    }

    public String getString(String path, String def) {
        return main.getString(path, def);
    }

    public int getInt(String path, int def) {
        return main.getInt(path, def);
    }

    public boolean getBoolean(String path, boolean def) {
        return main.getBoolean(path, def);
    }

    public double getDouble(String path, double def) {
        return main.getDouble(path, def);
    }

    public void set(String path, Object value) {
        main.set(path, value);
    }
}