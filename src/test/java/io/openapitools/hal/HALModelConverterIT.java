package io.openapitools.hal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.openapitools.hal.example.AccountServiceExposure;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Collections;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HALModelConverterIT {

    private static OpenApiContext openApiContext;
    private static OpenAPIConfiguration openApiConfiguration;

    @SuppressWarnings("rawtypes")
    @BeforeAll
    public static void configureSwagger() throws OpenApiConfigurationException {
        openApiConfiguration = new SwaggerConfiguration()
            .resourcePackages(Collections.singleton(AccountServiceExposure.class.getPackage().getName()))
            .modelConverterClasses(Collections.singleton(HALModelConverter.class.getName()));
        openApiContext = new JaxrsOpenApiContextBuilder()
            .openApiConfiguration(openApiConfiguration)
            .buildContext(true);
    }

    @Test
    public void testGenerationAccountRepresentation() {
        OpenAPI openApi = openApiContext.read();
        String resourceName = "AccountRepresentation";

        assertTrue(openApi.getComponents().getSchemas().containsKey(resourceName));

        Schema<?> accountRepresentation = openApi.getComponents().getSchemas().get(resourceName);
        assertTrue(accountRepresentation.getProperties().containsKey(HALAnnotation.LINKS.getName()));
        assertTrue(accountRepresentation.getProperties().containsKey(HALAnnotation.EMBEDDED.getName()));
        assertEquals(5, accountRepresentation.getProperties().size());

        Schema<?> transactions = (Schema<?>) accountRepresentation.getProperties()
            .get(HALAnnotation.EMBEDDED.getName())
            .getProperties()
            .get("transactions");
        assertEquals("Embeds the latest transaction of account.", transactions.getDescription());

        assertTrue(accountRepresentation.getProperties()
            .get(HALAnnotation.LINKS.getName())
            .getProperties()
            .containsKey("http://docs.my.site/transactions"));
    }

    @Test
    public void testGenerationAccountsRepresentation() {
        OpenAPI openApi = openApiContext.read();
        String resourceName = "AccountsRepresentation";

        assertTrue(openApi.getComponents().getSchemas().containsKey(resourceName));

        Schema<?> accountsRepresentation = openApi.getComponents().getSchemas().get(resourceName);
        assertTrue(accountsRepresentation.getProperties().containsKey(HALAnnotation.LINKS.getName()));
        assertTrue(accountsRepresentation.getProperties().containsKey(HALAnnotation.EMBEDDED.getName()));
        assertEquals(3, accountsRepresentation.getProperties().size());

        Schema<?> self = (Schema<?>) accountsRepresentation.getProperties()
            .get(HALAnnotation.LINKS.getName())
            .getProperties()
            .get("self");
        assertEquals("#/components/schemas/HALLink", self.get$ref());

        Schema<?> accounts = (Schema<?>) accountsRepresentation.getProperties()
            .get(HALAnnotation.EMBEDDED.getName())
            .getProperties()
            .get("accounts");
        assertEquals("array", accounts.getType());
    }

    @Test
    public void testGenerationUsesNameFromAnnotation() {
        OpenAPI openApi = openApiContext.read();
        String resourceName = "AccountRepresentation";

        assertTrue(openApi.getComponents().getSchemas().containsKey(resourceName));

        Schema<?> accountRepresentation = openApi.getComponents().getSchemas().get(resourceName);
        assertTrue(accountRepresentation.getProperties().containsKey(HALAnnotation.LINKS.getName()));
        assertTrue(accountRepresentation.getProperties().containsKey(HALAnnotation.EMBEDDED.getName()));
        assertEquals(5, accountRepresentation.getProperties().size());

        assertTrue(accountRepresentation
            .getProperties()
            .get(HALAnnotation.LINKS.getName())
            .getProperties()
            .containsKey("http://docs.my.site/transactions"));
    }

}
