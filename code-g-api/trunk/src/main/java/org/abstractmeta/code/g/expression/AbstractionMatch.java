package org.abstractmeta.code.g.expression;

import java.util.List;

/**
 * Represents method group match
 *
 * @author Adrian Witas
 */
public interface AbstractionMatch {

    public static final String GROUP_NAME_PLACEHOLDER = "<groupName>";
    public static final String DEFAULT_GROUP_NAME = "";
    
    /**
     * Group name, usually operation postfix
     * For instance for the following matched method: getId, setId,
     * this name is Id
     * @return group name
     */
    String getName();

    /**
     * Returns all method matched for this group match
     * @return
     */
    List<MethodMatch> getMatches();


    /**
     * Checks if supplied operation name (method prefix i.e get, set, etc) has been matched
     * @param operationName operation name
     * @param superParameterTypes method super parameter types
     * @return true if matched
     */
    boolean containsMatch(String operationName, Class  ... superParameterTypes);
 
    
    /**
     * Returns method match for supplied operation name
     * @param operationName operation name
     * @param superParameterTypes method super parameter types
     * @return method match
     */
    MethodMatch getMatch(String operationName, Class ... superParameterTypes);

}
