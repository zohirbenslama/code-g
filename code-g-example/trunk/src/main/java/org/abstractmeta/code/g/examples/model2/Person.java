package org.abstractmeta.code.g.examples.model2;

/**
 * Represents Person
 * <p>
 * </p>
 *
 * @author Adrian Witas
 */
public class Person {
    
    private int id;
    private String name;
    private String email;
    private Address address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
