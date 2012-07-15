package org.abstractmeta.code.g.examples.model4;


import java.util.Map;

public interface EmployeeRegistry2 {
    
    void register(int id, Employee employee);
    
    Employee get(int id);
    
    Map<Integer, Employee> getRegistry();
    
}
