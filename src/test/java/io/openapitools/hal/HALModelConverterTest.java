package io.openapitools.hal;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Collections;
import java.util.Iterator;
import org.junit.jupiter.api.Test;

class HALModelConverterTest {

  final HALModelConverter halModelConverter = new HALModelConverter();

  @Test
  void shouldNotFailIfOriginalSchemaResolvesToNull() {
    AnnotatedType annotatedType = mock(AnnotatedType.class);
    ModelConverterContext modelConverterContext = mock(ModelConverterContext.class);
    ModelConverter mockModelConverter = mock(ModelConverter.class);

    Iterator<ModelConverter> converterChain = Collections.singleton(mockModelConverter).iterator();
    Schema result = halModelConverter.resolve(annotatedType, modelConverterContext, converterChain);
    assertNull(result);
  }
}
