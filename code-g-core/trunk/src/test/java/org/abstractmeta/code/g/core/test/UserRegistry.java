package org.abstractmeta.code.g.core.test;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: adrianwit
 * Date: 5/7/13
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserRegistry {

    Map<String, User> getRegistry();

    void register(User user);

    User get(String name);

}

