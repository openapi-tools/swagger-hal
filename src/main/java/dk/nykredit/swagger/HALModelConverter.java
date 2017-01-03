package dk.nykredit.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import dk.nykredit.jackson.dataformat.hal.annotation.EmbeddedResource;
import dk.nykredit.jackson.dataformat.hal.annotation.Link;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.models.Model;
import io.swagger.models.properties.AbstractProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;

/**
 * Converter to handle HAL annotated classes ensuring embedded resources and linke are put into an "_embedded" and "_links" object respectively.
 */
public class HALModelConverter implements ModelConverter {

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        Model model = null;

        if (chain.hasNext()) {
            model = chain.next().resolve(type, context, chain);
        }

        if (model != null) {
            Map<HALReservedProperty, ObjectProperty> properties = new HashMap<>();
            Set<String> originalProperties = new HashSet<>();

            for (Map.Entry<String, Property> entry : model.getProperties().entrySet()) {
                if (entry.getValue() instanceof HALProperty) {
                    HALProperty property = (HALProperty) entry.getValue();
                    if (!properties.containsKey(property.getHALType())) {
                        properties.put(property.getHALType(), new ObjectProperty());
                    }
                    String name = property.getSpecificName().isEmpty() ? property.getName() : property.getSpecificName();
                    properties.get(property.getHALType()).property(name, property.getProperty());
                    originalProperties.add(entry.getKey());
                }
            }

            for (Map.Entry<HALReservedProperty, ObjectProperty> entry : properties.entrySet()) {
                model.getProperties().put(entry.getKey().getName(), entry.getValue());
            }

            for (String propertyName : originalProperties) {
                model.getProperties().remove(propertyName);
            }
        }

        return model;
    }

    @Override
    public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations, Iterator<ModelConverter> chain) {
        Property property = null;
        if (chain.hasNext()) {
            property = chain.next().resolveProperty(type, context, annotations, chain);
        }

        if (property != null && !(property instanceof HALProperty) && annotations != null) {
            for (Annotation annotation : annotations) {
                Optional<HALReservedProperty> rp = HALReservedProperty.valueOf(annotation.annotationType());
                if (rp.isPresent()) {
                    return new HALProperty(rp.get(), rp.get().getValue(annotation), property);
                }
            }
        }
        return property;
    }

    /**
     * Enumeration of properties reserved for HAL along with the association to the annotation marking objects to go into these properties.
     */
    public enum HALReservedProperty {
        LINKS("_links", Link.class), EMBEDDED("_embedded", EmbeddedResource.class);

        private final String name;
        private final Class<? extends Annotation> annotation;
        private final Method valueMethod;

        HALReservedProperty(String name, Class<? extends Annotation> annotation) {
            this.name = name;
            this.annotation = annotation;
            try {
                valueMethod = annotation.getDeclaredMethod("value");
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        public String getName() {
            return name;
        }

        public String getValue(Annotation annotation) {
            try {
                return (String) valueMethod.invoke(annotation);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException("Unable to get default value from annotation", e);
            }
        }

        public static Optional<HALReservedProperty> valueOf(Class<? extends Annotation> annotation) {
            for (HALReservedProperty rp : values()) {
                if (rp.annotation.equals(annotation)) {
                    return Optional.of(rp);
                }
            }
            return Optional.empty();
        }
    }

}
