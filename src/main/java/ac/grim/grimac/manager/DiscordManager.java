package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.discord.Image;
import ac.grim.grimac.utils.discord.*;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordManager implements Initable {
    private boolean enabled = false;
    private int embedColor;
    private boolean showHead;
    private String staticContent = "";
    private String embedTitle = "";
    private String webhookUrl;

    public static final Pattern WEBHOOK_PATTERN = Pattern.compile("(?:https?://)?(?:\\w+\\.)?\\w+\\.\\w+/api(?:/v\\d+)?/webhooks/(\\d+)/([\\w-]+)(?:/(?:\\w+)?)?");

    @Override
    public void start() {
        enabled = false;
        try {
            if (!GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("enabled", false)) return;
            webhookUrl = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("webhook", "");
            if (webhookUrl.isEmpty()) {
                LogUtil.warn("Discord webhook is empty, disabling Discord alerts");
                return;
            }
            //
            Matcher matcher = WEBHOOK_PATTERN.matcher(webhookUrl);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Failed to parse webhook URL");
            }

            showHead = GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("show-head", true);

            embedTitle = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("embed-title", "**Grim Alert**");

            try {
                embedColor = Color.decode(GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("embed-color", "#00FFFF")).getRGB();
            } catch (NumberFormatException e) {
                LogUtil.warn("Discord embed color is invalid");
            }
            StringBuilder sb = new StringBuilder();
            for (String string : GrimAPI.INSTANCE.getConfigManager().getConfig().getStringListElse("violation-content", getDefaultContents())) {
                sb.append(string).append("\n");
            }
            staticContent = sb.toString();
            enabled = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getDefaultContents() {
        List<String> list = new ArrayList<>();
        list.add("**Player**: %player%");
        list.add("**Check**: %check%");
        list.add("**Violations**: %violations%");
        list.add("**Client Version**: %version%");
        list.add("**Brand**: %brand%");
        list.add("**Ping**: %ping%");
        list.add("**TPS**: %tps%");
        return list;
    }

    public void sendAlert(GrimPlayer player, AbstractCheck check, String verbose, String checkName, int violations) {
        if (!enabled) return;
        String content = staticContent;
        content = content.replace("%check%", check.isExperimental() ? "\\*" + checkName : checkName);
        content = content.replace("%violations%", violations + "");
        if (!verbose.isEmpty()) content = content.replace("%verbose%", verbose);
        content = GrimAPI.INSTANCE.getExternalAPI().replaceVariables(player, content);
        content = content.replace("_", "\\_");
        //
        Embed embed = Embed.builder()
                .color(embedColor)
                .title(embedTitle)
                .description(content)
                .timestamp(Instant.now())
                .footer(new Footer(player.getUniqueId().toString(), "https://grim.ac/images/grim.png"))
                .build();
        //
        if (!verbose.isEmpty()) {
            embed.addField(new Field("Verbose", verbose));
        }

        if (showHead) {
            embed.setThumbnail(new Image("https://crafthead.net/helm/" + player.user.getProfile().getUUID()));
        }
        //
        DiscordMessage message = new DiscordMessage();
        message.addEmbed(embed);
        DiscordMessager.sendAsync(message, webhookUrl);
    }

}
