package beans.exampleProject.models.config;

import com.patryklikus.winter.beans.Beans;
import beans.exampleProject.models.fruits.Apple;
import beans.exampleProject.models.fruits.Banana;
import beans.exampleProject.models.fruits.Fruit;

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