package tw.allent.nexaapi.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Base class for inventory menus.
 * <p>
 * Extend this class and populate items in {@link #build()}. Click handling
 * is delegated to the item assigned to each slot.
 * </p>
 */
public abstract class Menu implements InventoryHolder {

    private final Plugin plugin;
    private final String title;
    private final int rows;
    private final Map<Integer, MenuItem> items = new HashMap<>();
    private Inventory inventory;
    private MenuListener listener;
    private Consumer<Player> closeHandler;

    protected Menu(Plugin plugin, String title, int rows) {
        if (rows < 1 || rows > 6) throw new IllegalArgumentException("rows must be 1-6");
        this.plugin = plugin;
        this.title = title;
        this.rows = rows;
    }

    /**
     * Populate items into the menu. Called once on open.
     */
    protected abstract void build();

    public final void open(Player player) {
        this.items.clear();
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        build();
        for (Map.Entry<Integer, MenuItem> e : items.entrySet()) {
            inventory.setItem(e.getKey(), e.getValue().getItem());
        }
        if (listener == null) {
            listener = new MenuListener(this);
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
        player.openInventory(inventory);
    }

    /** Sets a slot item. Slot must be within bounds. */
    protected void set(int slot, MenuItem item) {
        items.put(slot, item);
    }

    void handleClick(Player player, int slot) {
        MenuItem item = items.get(slot);
        if (item == null) return;
        if (item.getClickHandler() != null) {
            item.getClickHandler().accept(player);
        }
    }

    void handleClose(Player player) {
        if (closeHandler != null) closeHandler.accept(player);
    }

    public void setCloseHandler(Consumer<Player> handler) {
        this.closeHandler = handler;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Plugin getPlugin() { return plugin; }

    /** Internal listener, unregisters itself on plugin shutdown. */
    static final class MenuListener implements Listener {
        private final Menu menu;
        MenuListener(Menu menu) { this.menu = menu; }

        @EventHandler
        public void onClick(InventoryClickEvent e) {
            if (!(e.getInventory().getHolder() instanceof Menu)) return;
            Menu holder = (Menu) e.getInventory().getHolder();
            if (holder != menu) return;
            e.setCancelled(true);
            if (e.getWhoClicked() instanceof Player player) {
                menu.handleClick(player, e.getRawSlot());
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e) {
            if (!(e.getInventory().getHolder() instanceof Menu)) return;
            Menu holder = (Menu) e.getInventory().getHolder();
            if (holder != menu) return;
            if (e.getPlayer() instanceof Player player) {
                menu.handleClose(player);
            }
        }

        void unregister() {
            HandlerList.unregisterAll(this);
        }
    }
}