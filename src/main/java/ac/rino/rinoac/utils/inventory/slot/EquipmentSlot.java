package ac.rino.rinoac.utils.inventory.slot;

import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.inventory.EquipmentType;
import ac.rino.rinoac.utils.inventory.InventoryStorage;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.player.GameMode;

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

    public boolean mayPickup(RinoPlayer p_39744_) {
        ItemStack itemstack = this.getItem();
        return (itemstack.isEmpty() || p_39744_.gamemode == GameMode.CREATIVE || itemstack.getEnchantmentLevel(EnchantmentTypes.BINDING_CURSE, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()) == 0) && super.mayPickup(p_39744_);
    }
}
