# Winter

Winter is a library that provides a mechanism for dependency injection and allows easy management of the life cycle of
objects.

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

All rights reserved. If you want to use it [contact me](https://www.linkedin.com/in/patryklikus).
