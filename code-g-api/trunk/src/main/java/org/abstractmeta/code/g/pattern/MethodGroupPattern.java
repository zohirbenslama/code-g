package org.abstractmeta.code.g.pattern;

/**
 * Method Group Pattern.
 * This abstraction groups all method patterns by group name fragment.
 * <p>Consider following method fragment </p>
 * <b>method name</b> = [operation name fragment]  + [group name fragment]
 * <p>
 * <i>setName, getName</i> where operation name = get,set; group name = Name<br/>
 * <i>registerFoo, unregisterFoo, isRegisteredFoo</i> where operation name = register,unregister, isRegistered; group name = Foo</br/>
 * </p>
 * @author Adrian Witas
 */
public interface MethodGroupPattern extends Iterable<MethodPattern> {

    /**
     * Adds method pattern
     * @param pattern
     */
    void add(MethodPattern pattern);

    int size();


}
