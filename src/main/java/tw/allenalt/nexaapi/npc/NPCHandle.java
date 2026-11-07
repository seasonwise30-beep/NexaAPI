package tw.allent.nexaapi.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Opaque handle for a spawned NPC.
 */
public interface NPCHandle {

    UUID getId();

    String getName();

    void setName(String name);

    Location getLocation();

    void teleport(Location location);

    /** Show or refresh the NPC for a specific viewer. */
    void show(Player viewer);

    /** Hide the NPC from a specific viewer. */
    void hide(Player viewer);

    /** Permanently remove the NPC for all viewers. */
    void remove();

    boolean isRemoved();
}