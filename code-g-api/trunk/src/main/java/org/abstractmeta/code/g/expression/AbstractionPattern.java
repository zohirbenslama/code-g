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
public interface AbstractionPattern extends Iterable<MethodPattern> {

    /**
     * Adds method expression
     * @param pattern
     */
    void add(MethodPattern pattern);

    int size();


}
