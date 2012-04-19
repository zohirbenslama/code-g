package org.abstractmeta.code.g;


import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.plugin.CodeGeneratorPluginLoader;

import java.util.Collection;


/**
 * Represents code builder.
 * <p>This abstraction is responsible for code building.
 * It uses code generation plugin to generate specialised code.
 * </p>
 * <p><b>Usage:</b>
 * <code><pre>
 *     CodeBuilder codeBuilder = new CodeBuilderImpl();
 *     DescriptorFactory descriptorFactory = new DescriptorFactory();
 *     codeBuilder.addDescriptor(descriptorFactory.create(Package.getPackage("com.test"), false));
 *     PersistenceHandler persistenceHandler = new PersistenceHandler(new File("target/generated/code-r/"));
 *     codeBuilder.build(persistenceHandler);
 *     List<File> generated = persistenceHandler.getGeneratedFiles();
 *
 * </pre></code>
 * <p/>
 * </p>
 */
public interface CodeBuilder {

    /**
     * Returns plugin loader
     *
     * @return plugin loader
     */
    CodeGeneratorPluginLoader getPluginLoader();

    /**
     * Returns configuration descriptors
     *
     * @return configuration descriptors,
     */
    Collection<Descriptor> getDescriptors();

    /**
     * Adds configuration descriptors to this builder.
     *
     * @param descriptors configuration descriptors
     * @return this code builder
     */
    CodeBuilder addDescriptors(Collection<Descriptor> descriptors);

    /**
     * Adds descriptor
     *
     * @param descriptor configuration descriptor
     * @return this code builder
     */
    CodeBuilder addDescriptor(Descriptor descriptor);

    /**
     * Builds code for the specified descriptors.
     *
     * @param codeHandler code handler
     * @return list of the source code files
     */
    void build(CodeStorageHandler codeHandler);

    /**
     * Builds code for the specified descriptors.
     *
     * @param handler     code persistent handler
     * @param classLoader class loader
     * @return list of the source code files
     */
    void build(CodeStorageHandler handler, ClassLoader classLoader);


}
