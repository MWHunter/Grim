package ac.rino.rinoac.checks.impl.badpackets;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.PacketCheck;
import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;

@CheckData(name = "BadPacketsP", experimental = true)
public class BadPacketsP extends Check implements PacketCheck {

    public BadPacketsP(RinoPlayer playerData) {
        super(playerData);
    }

    private int containerType = -1;
    private int containerId = -1;

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.OPEN_WINDOW) {
            WrapperPlayServerOpenWindow window = new WrapperPlayServerOpenWindow(event);
            this.containerType = window.getType();
            this.containerId = window.getContainerId();
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow wrapper = new WrapperPlayClientClickWindow(event);
            int clickType = wrapper.getWindowClickType().ordinal();
            int button = wrapper.getButton();

            boolean flag = false;

            //TODO: Adjust for containers
            switch (clickType) {
                case 0:
                case 1:
                case 4:
                    if (button != 0 && button != 1) flag = true;
                    break;
                case 2:
                    if ((button > 8 || button < 0) && button != 40) flag = true;
                    break;
                case 3:
                    if (button != 2) flag = true;
                    break;
                case 5:
                    if (button == 3 || button == 7 || button > 10 || button < 0) flag = true;
                    break;
                case 6:
                    if (button != 0) flag = true;
                    break;
            }

            //Allowing this to false flag to debug and find issues faster
            if (flag) {
                if (flagAndAlert("clickType=" + clickType + " button=" + button + (wrapper.getWindowId() == containerId ? " container=" + containerType : "")) && shouldModifyPackets()) {
                    event.setCancelled(true);
                    player.onPacketCancel();
                }
            }
        }
    }
}
