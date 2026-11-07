package tw.allent.nexaapi.hologram;

import org.bukkit.Location;

import java.util.List;

/**
 * Opaque handle returned by {@link HologramProvider#create(Location, List)}.
 */
public interface HologramHandle {

    void setLines(List<String> lines);

    List<String> getLines();

    void teleport(Location location);

    Location getLocation();

    void remove();

    boolean isRemoved();
}