package io.openapitools.hal;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.openapitools.hal.example.AccountServiceExposure;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

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

	Assertions.assertTrue(openApi.getComponents().getSchemas().containsKey(resourceName));

        Schema<?> accountRepresentation = openApi.getComponents().getSchemas().get(resourceName);
        Assertions.assertTrue(accountRepresentation.getProperties().containsKey(HALModelConverter.HAL_RESERVED_PROPERTY_LINKS));
        Assertions.assertTrue(accountRepresentation.getProperties().containsKey(HALModelConverter.HAL_RESERVED_PROPERTY_EMBEDDED));
        Assertions.assertEquals(5, accountRepresentation.getProperties().size());

        Schema<?> transactions = (Schema<?>) accountRepresentation.getProperties()
        	.get(HALModelConverter.HAL_RESERVED_PROPERTY_EMBEDDED)
        	.getProperties()
        	.get("transactions");
        Assertions.assertEquals("Embeds the latest transaction of account.", transactions.getDescription());
    }

    @Test
    public void testGenerationAccountsRepresentation() {
	OpenAPI openApi = openApiContext.read();
	String resourceName = "AccountsRepresentation";

	Assertions.assertTrue(openApi.getComponents().getSchemas().containsKey(resourceName));

        Schema<?> accountsRepresentation = openApi.getComponents().getSchemas().get(resourceName);
        Assertions.assertTrue(accountsRepresentation.getProperties().containsKey(HALModelConverter.HAL_RESERVED_PROPERTY_LINKS));
        Assertions.assertTrue(accountsRepresentation.getProperties().containsKey(HALModelConverter.HAL_RESERVED_PROPERTY_EMBEDDED));
        Assertions.assertEquals(3, accountsRepresentation.getProperties().size());

        Schema<?> self = (Schema<?>) accountsRepresentation.getProperties()
        	.get(HALModelConverter.HAL_RESERVED_PROPERTY_LINKS)
        	.getProperties()
        	.get("self");
        Assertions.assertEquals("#/components/schemas/HALLink", self.get$ref());

        Schema<?> accounts = (Schema<?>) accountsRepresentation.getProperties()
        	.get(HALModelConverter.HAL_RESERVED_PROPERTY_EMBEDDED)
        	.getProperties()
        	.get("accounts");
        Assertions.assertEquals("array", accounts.getType());
    }

    @Test
    public void testGenerationUsesNameFromAnnotation() {
	OpenAPI openApi = openApiContext.read();
	String resourceName = "AccountRepresentation";

	Assertions.assertTrue(openApi.getComponents().getSchemas().containsKey(resourceName));

        Schema<?> accountRepresentation = openApi.getComponents().getSchemas().get(resourceName);
        Assertions.assertTrue(accountRepresentation.getProperties().containsKey(HALModelConverter.HAL_RESERVED_PROPERTY_LINKS));
        Assertions.assertTrue(accountRepresentation.getProperties().containsKey(HALModelConverter.HAL_RESERVED_PROPERTY_EMBEDDED));
        Assertions.assertEquals(5, accountRepresentation.getProperties().size());

        Assertions.assertTrue(accountRepresentation
        	.getProperties()
        	.get(HALModelConverter.HAL_RESERVED_PROPERTY_LINKS)
        	.getProperties()
        	.containsKey("account:transactions"));
    }

}
