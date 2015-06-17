package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class BlockListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        String title = CratesPlus.getPlugin().getConfig().getString("Crate Keys.Name").replaceAll("%type%", "");
        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(title)) {
            event.getPlayer().sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Cant Place", event.getPlayer(), ChatColor.stripColor(item.getItemMeta().getDisplayName().replaceAll(title, ""))));
            event.setCancelled(true);
            return;
        }

        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains("Crate!")) {
            final String crateType = item.getItemMeta().getDisplayName().replaceAll(" Crate!", "");
            // BlockMeta to be used for some stuff in the future!
            event.getBlock().setMetadata("CrateType", new MetadataValue() {
                @Override
                public Object value() {
                    return null;
                }

                @Override
                public int asInt() {
                    return 0;
                }

                @Override
                public float asFloat() {
                    return 0;
                }

                @Override
                public double asDouble() {
                    return 0;
                }

                @Override
                public long asLong() {
                    return 0;
                }

                @Override
                public short asShort() {
                    return 0;
                }

                @Override
                public byte asByte() {
                    return 0;
                }

                @Override
                public boolean asBoolean() {
                    return false;
                }

                @Override
                public String asString() {
                    return crateType;
                }

                @Override
                public Plugin getOwningPlugin() {
                    return CratesPlus.getPlugin();
                }

                @Override
                public void invalidate() {

                }
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getBlock().getState();
            if (chest.getInventory().getTitle() != null && chest.getInventory().getTitle().contains("Crate!")) {
                Location location = chest.getLocation();

                if (event.getPlayer().isSneaking() && (CratesPlus.getPlugin().getConfig().getBoolean("Crate Protection") && !event.getPlayer().hasPermission("cratesplus.admin"))) {
                    event.getPlayer().sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "You do not have permission to remove this crate");
                    event.setCancelled(true);
                    return;
                } else if (!event.getPlayer().isSneaking()) {
                    event.getPlayer().sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Sneak to break crates");
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

}
