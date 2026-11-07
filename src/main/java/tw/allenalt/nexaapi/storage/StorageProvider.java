package tw.allent.nexaapi.storage;

import java.util.concurrent.CompletableFuture;

/**
 * Abstract storage backend used by NexaAPI and its consumers.
 */
public interface StorageProvider {

    void initialize();

    void close();

    /**
     * Stores a value under a namespace/key.
     */
    CompletableFuture<Void> set(String namespace, String key, String value);

    /**
     * Retrieves a value, or {@code null} if missing.
     */
    CompletableFuture<String> get(String namespace, String key);

    /**
     * Removes a key. Returns {@code true} if it existed.
     */
    CompletableFuture<Boolean> remove(String namespace, String key);

    /**
     * @return the backend type.
     */
    StorageType getType();
}