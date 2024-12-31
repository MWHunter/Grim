package ac.grim.grimac.utils.discord;


import lombok.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.LinkedList;
import java.util.List;


@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Embed {

    private final String title;
    private final String description;
    private String url;
    private int color;
    private String timestamp;
    @Setter private Image thumbnail;
    private Image image;
    private Author author;
    @Builder.Default private final List<Field> fields = new LinkedList<>();
    private Footer footer;

    public void addField(Field field) {
        fields.add(field);
    }

    public static int getColor(String hex) {
        return Integer.parseInt(hex.replaceFirst("#", ""), 16);
    }

    public static class EmbedBuilder {

        public EmbedBuilder timestamp(TemporalAccessor timestamp) {
            OffsetDateTime time;
            if (timestamp instanceof Instant) {
                time = OffsetDateTime.ofInstant((Instant) timestamp, ZoneId.of("UTC"));
            } else {
                time = timestamp == null ? null : OffsetDateTime.from(timestamp);
            }
            if (time != null) this.timestamp = time.toString();
            return this;
        }

    }


}
