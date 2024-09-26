package ac.rino.rinoac.checks.impl.crash;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.impl.exploit.ExploitA;
import ac.rino.rinoac.checks.type.PacketCheck;
import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;

@CheckData(name = "CrashE", experimental = false)
public class CrashE extends Check implements PacketCheck {

    public CrashE(RinoPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLIENT_SETTINGS) {
            WrapperPlayClientSettings wrapper = new WrapperPlayClientSettings(event);
            int viewDistance = wrapper.getViewDistance();
            boolean invalidLocale = player.checkManager.getPrePredictionCheck(ExploitA.class).checkString(wrapper.getLocale());
            if (viewDistance < 2) {
                flagAndAlert("distance=" + viewDistance);
                wrapper.setViewDistance(2);
            }
            if (invalidLocale) wrapper.setLocale("en_us");
        }
    }

}
