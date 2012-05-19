package org.abstractmeta.code.g;


import org.abstractmeta.code.g.config.Descriptor;
import org.abstractmeta.code.g.handler.CodeHandler;


/**
 * Represents a code builder.
 * <p>This abstraction is responsible for building code.
 * It uses code generation plugins to generate specialised code.
 * </p>
 * <p><b>Usage:</b>
 * <code><pre>
 *     CodeGenerator codeGenerator = new CodeBuilderImpl();
 *     DescriptorFactory descriptorFactory = new DescriptorFactory();
 *     Collection&lt;Descriptor> descriptors = Arrays.asList(
 *         ...
 *     );
 *     PersistenceHandler persistenceHandler = new PersistenceHandler(new File("target/generated/code-r/"));
 *     codeGenerator.generate(descriptors, persistenceHandler);
 *     List<File> generated = persistenceHandler.getGeneratedFiles();
 *
 * </pre></code>
 * <p/>
 * </p>
 */
public interface CodeGenerator {


    
    /**
     * Builds code for the specified descriptors.
     *
     * @param descriptors descriptors
     * @param handler     code handler
     * @param classLoader class loader
     */
    void generate(Iterable<Descriptor> descriptors, CodeHandler handler, ClassLoader classLoader);


    /**
     * Builds code for the specified descriptors.
     *
     * @param descriptors descriptors
     * @param handler     code handler
     */
    void generate(Iterable<Descriptor> descriptors, CodeHandler handler);


}
