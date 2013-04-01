package org.abstractmeta.code.g.core.test;

/**
 * Represents IMessage
 *
 * @author Adrian Witas
 */
public class Message {
    private final int id;
    private final String name;
    private String description;
    private String source;

    public Message(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
