package tw.allent.nexaapi.api;

import tw.allent.nexaapi.NexaAPI;

/**
 * Convenience helper for retrieving the API without hard references.
 */
public final class NexaProvider {

    private NexaProvider() {}

    /**
     * @return the API, or {@code null} if NexaAPI is not loaded.
     */
    public static NexaAPIAccess get() {
        return NexaAPI.getAPI();
    }

    /**
     * @return true if NexaAPI is loaded and ready.
     */
    public static boolean isAvailable() {
        return NexaAPI.getAPI() != null;
    }
}