package tw.allent.nexaapi.utils;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Detects the running server version to enable/disable optional features.
 */
public final class ServerVersion {

    public static final int MINECRAFT_1_8 = encode(1, 8);
    public static final int MINECRAFT_1_13 = encode(1, 13);
    public static final int MINECRAFT_1_16 = encode(1, 16);
    public static final int MINECRAFT_1_17 = encode(1, 17);
    public static final int MINECRAFT_1_18 = encode(1, 18);
    public static final int MINECRAFT_1_19 = encode(1, 19);
    public static final int MINECRAFT_1_20 = encode(1, 20);
    public static final int MINECRAFT_1_21 = encode(1, 21);

    private static final Pattern PATTERN = Pattern.compile("1\\.(\\d+)(?:\\.(\\d+))?");

    private static int current = -1;

    private ServerVersion() {}

    public static int encode(int major, int minor) {
        return major * 10_000 + minor;
    }

    public static int getCurrent() {
        if (current != -1) return current;
        try {
            String bukkit = Bukkit.getBukkitVersion(); // e.g. "1.20.4-R0.1-SNAPSHOT"
            Matcher m = PATTERN.matcher(bukkit);
            if (m.find()) {
                int major = 1;
                int minor = Integer.parseInt(m.group(1));
                current = encode(major, minor);
                return current;
            }
        } catch (Throwable ignored) {}
        current = MINECRAFT_1_13;
        return current;
    }

    public static boolean isAtLeast(int encodedVersion) {
        return getCurrent() >= encodedVersion;
    }

    public static String getBukkitVersion() {
        try {
            return Bukkit.getVersion();
        } catch (Throwable t) {
            return "unknown";
        }
    }
}