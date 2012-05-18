package org.abstractmeta.code.g.core.generator.test;

import org.abstractmeta.code.g.core.test.Bar;
import org.abstractmeta.code.g.core.test.Foo;

import java.util.List;

/**
 * Represents CollectionTest
*
 * @author Adrian Witas
 */
public abstract class CollectionTest {
    
    public abstract List<Bar> getBars();

     public abstract void addBar(Bar bar) ;
    
    public abstract  Bar getBar(int id);

    public abstract  Bar getBar(String name);

    public abstract List<Foo> getFoos();
}
