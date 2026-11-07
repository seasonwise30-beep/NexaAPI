package tw.allent.nexaapi.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/**
 * Thread-safe cooldown tracker. Can be used by NexaCore for ability cooldowns,
 * command throttles, etc.
 */
public final class CooldownUtil {

    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();
    private final LongSupplier clock;
    private final long defaultDurationMs;

    public CooldownUtil(long defaultDurationMs) {
        this(defaultDurationMs, System::currentTimeMillis);
    }

    public CooldownUtil(long defaultDurationMs, LongSupplier clock) {
        this.defaultDurationMs = defaultDurationMs;
        this.clock = clock;
    }

    /**
     * @return true if the key is currently on cooldown.
     */
    public boolean isOnCooldown(String key) {
        Long expires = cooldowns.get(key);
        if (expires == null) return false;
        if (clock.getAsLong() >= expires) {
            cooldowns.remove(key);
            return false;
        }
        return true;
    }

    /**
     * @return remaining milliseconds, or 0 if none.
     */
    public long getRemaining(String key) {
        Long expires = cooldowns.get(key);
        if (expires == null) return 0;
        long remaining = expires - clock.getAsLong();
        return Math.max(remaining, 0);
    }

    /**
     * Starts a cooldown with the default duration.
     */
    public void set(String key) {
        cooldowns.put(key, clock.getAsLong() + defaultDurationMs);
    }

    /**
     * Starts a cooldown with a custom duration in milliseconds.
     */
    public void set(String key, long durationMs) {
        cooldowns.put(key, clock.getAsLong() + durationMs);
    }

    public void clear(String key) {
        cooldowns.remove(key);
    }

    public void clearAll() {
        cooldowns.clear();
    }
}