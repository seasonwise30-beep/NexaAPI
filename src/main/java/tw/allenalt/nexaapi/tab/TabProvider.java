package tw.allent.nexaapi.tab;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * Extension point for tab list implementations.
 */
public interface TabProvider {

    /**
     * Sets the header and footer for the player.
     */
    void setHeaderFooter(Player player, String header, String footer);

    /**
     * Sets the tab list entries for the player.
     */
    void setEntries(Player player, List<String> entries);

    /**
     * @return provider identifier.
     */
    String getId();
}