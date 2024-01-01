package inject.exampleProject.models.config.other;

import com.patryklikus.winter.inject.Config;
import inject.exampleProject.models.Color;

import java.util.List;

@Config
public class ColorConfig {
    public String redText() {
        return "red";
    }

    public Color red(String redText) {
        return new Color(redText);
    }

    public String blueText() {
        return "blue";
    }

    public Color blue(String blueText) {
        return new Color(blueText);
    }

    public List<String> texts() {
        return List.of("Hello", "World", "!");
    }
}