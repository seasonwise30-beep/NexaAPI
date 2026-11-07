package tw.allent.nexaapi.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tw.allent.nexaapi.utils.ColorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A single item inside a {@link Menu}, with display and click behaviour.
 */
public final class MenuItem {

    private final ItemStack item;
    private Consumer<Player> clickHandler;

    public MenuItem(ItemStack item) {
        this.item = item;
    }

    public static MenuItem of(Material material, String name) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ColorUtil.colorize(name));
            stack.setItemMeta(meta);
        }
        return new MenuItem(stack);
    }

    public static MenuItem of(Material material, String name, List<String> lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ColorUtil.colorize(name));
            List<String> colored = new ArrayList<>();
            for (String s : lore) colored.add(ColorUtil.colorize(s));
            meta.setLore(colored);
            stack.setItemMeta(meta);
        }
        return new MenuItem(stack);
    }

    public MenuItem onClick(Consumer<Player> handler) {
        this.clickHandler = handler;
        return this;
    }

    public ItemStack getItem() { return item; }
    public Consumer<Player> getClickHandler() { return clickHandler; }
}