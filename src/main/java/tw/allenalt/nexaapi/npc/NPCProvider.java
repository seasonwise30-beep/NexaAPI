package tw.allent.nexaapi.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Extension point for NPC implementations.
 * <p>
 * Kept as an interface so NexaCore can provide a version-specific
 * implementation without pushing NMS into NexaAPI.
 * </p>
 */
public interface NPCProvider {

    /**
     * Spawns an NPC for the given viewer at the location.
     */
    NPCHandle spawn(Player viewer, String name, Location location);

    /**
     * Removes an NPC by ID.
     */
    void remove(UUID npcId);

    /**
     * @return provider identifier.
     */
    String getId();
}