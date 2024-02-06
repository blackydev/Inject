/* Copyright patryklikus.com All Rights Reserved. */
package beans.exampleProject;

import com.patryklikus.winter.beans.BeanProvider;
import com.patryklikus.winter.beans.BeanProviderImpl;
import com.patryklikus.winter.framework.Winter;
import com.patryklikus.winter.lifecycle.LifecycleHandler;
import com.patryklikus.winter.lifecycle.LifecycleHandlerImpl;

@Winter
public class Main {
    public static void main(String[] args) {
        BeanProvider beanProvider = new BeanProviderImpl();
        beanProvider.init();
        LifecycleHandler lifecycleHandler = new LifecycleHandlerImpl();
    }
}