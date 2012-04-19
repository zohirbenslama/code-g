package org.abstractmeta.code.g.common.test;

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
