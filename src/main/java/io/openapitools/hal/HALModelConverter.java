package io.openapitools.hal;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.AbstractModelConverter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Converter to handle HAL annotated classes.
 *
 * It ensures embedded resources and links are arranged into an "_embedded" and "_links" object respectively.
 */
public class HALModelConverter extends AbstractModelConverter {

    public HALModelConverter() {
        this(Json.mapper());
    }

    public HALModelConverter(ObjectMapper mapper) {
        super(mapper);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (!chain.hasNext()) {
            return null;
        }

        Schema<?> originalSchema = chain.next().resolve(annotatedType, context, chain);
        if (originalSchema == null) {
            return null;
        }
        Schema<?> schema = originalSchema;

        if (originalSchema.get$ref() != null) {
            schema = context.resolve(annotatedType);
        }

        Map<String, Schema> properties = schema.getProperties();
        if (properties == null) {
            return originalSchema;
        }

        final JavaType javaType;
        if (annotatedType.getType() instanceof JavaType) {
            javaType = (JavaType) annotatedType.getType();
        } else {
            javaType = _mapper.constructType(annotatedType.getType());
        }
        BeanDescription description = _mapper.getSerializationConfig().introspect(javaType);
        HALResourceType type = new HALResourceType(description);
        if (!type.isHALResource()) {
            return originalSchema;
        }

        Map<String, Schema> updatedProps = updateProperties(properties, type);
        schema.setProperties(updatedProps);

        if (originalSchema.get$ref() != null) {
            context.defineModel(schema.getName(), schema);
        }

        return originalSchema;
    }

    /**
     * Create updated properties map.
     */
    @SuppressWarnings("rawtypes")
    private Map<String, Schema> updateProperties(Map<String, Schema> properties, HALResourceType type) {
        Map<String, Schema> updatedProps = new HashMap<>();
        properties.forEach((propertyName, propertySchema) -> {
            Optional<Link> annotation = type.getAnnotation(HALAnnotation.LINKS.getAnnotation(), propertyName);
            if (annotation.isPresent()) {
                Schema<?> halSchema = updatedProps.getOrDefault(HALAnnotation.LINKS.getName(), new ObjectSchema());
                updatedProps.putIfAbsent(HALAnnotation.LINKS.getName(), halSchema);

                String name = HALAnnotation.LINKS.deriveName(type, annotation.get(), propertyName);
                halSchema.addProperties(name, propertySchema);

            } else {
                Optional<EmbeddedResource> embedded = type.getAnnotation(HALAnnotation.EMBEDDED.getAnnotation(), propertyName);
                if (embedded.isPresent()) {
                    Schema<?> halSchema = updatedProps.getOrDefault(HALAnnotation.EMBEDDED.getName(), new ObjectSchema());
                    updatedProps.putIfAbsent(HALAnnotation.EMBEDDED.getName(), halSchema);

                    String name = HALAnnotation.EMBEDDED.deriveName(type, embedded.get(), propertyName);
                    halSchema.addProperties(name, propertySchema);

                } else {
                    updatedProps.put(propertyName, propertySchema);
                }
            }
        });
        return updatedProps;
    }
}
