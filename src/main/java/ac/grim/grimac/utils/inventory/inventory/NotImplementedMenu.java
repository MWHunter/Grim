package ac.grim.grimac.utils.inventory.inventory;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import ac.grim.grimac.utils.inventory.Inventory;

public class NotImplementedMenu extends AbstractContainerMenu {
    public NotImplementedMenu(GrimPlayer player, Inventory playerInventory) {
        super(player, playerInventory);
        player.getInventory().isPacketInventoryActive = false;
        player.getInventory().needResend = true;
    }

    @Override
    public void doClick(int button, int slotID, WrapperPlayClientClickWindow.WindowClickType clickType) {

    }
}
