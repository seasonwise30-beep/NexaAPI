package tw.allent.nexaapi.hologram;

import org.bukkit.Location;

import java.util.List;

/**
 * Extension point for hologram implementations.
 * <p>
 * NexaCore (or another plugin) can implement this interface and register it
 * with the API, keeping NexaAPI itself free of NMS / per-version code.
 * </p>
 */
public interface HologramProvider {

    /**
     * Creates a hologram at the given location with the initial lines.
     *
     * @return a handle for later updates / removal.
     */
    HologramHandle create(Location location, List<String> lines);

    /**
     * @return the identifier this provider is registered under.
     */
    String getId();
}