package tw.allent.nexaapi.script;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Function;

/**
 * Extension point for the NexaCore scripting engine.
 */
public interface ScriptProvider {

    /**
     * Evaluates a script snippet with variables.
     *
     * @return the result, typically a String.
     */
    Object evaluate(String script, Map<String, Object> variables);

    /**
     * Registers a function that scripts can invoke by name.
     */
    void registerFunction(String name, Function<Object[], Object> function);

    /**
     * Runs a script in the context of the given player.
     */
    void runForPlayer(Player player, String script);

    /**
     * @return provider identifier.
     */
    String getId();
}