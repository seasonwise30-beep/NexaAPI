package tw.allent.nexaapi.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Lightweight event facade that simplifies registering and calling events.
 * <p>
 * Listener registration goes through the Bukkit event system so both
 * custom {@link NexaEvent} and standard Bukkit events work uniformly.
 * </p>
 */
public final class EventBus {

    private final Plugin plugin;

    public EventBus(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers a Bukkit {@link Listener} instance.
     */
    public void register(@NotNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Convenience: register a listener via reflection-free functional method.
     * The caller must supply a listener class instance.
     */
    public <T extends Listener> T registerAndReturn(@NotNull T listener) {
        register(listener);
        return listener;
    }

    /**
     * Fires an event through the Bukkit event system.
     */
    public <T extends Event> T call(@NotNull T event) {
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * Fires an event synchronously if currently on the main thread,
     * otherwise schedules it.
     */
    public <T extends Event> void callLater(@NotNull T event) {
        if (Bukkit.isPrimaryThread()) {
            call(event);
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> call(event));
        }
    }

    /**
     * Registers a one-shot callback that runs once the event fires.
     */
    public <T extends Event> void once(@NotNull Class<T> type, @NotNull Consumer<T> consumer) {
        Bukkit.getPluginManager().registerEvent(
                type,
                new Listener() {},
                EventPriority.NORMAL,
                (listener, event) -> {
                    if (type.isInstance(event)) {
                        consumer.accept(type.cast(event));
                    }
                },
                plugin
        );
    }
}