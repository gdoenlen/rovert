package com.github.gdoenlen.rovert;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Customizers for the object mapper
 */
public class ObjectMapperCustomizer implements io.quarkus.jackson.ObjectMapperCustomizer {

    @Override
    public void customize(ObjectMapper mapper) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
