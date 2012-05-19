package org.abstractmeta.code.g.examples.model4.impl;

import org.abstractmeta.code.g.examples.model4.Employee;

public class EmployeeImpl implements Employee {
    private final String name;
    private final int id;

    public EmployeeImpl(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

}