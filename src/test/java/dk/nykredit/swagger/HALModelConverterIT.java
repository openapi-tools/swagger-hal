package dk.nykredit.swagger;

import dk.nykredit.swagger.example.AccountServiceExposure;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.ReflectiveJaxrsScanner;
import io.swagger.models.Swagger;
import io.swagger.util.Yaml;
import org.junit.Test;

public class HALModelConverterIT {

    @Test
    public void testGeneration() {
        Swagger swagger = new Swagger();
        ReflectiveJaxrsScanner reflectiveScanner = new ReflectiveJaxrsScanner();
        reflectiveScanner.setResourcePackage(AccountServiceExposure.class.getPackage().getName());
        Reader reader = new Reader(swagger);
        swagger = reader.read(reflectiveScanner.classes());
        System.out.println(swagger);
        Yaml.prettyPrint(swagger);
        
    }
    
}
