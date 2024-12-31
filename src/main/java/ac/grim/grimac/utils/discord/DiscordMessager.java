package ac.grim.grimac.utils.discord;


import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.shaded.io.packetevents.util.folia.FoliaScheduler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordMessager {

    public static void sendAsync(DiscordMessage message, final String webhook_url) {
        FoliaScheduler.getAsyncScheduler().runNow(GrimAPI.INSTANCE.getPlugin(), (o) -> send(serializeMessage(message), webhook_url));
    }

    public static String serializeMessage(DiscordMessage message) {
        JsonObject object = new JsonObject();
        if (message.getContent() != null && !message.getContent().isEmpty()) {
            object.addProperty("content", message.getContent());
        }
        //
        if (message.getEmbeds() != null && !message.getEmbeds().isEmpty()) {
            JsonArray array = new JsonArray();
            object.add("embeds", array);
            for (Embed embed : message.getEmbeds()) {
                JsonObject embedObject = new JsonObject();
                array.add(embedObject);
                if (embed.getTitle() != null && !embed.getTitle().isEmpty())
                    embedObject.addProperty("title", embed.getTitle());
                if (embed.getDescription() != null && !embed.getDescription().isEmpty())
                    embedObject.addProperty("description", embed.getDescription());
                if (embed.getUrl() != null && !embed.getUrl().isEmpty()) embedObject.addProperty("url", embed.getUrl());
                embedObject.addProperty("color", embed.getColor());
                // footer
                if (embed.getFooter() != null) {
                    JsonObject footerObject = new JsonObject();
                    embedObject.add("footer", footerObject);
                    if (embed.getFooter().getText() != null && !embed.getFooter().getText().isEmpty())
                        footerObject.addProperty("text", embed.getFooter().getText());
                    if (embed.getFooter().getIcon_url() != null && !embed.getFooter().getIcon_url().isEmpty())
                        footerObject.addProperty("icon_url", embed.getFooter().getIcon_url());
                    embedObject.add("footer", footerObject);
                }
                // author
                if (embed.getAuthor() != null) {
                    JsonObject authorObject = new JsonObject();
                    embedObject.add("author", authorObject);
                    if (embed.getAuthor().getName() != null && !embed.getAuthor().getName().isEmpty())
                        authorObject.addProperty("name", embed.getAuthor().getName());
                    if (embed.getAuthor().getUrl() != null && !embed.getAuthor().getUrl().isEmpty())
                        authorObject.addProperty("url", embed.getAuthor().getUrl());
                    if (embed.getAuthor().getIcon_url() != null && !embed.getAuthor().getIcon_url().isEmpty())
                        authorObject.addProperty("icon_url", embed.getAuthor().getIcon_url());
                    embedObject.add("author", authorObject);
                }
                // thumbnail
                if (embed.getThumbnail() != null) {
                    JsonObject thumbnailObject = new JsonObject();
                    embedObject.add("thumbnail", thumbnailObject);
                    if (embed.getThumbnail().getUrl() != null && !embed.getThumbnail().getUrl().isEmpty())
                        thumbnailObject.addProperty("url", embed.getThumbnail().getUrl());
                    embedObject.add("thumbnail", thumbnailObject);
                }
                // image
                if (embed.getImage() != null) {
                    JsonObject imageObject = new JsonObject();
                    embedObject.add("image", imageObject);
                    if (embed.getImage().getUrl() != null && !embed.getImage().getUrl().isEmpty())
                        imageObject.addProperty("url", embed.getImage().getUrl());
                    embedObject.add("image", imageObject);
                }
                // fields
                if (embed.getFields() != null && !embed.getFields().isEmpty()) {
                    JsonArray fieldsArray = new JsonArray();
                    embedObject.add("fields", fieldsArray);
                    for (Field field : embed.getFields()) {
                        JsonObject fieldObject = new JsonObject();
                        fieldsArray.add(fieldObject);
                        if (field.getName() != null && !field.getName().isEmpty())
                            fieldObject.addProperty("name", field.getName());
                        if (field.getValue() != null && !field.getValue().isEmpty())
                            fieldObject.addProperty("value", field.getValue());
                        fieldObject.addProperty("inline", field.isInline());
                        embedObject.add("fields", fieldObject);
                    }
                }
            }
        }

        return object.toString();
    }

    public static void send(final String json, final String webhook_url) {
        try {
            URL url = new URL(webhook_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "GrimAC/" + GrimAPI.INSTANCE.getExternalAPI().getGrimVersion());
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(json.getBytes());
                outputStream.flush();
            } finally {
                connection.getInputStream().close();
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
