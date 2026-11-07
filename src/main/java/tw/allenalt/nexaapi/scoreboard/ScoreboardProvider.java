package tw.allent.nexaapi.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * Extension point for scoreboard implementations.
 */
public interface ScoreboardProvider {

    /**
     * Shows a scoreboard to the player with the given title and lines.
     */
    void show(Player player, String title, List<String> lines);

    /**
     * Hides the scoreboard for the player.
     */
    void hide(Player player);

    /**
     * @return provider identifier.
     */
    String getId();
}