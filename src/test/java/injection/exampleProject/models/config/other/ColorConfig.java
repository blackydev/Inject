package injection.exampleProject.models.config.other;

import com.patryklikus.winter.injection.Beans;
import com.patryklikus.winter.lifecycle.Close;
import com.patryklikus.winter.lifecycle.Init;
import com.patryklikus.winter.lifecycle.Run;
import injection.exampleProject.models.Color;

import java.util.List;

@Beans
public class ColorConfig {
    public String red() {
        return "red";
    }

    @Init(enabled = false, order = 1)
    @Run(enabled = false)
    @Close(enabled = false, order = 3)
    public Color redColor(String red) {
        return new Color(red);
    }

    public String blue() {
        return "blue";
    }

    public Color blueColor(String blue) {
        return new Color(blue);
    }

    public List<String> texts() {
        return List.of("Hello", "World", "!");
    }
}