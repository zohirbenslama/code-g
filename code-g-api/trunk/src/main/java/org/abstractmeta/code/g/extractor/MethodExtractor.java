package org.abstractmeta.code.g.extractor;


import org.abstractmeta.code.g.code.JavaMethod;
import org.abstractmeta.code.g.code.JavaType;

import java.util.List;

public interface MethodExtractor {

    List<JavaMethod> extract(JavaType sourceType);

}
