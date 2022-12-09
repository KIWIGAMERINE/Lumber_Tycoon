package me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.LanguageNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootConfig;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ENameParser;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.PermissionNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EListener implements Listener {
    LumberTycoon plugin;
    HashMap<String, TableTask> tasks = new HashMap();
    HashMap<String, int[]> levels = new HashMap();
    public static boolean excuse = false;

    public EListener(LumberTycoon plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onHit(EntityDamageByEntityEvent event) {
        if (excuse) {
            excuse = false;
        } else if (event.getEntity() instanceof LivingEntity) {
            LivingEntity damaged = (LivingEntity)event.getEntity();
            LivingEntity damager = event.getDamager() instanceof LivingEntity ? (LivingEntity)event.getDamager() : (event.getDamager() instanceof Projectile ? (LivingEntity)((Projectile)event.getDamager()).getShooter() : null);
            if (event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE) {
                Iterator i$;
                Map.Entry entry;
                if (damager != null) {
                    i$ = this.getValidEnchantments(this.getItems(damager)).entrySet().iterator();

                    while(i$.hasNext()) {
                        entry = (Map.Entry)i$.next();
                        ((CustomEnchantment)entry.getKey()).applyEffect(damager, damaged, (Integer)entry.getValue(), event);
                    }
                }

                i$ = this.getValidEnchantments(this.getItems(damaged)).entrySet().iterator();

                while(i$.hasNext()) {
                    entry = (Map.Entry)i$.next();
                    ((CustomEnchantment)entry.getKey()).applyDefenseEffect(damaged, damager, (Integer)entry.getValue(), event);
                }

            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onDamaged(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity damaged = (LivingEntity)event.getEntity();
            Iterator i$ = this.getValidEnchantments(this.getItems(damaged)).entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
                ((CustomEnchantment)entry.getKey()).applyDefenseEffect(damaged, (LivingEntity)null, (Integer)entry.getValue(), event);
            }

        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onDamaged(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity damaged = (LivingEntity)event.getEntity();
            Iterator i$ = this.getValidEnchantments(this.getItems(damaged)).entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
                ((CustomEnchantment)entry.getKey()).applyDefenseEffect(damaged, (LivingEntity)null, (Integer)entry.getValue(), event);
            }

        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onDamageBlock(BlockDamageEvent event) {
        Iterator i$ = this.getValidEnchantments(this.getItems(event.getPlayer())).entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
            ((CustomEnchantment)entry.getKey()).applyToolEffect(event.getPlayer(), event.getBlock(), (Integer)entry.getValue(), event);
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onBreakBlock(BlockBreakEvent event) {
        Iterator i$ = this.getValidEnchantments(this.getItems(event.getPlayer())).entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
            ((CustomEnchantment)entry.getKey()).applyToolEffect(event.getPlayer(), event.getBlock(), (Integer)entry.getValue(), event);
        }

        if (event.getBlock().getType() == Material.ENCHANTMENT_TABLE) {
            i$ = this.tasks.values().iterator();

            while(i$.hasNext()) {
                TableTask task = (TableTask)i$.next();
                task.restore();
            }
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onInteract(PlayerInteractEvent event) {
        Iterator i$ = this.getValidEnchantments(this.getItems(event.getPlayer())).entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
            ((CustomEnchantment)entry.getKey()).applyMiscEffect(event.getPlayer(), (Integer)entry.getValue(), event);
        }

        (new EEquip(event.getPlayer())).runTaskLater(this.plugin, 1L);
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onInteract(PlayerInteractEntityEvent event) {
        Iterator i$ = this.getValidEnchantments(this.getItems(event.getPlayer())).entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
            ((CustomEnchantment)entry.getKey()).applyEntityEffect(event.getPlayer(), (Integer)entry.getValue(), event);
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onEquip(InventoryClickEvent event) {
        (new EEquip(this.plugin.getServer().getPlayer(event.getWhoClicked().getName()))).runTaskLater(this.plugin, 1L);
    }

    @EventHandler
    public void onBreak(PlayerItemBreakEvent event) {
        (new EEquip(event.getPlayer())).runTaskLater(this.plugin, 1L);
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onConnect(PlayerJoinEvent event) {
        EEquip.loadPlayer(event.getPlayer());
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onDisconnect(PlayerQuitEvent event) {
        EEquip.clearPlayer(event.getPlayer());
        this.levels.remove(event.getPlayer().getName());
        if (this.tasks.containsKey(event.getPlayer().getName())) {
            this.tasks.remove(event.getPlayer().getName());
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onProjectile(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() != null && event.getEntity().getShooter() instanceof LivingEntity) {
            Iterator i$ = this.getValidEnchantments(this.getItems((LivingEntity)event.getEntity().getShooter())).entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
                ((CustomEnchantment)entry.getKey()).applyProjectileEffect((LivingEntity)event.getEntity().getShooter(), (Integer)entry.getValue(), event);
            }

        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (event.getInventory().getType() == InventoryType.ENCHANTING && event.isShiftClick() && this.tasks.containsKey(event.getWhoClicked().getName())) {
            ((TableTask)this.tasks.get(event.getWhoClicked().getName())).restore();
        }

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING && event.getPlayer().hasPermission(PermissionNode.TABLE.getNode())) {
            this.tasks.put(event.getPlayer().getName(), new TableTask(this.plugin, this.plugin.getServer().getPlayer(event.getPlayer().getName())));
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof EnchantingInventory && this.tasks.containsKey(event.getPlayer().getName())) {
            ((TableTask)this.tasks.get(event.getPlayer().getName())).restore();
            ((TableTask)this.tasks.get(event.getPlayer().getName())).cancel();
            this.tasks.remove(event.getPlayer().getName());
        }

    }

    @EventHandler
    public void onPrepare(PrepareItemEnchantEvent event) {
        this.levels.put(event.getEnchanter().getName(), Arrays.copyOf(event.getExpLevelCostsOffered(), 3));
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onEnchant(EnchantItemEvent event) {
        if (event.getEnchanter().hasPermission(PermissionNode.TABLE.getNode()) && this.tasks.containsKey(event.getEnchanter().getName())) {
            event.setCancelled(true);
            if (LumberTycoon.getEnchantments(event.getItem()).size() <= 0) {
                ItemStack storedItem = ((TableTask)this.tasks.get(event.getEnchanter().getName())).stored;
                if (storedItem.getAmount() > 1) {
                    storedItem.setAmount(storedItem.getAmount() - 1);
                    event.getEnchanter().getInventory().addItem(new ItemStack[]{storedItem.clone()});
                    storedItem.setAmount(1);
                }

                int maxEnchants = ((RootConfig)this.plugin.getModuleForClass(RootConfig.class)).getInt(RootNode.MAX_ENCHANTS);
                EnchantResult result = EEnchantTable.enchant(event.getEnchanter(), storedItem, event.getExpLevelCost(), maxEnchants);
                ItemStack item = result.getItem();
                if (item != null) {
                    event.getInventory().setItem(0, (ItemStack)null);
                    event.getEnchantsToAdd().clear();
                    boolean randomName = ((RootConfig)this.plugin.getModuleForClass(RootConfig.class)).getBoolean(RootNode.ITEM_LORE);
                    if (randomName && event.getEnchanter().hasPermission(PermissionNode.NAMES.getNode())) {
                        String name = "§";
                        int random = (int)(Math.random() * 14.0) + 49;
                        if (random > 57) {
                            random += 39;
                        }

                        String format = (String)this.plugin.getConfig().getStringList(LanguageNode.NAME_FORMAT.getFullPath()).get(0);
                        format = format.replace("{adjective}", this.plugin.getAdjective(result.getLevel() / 11 + 1 > 4 ? 4 : result.getLevel() / 11 + 1));
                        format = format.replace("{weapon}", this.plugin.getWeapon(item.getType().name()));
                        format = format.replace("{suffix}", this.plugin.getSuffix(result.getFirstEnchant()));
                        name = name + (char)random + format;
                        ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : this.plugin.getServer().getItemFactory().getItemMeta(item.getType());
                        meta.setDisplayName(name);
                        item.setItemMeta(meta);
                    }

                    event.getInventory().addItem(new ItemStack[]{item});
                    int cost = 1;
                    int[] costs = (int[])this.levels.get(event.getEnchanter().getName());

                    for(int i = 0; i < costs.length; ++i) {
                        if (costs[i] == event.getExpLevelCost()) {
                            cost = i + 1;
                        }
                    }

                    if (event.getEnchanter().getGameMode() != GameMode.CREATIVE) {
                        event.getEnchanter().setLevel(event.getEnchanter().getLevel() - cost);
                        if (event.getInventory().getItem(1).getAmount() <= cost) {
                            event.getInventory().setItem(1, (ItemStack)null);
                        } else {
                            event.getInventory().getItem(1).setAmount(event.getInventory().getItem(1).getAmount() - cost);
                        }
                    }

                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onPrepareEnchant(PrepareItemEnchantEvent event) {
        if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasLore() && event.getItem().getItemMeta().getLore().contains(((TableTask)this.tasks.get(event.getEnchanter().getName())).cantEnchant)) {
            event.setCancelled(true);
        }

    }

    private Map<CustomEnchantment, Integer> getValidEnchantments(ArrayList<ItemStack> items) {
        Map<CustomEnchantment, Integer> validEnchantments = new HashMap();
        Iterator i$ = items.iterator();

        while(true) {
            ItemMeta meta;
            do {
                do {
                    if (!i$.hasNext()) {
                        return validEnchantments;
                    }

                    ItemStack item = (ItemStack)i$.next();
                    meta = item.getItemMeta();
                } while(meta == null);
            } while(!meta.hasLore());

            i$ = meta.getLore().iterator();

            while(i$.hasNext()) {
                String lore = (String)i$.next();
                String name = ENameParser.parseName(lore);
                int level = ENameParser.parseLevel(lore);
                if (name != null && level != 0 && LumberTycoon.isRegistered(name)) {
                    CustomEnchantment enchant = LumberTycoon.getEnchantment(name);
                    if (enchant.canStack() && validEnchantments.containsKey(enchant)) {
                        level += (Integer)validEnchantments.get(enchant);
                    }

                    validEnchantments.put(enchant, level);
                }
            }
        }
    }

    private ArrayList<ItemStack> getItems(LivingEntity entity) {
        ItemStack[] armor = entity.getEquipment().getArmorContents();
        ItemStack weapon = entity.getEquipment().getItemInHand();
        ArrayList<ItemStack> items = new ArrayList(Arrays.asList(armor));
        items.add(weapon);
        return items;
    }
}
