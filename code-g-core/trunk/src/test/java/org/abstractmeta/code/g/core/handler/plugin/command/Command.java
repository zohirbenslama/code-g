package org.abstractmeta.code.g.core.handler.plugin.command;

/**
 * A simple command.
 */
public abstract class Command<T, V>  {

    private final String field;
    private final V previousValue;
    private final V value;
    private final boolean id;
    protected Command(String field, V previousValue, V value, boolean id) {
        this.field = field;
        this.previousValue = previousValue;
        this.value = value;
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public V getPreviousValue() {
        return previousValue;
    }

    public V getValue() {
        return value;
    }

    public boolean isId() {
        return id;
    }

    public abstract void execute(T instance);


}

        
