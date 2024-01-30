package beans.exampleProject.models.config.other;

import beans.exampleProject.models.Color;
import com.patryklikus.winter.beans.Beans;
import com.patryklikus.winter.lifecycle.Close;
import com.patryklikus.winter.lifecycle.Init;
import com.patryklikus.winter.lifecycle.Run;

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

    public List<String> textList() {
        return List.of("Hello", "World", "!");
    }

    public String text(List<String> textList) {
        return String.join(" ", textList);
    }
}