package ac.rino.rinoac.utils.inventory.inventory;

import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.inventory.Inventory;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

public class NotImplementedMenu extends AbstractContainerMenu {
    public NotImplementedMenu(RinoPlayer player, Inventory playerInventory) {
        super(player, playerInventory);
        player.getInventory().isPacketInventoryActive = false;
        player.getInventory().needResend = true;
    }

    @Override
    public void doClick(int button, int slotID, WrapperPlayClientClickWindow.WindowClickType clickType) {

    }
}
