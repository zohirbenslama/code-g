package org.abstractmeta.code.g.pattern;

import java.util.List;

/**
* Represents method pattern.
 * This pattern uses the following attribute to match java methods:
 * <ul>
 * <li><b>operation name</b> defined as fragment of</br>
 * <b>method name</b> = [operation name fragment]  + [group name fragment] i.e. setName, getName
 *  </li>
 * <li><b>base argument type</b> to be matched with method parameters<br/>
 * <li><b>base result type type</b>to be matched with method result type<br/>
 *  </ul>
 *  <i>Object.class</i> can be used as any type
 *
* @author Adrian Witas
*/
public interface MethodPattern {

    /**
     * List of operation name
     * @return
     */
    List<String> getOperationNames();

    boolean isSingularNameMatching();

    List<String> getModifiers();

    List<Class> getBaseParameterTypes();

    Class getBaseResultType();

}
