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
