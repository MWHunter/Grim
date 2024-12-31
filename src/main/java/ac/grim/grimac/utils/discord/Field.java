package ac.grim.grimac.utils.discord;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Field {

    private final String name;
    private final String value;
    private boolean inline;

}
