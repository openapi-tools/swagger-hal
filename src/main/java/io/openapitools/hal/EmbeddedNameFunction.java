package io.openapitools.hal;

import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Bi-function generating property name for embedded resource.
 */
public class EmbeddedNameFunction implements BiFunction<HALResourceType, EmbeddedResource, Optional<String>> {

    @Override
    public Optional<String> apply(HALResourceType parent, EmbeddedResource embedded) {
        if (embedded.value().isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(embedded.value());
        }
    }
}
