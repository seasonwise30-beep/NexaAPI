package tw.allent.nexaapi.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Legacy color-code plus hex {@code &#RRGGBB} translation.
 * <p>
 * The hex-to-legacy-anvil conversion works on 1.16+ via {@code net.md_5.bungee}
 * APIs included with Paper/Spigot. We fall back gracefully on older servers.
 * </p>
 */
public final class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final boolean HEX_SUPPORTED = ServerVersion.isAtLeast(ServerVersion.MINECRAFT_1_16);

    private ColorUtil() {}

    /**
     * Translates both legacy &-codes and hex codes into a final colored String.
     */
    public static String colorize(String input) {
        if (input == null || input.isEmpty()) return input;
        String result = input;
        if (HEX_SUPPORTED) {
            Matcher matcher = HEX_PATTERN.matcher(result);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                String hex = matcher.group(1);
                matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + hex).toString());
            }
            matcher.appendTail(buffer);
            result = buffer.toString();
        } else {
            // Strip hex and let it be treated as literal — avoids unexpected colors.
            result = HEX_PATTERN.matcher(result).replaceAll("");
        }
        return ChatColor.translateAlternateColorCodes('&', result);
    }

    public static String strip(String input) {
        if (input == null) return null;
        return ChatColor.stripColor(colorize(input));
    }
}