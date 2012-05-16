package org.abstractmeta.code.g.expression;

import java.util.List;

/**
* Represents a method pattern expression.
 * This expression uses the following attribute to match methods:
 * <ul>
 * <li><b>operation name</b> defined as fragment of</br>
 * <b>method name</b> = [operation name fragment]  + [group name fragment] i.e. setName, getName
 *  </li>
 * <li><b>base argument type</b> to be matched with method parameters<br/>
 * <li><b>base result type type</b>to be matched with method result type<br/>
 * <li><b>method parameter AbstractionPattern</b>to be matched with another abstraction pattern</li>
 * <li><b>method result AbstractionPattern</b>to be matched with another abstraction pattern</li>

 *  </ul>
 *  <i>Object.class</i> can be used as any type
 *
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

    /**
     * Returns true if abstraction pattern has been defined for a given parameter.
     * @param indexParameter abstraction
     * @return
     */
    boolean hasParameterAbstractionPattern(int indexParameter);

    AbstractionPattern gatParameterAbstractionPattern(int indexParameter);

    AbstractionMatch getResultAbstractionPattern();

    

}
