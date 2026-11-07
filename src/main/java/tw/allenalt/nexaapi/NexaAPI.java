package tw.allent.nexaapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import tw.allent.nexaapi.api.NexaAPIAccess;
import tw.allent.nexaapi.command.CommandRegistry;
import tw.allent.nexaapi.event.EventBus;
import tw.allent.nexaapi.manager.ConfigManager;
import tw.allent.nexaapi.manager.ManagerRegistry;
import tw.allent.nexaapi.manager.MessageManager;
import tw.allent.nexaapi.manager.PlaceholderManager;
import tw.allent.nexaapi.manager.StorageManager;
import tw.allent.nexaapi.utils.ServerVersion;

import java.util.logging.Level;

/**
 * Main plugin entry point for NexaAPI.
 * <p>
 * This plugin exposes a stable, extensible API surface that other plugins
 * (notably NexaCore) can consume. All subsystems are registered as managers
 * inside the {@link ManagerRegistry}, and lifecycle is handled centrally.
 * </p>
 */
public final class NexaAPI extends JavaPlugin implements NexaAPIAccess {

    private static NexaAPI instance;
    private static NexaAPIAccess api;

    private ManagerRegistry managerRegistry;
    private EventBus eventBus;

    @Override
    public void onLoad() {
        instance = this;
        api = this;
        getLogger().info("NexaAPI loading...");
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        this.managerRegistry = new ManagerRegistry(this);
        this.eventBus = new EventBus(this);

        // Register built-in managers in dependency order.
        managerRegistry.register(new ConfigManager(this));
        managerRegistry.register(new MessageManager(this));
        managerRegistry.register(new PlaceholderManager(this));
        managerRegistry.register(new StorageManager(this));

        // Initialize managers
        managerRegistry.initializeAll();

        // Register the built-in admin command (registered via plugin.yml-less API)
        CommandRegistry commandRegistry = new CommandRegistry(this);
        managerRegistry.register(commandRegistry);

        detectDependencies();

        getLogger().info("NexaAPI v" + getDescription().getVersion()
                + " enabled in " + (System.currentTimeMillis() - start) + "ms"
                + " (Server: " + ServerVersion.getBukkitVersion() + ")");
    }

    @Override
    public void onDisable() {
        if (managerRegistry != null) {
            managerRegistry.shutdownAll();
        }
        getLogger().info("NexaAPI disabled.");
    }

    private void detectDependencies() {
        Plugin placeholderApi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderApi != null && placeholderApi.isEnabled()) {
            getLogger().info("Hooked into PlaceholderAPI.");
        }

        if (!ServerVersion.isAtLeast(ServerVersion.MINECRAFT_1_13)) {
            getLogger().warning("This server is older than 1.13 — some APIs may be limited.");
        }
    }

    /**
     * @return the live plugin instance.
     */
    public static NexaAPI getInstance() {
        return instance;
    }

    /**
     * @return the API accessor. Prefer this in downstream plugins.
     */
    public static NexaAPIAccess getAPI() {
        return api;
    }

    /**
     * Logs a message only if debug mode is enabled.
     */
    public void debug(String message) {
        ConfigManager cfg = getManagerRegistry().get(ConfigManager.class);
        if (cfg != null && cfg.getBoolean("settings.debug", false)) {
            getLogger().log(Level.INFO, "[DEBUG] " + message);
        }
    }

    public ManagerRegistry getManagerRegistry() {
        return managerRegistry;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    // -- NexaAPIAccess implementation ------------------------------------

    @Override
    public ManagerRegistry managers() {
        return managerRegistry;
    }

    @Override
    public ConfigManager config() {
        return managerRegistry.get(ConfigManager.class);
    }

    @Override
    public MessageManager messages() {
        return managerRegistry.get(MessageManager.class);
    }

    @Override
    public PlaceholderManager placeholders() {
        return managerRegistry.get(PlaceholderManager.class);
    }

    @Override
    public StorageManager storage() {
        return managerRegistry.get(StorageManager.class);
    }

    @Override
    public EventBus events() {
        return eventBus;
    }
}