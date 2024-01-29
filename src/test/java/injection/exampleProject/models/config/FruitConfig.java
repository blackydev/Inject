package injection.exampleProject.models.config;

import com.patryklikus.winter.injection.Beans;
import injection.exampleProject.models.fruits.Apple;
import injection.exampleProject.models.fruits.Banana;
import injection.exampleProject.models.fruits.Fruit;

@Beans
public class FruitConfig {
    public Apple apple() {
        return new Apple();
    }

    public Fruit bananaFruit() {
        return new Banana();
    }

    protected Banana protectedBananaFruit() {
        return new Banana();
    }

    private Fruit privateBananaFruit() {
        return new Banana();
    }
}