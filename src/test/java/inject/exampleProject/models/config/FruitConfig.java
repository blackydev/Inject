package inject.exampleProject.models.config;

import com.patryklikus.winter.inject.Config;
import inject.exampleProject.models.fruits.Apple;
import inject.exampleProject.models.fruits.Banana;
import inject.exampleProject.models.fruits.Fruit;

@Config
public class FruitConfig {
    public Apple apple() {
        return new Apple();
    }

    public Fruit bananaFruit() {
        return new Banana();
    }
}