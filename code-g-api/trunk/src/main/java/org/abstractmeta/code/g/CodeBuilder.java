package org.abstractmeta.code.g;


import org.abstractmeta.code.g.config.Descriptor;

import java.util.Collection;
import java.util.List;


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
     * @param handler     code persistent handler
     * @param classLoader class loader
     * @return list of the source code files
     */
    void build(CodeStorageHandler handler, ClassLoader classLoader);


    /**
     * Builds code for the specified descriptors.
     *
     * @param handler     code persistent handler
     * @return list of the source code files
     */
    void build(CodeStorageHandler handler);


}
