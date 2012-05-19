package org.abstractmeta.code.g.examples.model4.builder;

import org.abstractmeta.code.g.examples.model4.Employee;
import org.abstractmeta.code.g.examples.model4.impl.EmployeeImpl;

public class EmployeeBuilder {
    private String name;
    private boolean _name;
    private int id;
    private boolean _id;


    public EmployeeBuilder setName(String name) {
        this.name = name;
        this._name = true;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasName() {
        return this._name;
    }

    public EmployeeBuilder setId(int id) {
        this.id = id;
        this._id = true;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public boolean hasId() {
        return this._id;
    }

    public Employee build() {
        Employee result = new EmployeeImpl(name, id);
        return result;
    }

}