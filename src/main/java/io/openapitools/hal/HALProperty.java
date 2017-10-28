package io.openapitools.hal;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.Xml;
import io.swagger.models.properties.Property;

/**
 * A property that is a HAL property.
 *
 * A HAL property is, i.e., a link or an embedded resource.
 * Decorating the original property.
 */
public class HALProperty implements Property {

    private final HALModelConverter.HALReservedProperty halType;
    private final Property property;
    private final String specificName;

    public HALProperty(HALModelConverter.HALReservedProperty halType, String value, Property property) {
        this.halType = halType;
        this.property = property;
        this.specificName = value;
    }

    @JsonIgnore
    public HALModelConverter.HALReservedProperty getHALType() {
        return halType;
    }

    @JsonIgnore
    public Property getProperty() {
        return property;
    }

    @JsonIgnore
    public String getSpecificName() {
        return specificName;
    }

    @Override
    public Property title(String title) {
        property.setTitle(title);
        return this;
    }

    @Override
    public Property description(String description) {
        property.setDescription(description);
        return this;
    }

    @Override
    public String getType() {
        return property.getType();
    }

    @Override
    public String getFormat() {
        return property.getFormat();
    }

    @Override
    public String getTitle() {
        return property.getTitle();
    }

    @Override
    public void setTitle(String title) {
        property.setTitle(title);
    }

    @Override
    public String getDescription() {
        return property.getDescription();
    }

    @Override
    public void setDescription(String description) {
        property.setDescription(description);
    }

    @Override
    public Boolean getAllowEmptyValue() {
        return property.getAllowEmptyValue();
    }

    @Override
    public void setAllowEmptyValue(Boolean value) {
        property.setAllowEmptyValue(value);
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public void setName(String name) {
        property.setName(name);
    }

    @Override
    public boolean getRequired() {
        return property.getRequired();
    }

    @Override
    public void setRequired(boolean required) {
        property.setRequired(required);
    }

    @Override
    public Object getExample() {
        return property.getExample();
    }

    @Override
    public void setExample(Object example) {
        property.setExample(example);
    }

    @Override
    public void setExample(String example) {
        property.setExample(example);
    }

    @Override
    public Boolean getReadOnly() {
        return property.getReadOnly();
    }

    @Override
    public void setReadOnly(Boolean readOnly) {
        property.setReadOnly(readOnly);
    }

    @Override
    public Integer getPosition() {
        return property.getPosition();
    }

    @Override
    public void setPosition(Integer position) {
        property.setPosition(position);
    }

    @Override
    public Xml getXml() {
        return property.getXml();
    }

    @Override
    public void setXml(Xml xml) {
        property.setXml(xml);
    }

    @Override
    public void setDefault(String _default) {
        property.setDefault(_default);
    }

    @Override
    public String getAccess() {
        return property.getAccess();
    }

    @Override
    public void setAccess(String access) {
        property.setAccess(access);
    }

    @Override
    public Map<String, Object> getVendorExtensions() {
        return property.getVendorExtensions();
    }

    @Override
    public Property rename(String newName) {
        return new HALProperty(halType, specificName, property.rename(newName));
    }

}
