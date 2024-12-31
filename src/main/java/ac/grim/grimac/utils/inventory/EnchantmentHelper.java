package ac.grim.grimac.utils.inventory;

import ac.grim.grimac.shaded.com.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.packetevents.protocol.item.enchantment.type.EnchantmentType;
import ac.grim.grimac.shaded.com.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.latency.CompensatedInventory;

public class EnchantmentHelper {
    public static boolean isCurse(EnchantmentType type) {
        return type == EnchantmentTypes.BINDING_CURSE || type == EnchantmentTypes.VANISHING_CURSE;
    }

    // Some enchants work on any armor piece but only the maximum level counts
    public static int getMaximumEnchantLevel(CompensatedInventory inventory, EnchantmentType enchantmentType, ClientVersion clientVersion) {
        int maxEnchantLevel = 0;

        ItemStack helmet = inventory.getHelmet();
        if (helmet != ItemStack.EMPTY) {
            maxEnchantLevel = Math.max(maxEnchantLevel, helmet.getEnchantmentLevel(enchantmentType, clientVersion));
        }

        ItemStack chestplate = inventory.getChestplate();
        if (chestplate != ItemStack.EMPTY) {
            maxEnchantLevel = Math.max(maxEnchantLevel, chestplate.getEnchantmentLevel(enchantmentType, clientVersion));
        }

        ItemStack leggings = inventory.getLeggings();
        if (leggings != ItemStack.EMPTY) {
            maxEnchantLevel = Math.max(maxEnchantLevel, leggings.getEnchantmentLevel(enchantmentType, clientVersion));
        }

        ItemStack boots = inventory.getBoots();
        if (boots != ItemStack.EMPTY) {
            maxEnchantLevel = Math.max(maxEnchantLevel, boots.getEnchantmentLevel(enchantmentType, clientVersion));
        }

        return maxEnchantLevel;
    }
}
