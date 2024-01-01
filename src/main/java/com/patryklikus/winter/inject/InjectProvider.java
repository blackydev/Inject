package com.patryklikus.winter.inject;

import com.patryklikus.winter.lifecycle.Initializable;

public interface InjectProvider extends Initializable {
     /**
      * Searches recursively all packages for classes with {@link Config} annotation.
      * Checks that superclass is equal to {@link Object} if not throws {@link InjectInitializationException}.
      * Retrieves all methods from these classes. Filters nonpublic methods. Sorts methods by parameters count.
      * Creates objects using these methods and saves them.
      *
      * @throws InjectInitializationException if any {@link Config} superclass is other than {@link Object}
      */
     void init() throws InjectInitializationException;

     <T> T getInject(String name, Class<T> classType);
}
