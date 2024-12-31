package ac.grim.grimac.utils.discord;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class DiscordMessage {

    private String content;
    private List<Embed> embeds = new LinkedList<>();

    public DiscordMessage addEmbed(Embed embed) {
        this.embeds.add(embed);
        return this;
    }

    public DiscordMessage setContent(String content) {
        this.content = content;
        return this;
    }

}
