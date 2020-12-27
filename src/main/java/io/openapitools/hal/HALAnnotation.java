package io.openapitools.hal;

import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Enumeration of properties reserved for HAL along with the association to the annotation marking objects to go into these properties.
 */
public class HALAnnotation<T extends Annotation> {

    public static final HALAnnotation<Link> LINKS = new HALAnnotation<>("_links", Link.class, new LinkNameFunction());
    public static final HALAnnotation<EmbeddedResource> EMBEDDED = new HALAnnotation<>("_embedded", EmbeddedResource.class,
        new EmbeddedNameFunction());

    private final String name;
    private final Class<T> annotation;
    private final BiFunction<HALResourceType, T, Optional<String>> nameFunction;

    private HALAnnotation(String name, Class<T> annotation, BiFunction<HALResourceType, T, Optional<String>> nameFunction) {
        this.name = name;
        this.annotation = annotation;
        this.nameFunction = nameFunction;
    }

    public String getName() {
        return name;
    }

    public Class<T> getAnnotation() {
        return annotation;
    }

    /**
     * Derive name for property defines as link or embedded.
     */
    public String deriveName(HALResourceType parent, T annotation, String property) {
        Optional<String> name = nameFunction.apply(parent, annotation);
        return name.orElse(property);
    }
}
