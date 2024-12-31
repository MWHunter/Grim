package ac.grim.grimac.utils.inventory.slot;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.packetevents.protocol.player.GameMode;
import ac.grim.grimac.utils.inventory.EquipmentType;
import ac.grim.grimac.utils.inventory.InventoryStorage;

public class EquipmentSlot extends Slot {
    EquipmentType type;

    public EquipmentSlot(EquipmentType type, InventoryStorage menu, int slot) {
        super(menu, slot);
        this.type = type;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(ItemStack p_39746_) {
        return type == EquipmentType.getEquipmentSlotForItem(p_39746_);
    }

    public boolean mayPickup(GrimPlayer p_39744_) {
        ItemStack itemstack = this.getItem();
        return (itemstack.isEmpty() || p_39744_.gamemode == GameMode.CREATIVE || itemstack.getEnchantmentLevel(EnchantmentTypes.BINDING_CURSE, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()) == 0) && super.mayPickup(p_39744_);
    }
}
