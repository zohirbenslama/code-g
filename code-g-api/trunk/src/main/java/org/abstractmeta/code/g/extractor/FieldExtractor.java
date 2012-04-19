package org.abstractmeta.code.g.extractor;


import org.abstractmeta.code.g.code.JavaField;
import org.abstractmeta.code.g.code.JavaType;

import java.util.List;

public interface FieldExtractor {

    List<JavaField> extract(JavaType sourceType);

}
