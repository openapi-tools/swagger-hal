package io.openapitools.hal;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;

/**
 * Converter to handle HAL annotated classes.
 *
 * It ensures embedded resources and links are arranged into an "_embedded" and "_links" object respectively.
 */
public class HALModelConverter extends ModelResolver {
    public static final String HAL_RESERVED_PROPERTY_LINKS = "_links";
    public static final String HAL_RESERVED_PROPERTY_EMBEDDED = "_embedded";
    public static final String OPENAPI_REF_PATH_DELIMITER = "/";

    public HALModelConverter() {
	super(Json.mapper());
    }

    public HALModelConverter(ObjectMapper mapper) {
	super(mapper);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next) {
	Schema<?> originalSchema = super.resolve(annotatedType, context, next);
	Schema<?> schema = originalSchema;

	String schemaReferenceName = "";
	if(schema.get$ref() != null) {
	    List<String> referencePathParts = Arrays.asList(schema.get$ref().split(OPENAPI_REF_PATH_DELIMITER));
	    schemaReferenceName = referencePathParts.get(referencePathParts.size()-1);
	    schema = context.getDefinedModels().get(schemaReferenceName);
	}

	Map<String, Schema> properties = schema.getProperties();
	if (properties == null) {
	    return originalSchema;
	}

	Map<String, Schema> newProperties = new HashMap<>();
	ObjectSchema linksSchema = new ObjectSchema();
	ObjectSchema embeddedSchema = new ObjectSchema();

	Type annotationType = annotatedType.getType();
	if(annotationType == null) {
	    return originalSchema;
	}

	Class<?> schemaImplementationClass;
	if (annotationType instanceof Class) {
	    schemaImplementationClass = (Class<?>) annotationType;
	} else if (annotationType instanceof JavaType) {
	    schemaImplementationClass = ((JavaType) annotationType).getRawClass();
	} else {
	    return originalSchema;
	}

	if (isHALJsonResource(schemaImplementationClass)) {
	    for (Entry<String, Schema> propertyNameAndSchema : properties.entrySet()) {
		String propertyName = propertyNameAndSchema.getKey();
		Schema propertySchema = propertyNameAndSchema.getValue();

		boolean isLink = false;
		boolean isEmbedded = false;

		Field field = getField(schemaImplementationClass, propertyName);
		isLink = field.getAnnotationsByType(Link.class).length > 0;
		isEmbedded = field.getAnnotationsByType(EmbeddedResource.class).length > 0;

		Method method = getReadMethod(schemaImplementationClass, propertyName);
		isLink = method.getAnnotationsByType(Link.class).length > 0 || isLink;
		isEmbedded = method.getAnnotationsByType(EmbeddedResource.class).length > 0 || isEmbedded;

		if (isLink) {
		    linksSchema.addProperties(propertyName, propertySchema);
		}
		if (isEmbedded) {
		    embeddedSchema.addProperties(propertyName, propertySchema);
		}
		if (!isLink && !isEmbedded) {
		    newProperties.put(propertyName, propertySchema);
		}
	    }
	    if(linksSchema.getProperties() == null || linksSchema.getProperties().size() > 0) {
		newProperties.put(HAL_RESERVED_PROPERTY_LINKS, linksSchema);
	    }
	    if(embeddedSchema.getProperties() == null || embeddedSchema.getProperties().size() > 0) {
		newProperties.put(HAL_RESERVED_PROPERTY_EMBEDDED, embeddedSchema);
	    }
	    schema.setProperties(newProperties);
	}
	if(!schemaReferenceName.isEmpty()) {
	    context.defineModel(schemaReferenceName, schema);
	    return originalSchema;
	}
	return schema;
    }

    private boolean isHALJsonResource(Class<?> typeClass) {
	return typeClass.getAnnotationsByType(Resource.class).length > 0;
    }

    private Method getReadMethod(Class<?> classContainingFields, String property) {
	try {
	    return Stream.of(Introspector.getBeanInfo(classContainingFields, Object.class).getPropertyDescriptors())
		    .filter(propertyDescriptor -> propertyDescriptor.getName().equals(property))
		    .findFirst()
		    .orElse(null)
		    .getReadMethod();
	} catch (IntrospectionException e1) {
	    return null;
	}
    }

    private Field getField(Class<?> classContainingFields, String property) {
	try {
	    return classContainingFields.getDeclaredField(property);
	} catch (NoSuchFieldException e) {
	    if (classContainingFields.getSuperclass() != null) {
		return getField(classContainingFields.getSuperclass(), property);
	    }
	}
	return null;

    }
}
