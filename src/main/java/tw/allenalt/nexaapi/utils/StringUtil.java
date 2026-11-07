package tw.allent.nexaapi.utils;

import java.util.Locale;

/**
 * Generic string helpers.
 */
public final class StringUtil {

    private StringUtil() {}

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase(Locale.ROOT) + input.substring(1).toLowerCase(Locale.ROOT);
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String join(String[] arr, String sep, int from) {
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < arr.length; i++) {
            if (sb.length() > 0) sb.append(sep);
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /** Formats seconds as mm:ss. */
    public static String formatTime(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}