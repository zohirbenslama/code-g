package org.abstractmeta.code.g.core.factory;

import org.abstractmeta.code.g.core.config.DescriptorBuilder;
import org.abstractmeta.code.g.core.plugin.BuilderGeneratorPlugin;
import org.abstractmeta.code.g.core.plugin.ClassGeneratorPlugin;
import org.abstractmeta.code.g.config.Descriptor;

/**
 * Descriptor factory.
 * It is convenience class to build ClassGenerator, BuilderGeneratorPlugin descriptor.
 * The following setting are used:
 * <ul><i>ClassGenerator plugin</i><br/>
 * <li><b>targetPackage</b> is defined as [source package].impl. </li>
 * <li><b>targetPostfix</b> is defined as Impl</li>
 * <br/>
 * <i>BuilderGeneratorPlugin plugin </i><br/>
 * <li><b>source</b> is defined as [ClassGenerator.targetPackage]</li>
 * <li><b>targetPostfix</b> is defined as Builder</li>
 * </ul>
 *
 * @author Adrian Witas
 */
public class DescriptorFactory {


    public Descriptor create(Package pkg, boolean builder) {
        DescriptorBuilder descriptorBuilder = new DescriptorBuilder();
        descriptorBuilder.setSource(pkg.getName());
        descriptorBuilder.setTargetPackage(pkg.getName() + ".impl");
        return create(descriptorBuilder, builder, null);
    }

    public Descriptor create(Class sourceType, boolean builder) {
        DescriptorBuilder descriptorBuilder = new DescriptorBuilder();
        descriptorBuilder.setSource(sourceType.getName());
        descriptorBuilder.setTargetPackage(sourceType.getPackage().getName() + ".impl");
        return create(descriptorBuilder, builder, sourceType);
    }

    protected Descriptor create(DescriptorBuilder descriptorBuilder, boolean builder, Class baseType) {
        descriptorBuilder.setTargetPostfix("Impl");
        descriptorBuilder.addPlugins(ClassGeneratorPlugin.class.getSimpleName());
        if (builder) {
            descriptorBuilder.addPlugins(BuilderGeneratorPlugin.class.getSimpleName());
            String source;
            if (baseType == null) {
                source = descriptorBuilder.getSource() + "*";
            }  else {
                if(baseType.isInterface()) {
                    source = descriptorBuilder.getTargetPackage() + "." + baseType.getSimpleName()  + descriptorBuilder.getTargetPostfix();
                } else {
                    source = descriptorBuilder.getSource();
                }
            }
            descriptorBuilder.addOptions(BuilderGeneratorPlugin.class.getSimpleName() + ".source", source);
            descriptorBuilder.addOptions(BuilderGeneratorPlugin.class.getSimpleName() + ".targetPostfix", "Builder");
        }
        return descriptorBuilder.build();
    }

}
