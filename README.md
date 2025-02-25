# Dependency Injection Helper Demo

Dependency Injection Helper Demo is an experimental Java library created as part of a personal coding challenge. This
project aims to demonstrate fundamental techniques of dependency injection and object lifecycle management in Java
applications. Although not all lifecycle features are fully implemented, it serves as a valuable resource for learning
and gaining experience.

## Example usage

```java
class Launcher {
    public static void main(String[] args) {
        BeanProvider beanProvider = new BeanProviderImpl(Main.class);
        beanProvider.init();
        lifecycleHandler = new LifecycleHandlerImpl(beanProvider.getBeans());
        lifecycleHandler.init();
    }
}
```

```java

@Beans
public class Config {
    public String red() {
        return "red";
    }

    @Run(enabled = false, order = 1)
    public Color redColor(String red) {
        return new Color(red);
    }
}
```

## Author

[@Patryk Likus](https://www.linkedin.com/in/patryklikus)

## License

GNU General Public License
