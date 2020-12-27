package io.openapitools.hal;

import io.openapitools.jackson.dataformat.hal.annotation.Curies;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.deser.CurieMap;
import io.openapitools.jackson.dataformat.hal.deser.CurieMap.Mapping;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Bi-function to generate property name for link (using curies annotations if specified).
 */
public class LinkNameFunction implements BiFunction<HALResourceType, Link, Optional<String>> {

    @Override
    public Optional<String> apply(HALResourceType parent, Link link) {
        if (link.value().isEmpty()) {
            return Optional.empty();
        }

        if (link.curie().isEmpty()) {
            return Optional.of(link.value());
        }

        Optional<Curies> curies = parent.getAnnotation(Curies.class);
        if (curies.isPresent()) {
            CurieMap map = new CurieMap(Arrays.stream(curies.get().value())
                .map(Mapping::new)
                .toArray(Mapping[]::new));
            return map
                .resolve(link.curie() + ":" + link.value())
                .map(URI::toString);
        }

        return Optional.empty();
    }
}
