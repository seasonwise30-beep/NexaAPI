package tw.allent.nexaapi.api;

import tw.allent.nexaapi.event.EventBus;
import tw.allent.nexaapi.manager.ConfigManager;
import tw.allent.nexaapi.manager.ManagerRegistry;
import tw.allent.nexaapi.manager.MessageManager;
import tw.allent.nexaapi.manager.PlaceholderManager;
import tw.allent.nexaapi.manager.StorageManager;

/**
 * Public API accessor exposed by NexaAPI.
 * <p>
 * Downstream plugins (e.g. NexaCore) should depend on this interface rather
 * than the concrete plugin class to keep coupling low.
 * </p>
 */
public interface NexaAPIAccess {

    /** @return the central manager registry. */
    ManagerRegistry managers();

    /** @return the configuration manager. */
    ConfigManager config();

    /** @return the message manager. */
    MessageManager messages();

    /** @return the placeholder manager. */
    PlaceholderManager placeholders();

    /** @return the storage manager. */
    StorageManager storage();

    /** @return the custom event bus. */
    EventBus events();
}