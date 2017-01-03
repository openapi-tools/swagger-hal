package dk.nykredit.swagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import dk.nykredit.swagger.example.AccountServiceExposure;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.ReflectiveJaxrsScanner;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import org.junit.Test;

public class HALModelConverterIT {

    @Test
    public void testGeneration() {
        Swagger swagger = new Swagger();
        ReflectiveJaxrsScanner reflectiveScanner = new ReflectiveJaxrsScanner();
        reflectiveScanner.setResourcePackage(AccountServiceExposure.class.getPackage().getName());
        Reader reader = new Reader(swagger);
        swagger = reader.read(reflectiveScanner.classes());

        assertTrue(swagger.getDefinitions().containsKey("AccountRepresentation"));

        Model accountRepresentation = swagger.getDefinitions().get("AccountRepresentation");
        assertTrue(accountRepresentation.getProperties().containsKey("_links"));
        assertTrue(accountRepresentation.getProperties().containsKey("_embedded"));
        assertEquals(5, accountRepresentation.getProperties().size());

        Property transactions = ((ObjectProperty) accountRepresentation.getProperties().get("_embedded")).getProperties().get("transactions");
        assertEquals("Embeds the latest transaction of account.", transactions.getDescription());
    }

}
