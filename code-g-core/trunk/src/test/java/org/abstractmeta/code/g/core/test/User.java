package org.abstractmeta.code.g.core.test;


import java.util.List;

public interface User {

    int getId();

    List<String> getAliases();

    int getSequenceChangeNo();

    void setSequenceChangeNo(int scn);

}
