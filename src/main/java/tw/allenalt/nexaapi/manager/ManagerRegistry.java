package tw.allent.nexaapi.manager;

import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Central registry of all {@link Manager} instances.
 * <p>
 * Managers are keyed by their concrete class, keeping lookups type-safe.
 * Registration order determines initialization order.
 * </p>
 */
public final class ManagerRegistry {

    private final Plugin plugin;
    private final Logger logger;
    private final Map<Class<? extends Manager>, Manager> managers = new LinkedHashMap<>();
    private boolean initialized = false;
    private boolean shutdown = false;

    public ManagerRegistry(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    /**
     * Registers a manager instance under its concrete class.
     * <p>
     * Any manager can be registered, including custom managers from other
     * plugins. Registering before {@link #initializeAll()} ensures automatic
     * lifecycle management.
     * </p>
     */
    public <T extends Manager> T register(T manager) {
        if (manager == null) {
            throw new IllegalArgumentException("manager cannot be null");
        }
        if (initialized) {
            // Late registration: initialize immediately.
            safeInitialize(manager);
        }
        managers.put(manager.getClass(), manager);
        logger.fine("Registered manager: " + manager.getName());
        return manager;
    }

    /**
     * @return the manager for the given class, or {@code null}.
     */
    public <T extends Manager> T get(Class<T> type) {
        Manager m = managers.get(type);
        if (m == null) {
            // Try an assignable match (support interfaces / superclasses).
            for (Manager candidate : managers.values()) {
                if (type.isInstance(candidate)) {
                    return type.cast(candidate);
                }
            }
            return null;
        }
        return type.cast(m);
    }

    /**
     * @return the manager as an Optional.
     */
    public <T extends Manager> Optional<T> find(Class<T> type) {
        return Optional.ofNullable(get(type));
    }

    public boolean contains(Class<? extends Manager> type) {
        return get(type) != null;
    }

    public Collection<Manager> all() {
        return managers.values();
    }

    /**
     * Initializes all registered managers in registration order.
     * Errors from individual managers do not halt startup of others.
     */
    public void initializeAll() {
        if (initialized) return;
        for (Manager m : managers.values()) {
            safeInitialize(m);
        }
        initialized = true;
    }

    /**
     * Shuts down all managers in reverse registration order.
     */
    public void shutdownAll() {
        if (shutdown) return;
        shutdown = true;
        Manager[] ordered = managers.values().toArray(new Manager[0]);
        for (int i = ordered.length - 1; i >= 0; i--) {
            try {
                ordered[i].shutdown();
            } catch (Throwable t) {
                logger.warning("Error shutting down " + ordered[i].getName() + ": " + t);
            }
        }
    }

    /**
     * Calls {@link Manager#reload()} on every manager.
     */
    public void reloadAll() {
        for (Manager m : managers.values()) {
            try {
                m.reload();
            } catch (Throwable t) {
                logger.warning("Error reloading " + m.getName() + ": " + t);
            }
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    private void safeInitialize(Manager m) {
        try {
            m.initialize();
        } catch (Throwable t) {
            logger.severe("Error initializing " + m.getName() + ": " + t);
            t.printStackTrace();
        }
    }
}