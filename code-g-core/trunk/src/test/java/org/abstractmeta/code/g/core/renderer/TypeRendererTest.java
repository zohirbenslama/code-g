/**
 * Copyright 2011 Adrian Witas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.abstractmeta.code.g.core.renderer;

import org.abstractmeta.code.g.code.JavaType;
import org.abstractmeta.code.g.code.JavaTypeImporter;
import org.abstractmeta.code.g.core.code.JavaTypeImporterImpl;
import org.abstractmeta.code.g.core.code.builder.JavaFieldBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaMethodBuilder;
import org.abstractmeta.code.g.core.code.builder.JavaTypeBuilder;
import org.abstractmeta.code.g.core.provider.ClassTypeProvider;
import org.abstractmeta.code.g.core.renderer.TypeRenderer;
import org.abstractmeta.code.g.renderer.JavaTypeRenderer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

@Test
public class TypeRendererTest {

    public void testRender() {
        JavaTypeBuilder typeBuilder = new JavaTypeBuilder();
        typeBuilder.setTypeName("com.foo.Test");
        typeBuilder.addField(new JavaFieldBuilder().setName("field1").setType(int.class).setImmutable(true).build());
        typeBuilder.addField((new JavaFieldBuilder().setName("field2").setType(String.class).build()));
        typeBuilder.addMethod(new JavaMethodBuilder().setName("test").setResultType(void.class)
                .addParameter("arg1", Map.class).build());

        JavaTypeRenderer renderer = new TypeRenderer();
        {
            JavaTypeImporter importer = new JavaTypeImporterImpl(typeBuilder.getPackageName());
            String classFragment = renderer.render(typeBuilder.build(), importer, 0);

            Assert.assertTrue(classFragment.contains("final int field1;"));
            Assert.assertTrue(classFragment.contains("String field2;"));
        }
        JavaType bar = new ClassTypeProvider(Bar.class).get();
        {
            JavaTypeImporter importer = new JavaTypeImporterImpl(bar.getPackageName());
            String classFragment = renderer.render(bar, importer, 0);
            Assert.assertEquals(classFragment, "package org.abstractmeta.code.g.core.renderer;\n" +
                    "\n" +
                    "import java.io.IOException;\n" +
                    "\n" +
                    "class Bar extends Object {\n" +
                    "\n" +
                    "    public Bar() throws IOException{\n" +
                    "        \n" +
                    "    }\n" +
                    "\n" +
                    "    public void add() throws Exception{\n" +
                    "        \n" +
                    "    }\n" +
                    "\n" +
                    "}");
        }

    }


    public static class Bar {

        public  Bar() throws IOException  {

        }
        public void add() throws Exception {

        }
    }

}
