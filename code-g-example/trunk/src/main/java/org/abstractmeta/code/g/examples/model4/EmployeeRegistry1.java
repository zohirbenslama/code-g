package org.abstractmeta.code.g.examples.model4;


import java.util.Map;

public interface EmployeeRegistry1 {


    void register(int id, String name, String group, Employee employee);

    Employee get(int id, String name, String group);

    Map<Integer, Map<String, Map<String, Employee>>> getRegistry();

}
