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
package org.abstractmeta.code.g.core.test;

import java.util.Collection;
import java.util.Map;

/**
 * Represents IMessage
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class AMessage {

    private final int id;
    private String type;
    private int counter;
    private Map<String, String> map;
    private Collection<String> collection;


    public AMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Collection<String> getCollection() {
        return collection;
    }

    public int getCounter() {
        return counter;
    }

    public void increaseCounter() {
        counter++;
    }

    public void decreaseCounter() {
        counter--;
    }

    public void resetCounter(int counter) {
        this.counter  = counter;
    }


    public void setCollection(Collection<String> collection) {
        this.collection = collection;
    }
}
