package ac.grim.grimac.utils.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Footer {

    public Footer(String text) {
        this.text = text;
    }

    private String text;
    private String icon_url;

}
