package tw.allent.nexaapi.manager;

/**
 * Base interface for all NexaAPI managers.
 * <p>
 * Managers must implement lifecycle methods. {@link #initialize()} runs
 * synchronously during plugin enable; {@link #shutdown()} runs on disable.
 * </p>
 */
public interface Manager {

    /**
     * Called when the manager should initialize its resources.
     */
    void initialize();

    /**
     * Called when the manager should release resources. Should be idempotent.
     */
    void shutdown();

    /**
     * Optional: called when /nexa reload runs.
     */
    default void reload() {}

    /**
     * @return a human readable name used for logging.
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}