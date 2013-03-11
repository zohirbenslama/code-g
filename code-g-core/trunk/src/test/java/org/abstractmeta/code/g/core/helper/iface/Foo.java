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
package org.abstractmeta.code.g.core.helper.iface;

import java.util.List;
import java.util.Map;


public interface Foo {

    int getId();
    
    Bar getBar();
    
    void setBar(Bar bar);
    
    List<Bar> getBarList();
    
    Map<String, Bar> getBarMap();
    
    Bar [] getBars();

}
