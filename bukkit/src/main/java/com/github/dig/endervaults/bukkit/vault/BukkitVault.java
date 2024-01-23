package com.github.dig.endervaults.bukkit.vault;

import com.github.dig.endervaults.api.util.VaultSerializable;
import com.github.dig.endervaults.api.vault.Vault;
import lombok.Getter;
import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log
public class BukkitVault implements Vault, VaultSerializable {

    private final UUID id;
    private final UUID ownerUUID;
    @Getter
    private Inventory inventory;
    private Map<String, Object> metadata = new HashMap<>();

    public BukkitVault(UUID id, String title, int size, UUID ownerUUID) {
        this.id = id;
        this.ownerUUID = ownerUUID;
        this.inventory = Bukkit.createInventory(new BukkitInventoryHolder(this), size, title);
    }

    public BukkitVault(UUID id, String title, int size, UUID ownerUUID, Map<String, Object> metadata) {
        this(id, title, size, ownerUUID);
        this.metadata = metadata;
    }

    public BukkitVault(UUID id, UUID ownerUUID, Inventory inventory, Map<String, Object> metadata) {
        this.id = id;
        this.ownerUUID = ownerUUID;
        this.inventory = inventory;
        this.metadata = metadata;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getOwner() {
        return ownerUUID;
    }

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public int getFreeSize() {
        int free = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                free++;
            }
        }
        return free;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    @Nullable
    public String encode() {
        ItemStack[] inventoryContents = inventory.getContents();
        return BukkitVaultSerialization.encodeItemStacksToString(inventoryContents);
    }

    @Override
    public void decode(String encoded) {
        inventory.setContents(BukkitVaultSerialization.decodeItemStacks(encoded));
    }

    public void launchFor(Player player) {
        player.openInventory(inventory);
    }

    public boolean compare(Inventory inventory) {
        return this.inventory == inventory;
    }
}
