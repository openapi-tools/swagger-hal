# Swagger HAL Module

This module is intended to ensure correct documentation generated by Swagger when
the [Jackson HAL module](https://github.com/openapi-tools/jackson-dataformat-hal) is
being used for generating [HAL JSON](http://tools.ietf.org/html/draft-kelly-json-hal)
output.

# Status

Module is considered production ready.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.openapitools.hal/swagger-hal/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.openapitools.hal/swagger-hal/)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/io.openapitools.hal/swagger-hal/badge.svg)](https://www.javadoc.io/doc/io.openapitools.hal/swagger-hal)

# Usage

The Swagger module will be automatically discovered by Swagger when present in the classpath.

## Caveat: Using the Swagger Maven Plugin

Note: Using the [Open API Tools Swagger Maven plugin](https://github.com/openapi-tools/swagger-maven-plugin) will also solve this issue.

The Swagger Maven Plugin manipulates the extensions of Swagger and does not
call upwards in the ModelConverter chain. Therefor it is necessary to configure
the Swagger HAL Module explicitly. The following illustrates the necessary configuration.

```xml
<plugin>
    <groupId>com.github.kongchen</groupId>
    <artifactId>swagger-maven-plugin</artifactId>
    <version>${swagger-maven-plugin.version}</version>
    <dependencies>
        <dependency>
            <groupId>io.openapitools.hal</groupId>
            <artifactId>swagger-hal</artifactId>
            <version>${swagger-hal.version}</version>
        </dependency>
    </dependencies>
    <configuration>
        <apiSources>
            <apiSource>
                <!-- add the model converter -->
                <modelConverters>
                    <modelConverter>io.openapi.tools.swagger.HALModelConverter</modelConverter>
                </modelConverters>
            </apiSource>
        </apiSources>
    </configuration>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

