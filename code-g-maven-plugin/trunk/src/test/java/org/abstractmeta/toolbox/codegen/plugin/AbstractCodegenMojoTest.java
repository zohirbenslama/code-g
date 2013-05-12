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
package org.abstractmeta.toolbox.codegen.plugin;

import org.abstractmeta.code.g.core.generator.ClassGenerator;
import org.abstractmeta.toolbox.codegen.plugin.test.View;
import org.apache.maven.artifact.Artifact;
import org.testng.annotations.Test;

import java.io.File;
import java.util.*;

/**
 * Plugin test
 */
@Test
public class AbstractCodegenMojoTest {

    public void testAbstractCodegenMojo() {

        try {
            AbstractCodegenMojo plugin = getPlugin();
            plugin.execute();
        } catch (Exception e) {
            throw new IllegalStateException("Failde to run plugin", e);
        }

    }


    protected AbstractCodegenMojo getPlugin() {
        AbstractCodegenMojo result = new AbstractCodegenMojo() {

            @Override
            protected List<Artifact> getDependencyArtifacts() {
                return Collections.emptyList();
            }
        };

        result.setTargetSourceDirectory(new File("src/test/java").getAbsolutePath());
        result.setBasedir(new File(new File("src").getAbsolutePath()).getParent());
        result.setDescriptors(getDescriptors());
        return result;


    }

    protected ArrayList<Descriptor> getDescriptors() {
        Descriptor descriptor = new Descriptor();
        SourceMatcher sourceMatcher = new SourceMatcher();
        sourceMatcher.setPackageNames(View.class.getPackage().getName());
        descriptor.setSourceMatcher(sourceMatcher);
        descriptor.setGeneratorClass(ClassGenerator.class.getName());
        Properties properties = new Properties();
        properties.setProperty("generateBuilder", "true");
        descriptor.setProperties(properties);
        return new ArrayList<Descriptor>(Arrays.asList(descriptor));
    }


}
