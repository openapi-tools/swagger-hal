# Swagger HAL Module

This module is intended to ensure correct documentation generated by Swagger when
the [Jackson HAL module](https://github.com/Nykredit/jackson-dataformat-hal) is
being used for generating [HAL JSON](http://tools.ietf.org/html/draft-kelly-json-hal)
output.

# Status

Module is considered production ready.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dk.nykredit.swagger/swagger-hal/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dk.nykredit.swagger/swagger-hal/)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/dk.nykredit.swagger/swagger-hal/badge.svg)](https://www.javadoc.io/doc/dk.nykredit.swagger/swagger-hal)

# Usage

The Swagger module will be automatically discovered by Swagger when present in the classpath.

## Using the Swagger Maven Plugin

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
            <groupId>dk.nykredit.swagger</groupId>
            <artifactId>swagger-hal</artifactId>
            <version>${swagger-hal.version}</version>
        </dependency>
    </dependencies>
    <configuration>
        <apiSources>
            <apiSource>
                <!-- add the model converter -->
                <modelConverters>
                    <modelConverter>dk.nykredit.swagger.HALModelConverter</modelConverter>
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