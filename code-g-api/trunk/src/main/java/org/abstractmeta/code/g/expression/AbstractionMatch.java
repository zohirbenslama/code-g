/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
