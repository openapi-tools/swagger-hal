package io.openapitools.hal;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.util.Annotations;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

/**
 * Wraps a HAL resource class.
 */
public class HALResourceType {

    private final BeanDescription description;

    public HALResourceType(BeanDescription description) {
        this.description = description;
    }

    public boolean isHALResource() {
        return description.getClassAnnotations().has(Resource.class);
    }

    /**
     * Retrieve annotation from class
     */
    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotationClass) {
        Annotations annotations = description.getClassAnnotations();
        if (annotations.has(annotationClass)) {
            return Optional.of(annotations.get(annotationClass));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retrieve annotation from specific property (field or getter method)
     */
    public <T extends Annotation> Optional<T> getAnnotation(Class<T> annotationClass, String property) {
        Optional<T> annotation = description.findProperties().stream()
            .filter(p -> property.equals(p.getName()))
            .map(p -> p.getField().getAnnotation(annotationClass))
            .filter(Objects::nonNull)
            .findAny();
        if (annotation.isPresent()) {
            return annotation;
        }
        return description.findProperties().stream()
            .filter(p -> property.equals(p.getName()))
            .map(p -> p.getGetter().getAnnotation(annotationClass))
            .filter(Objects::nonNull)
            .findAny();
    }
}
