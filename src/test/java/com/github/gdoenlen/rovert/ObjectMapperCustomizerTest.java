package com.github.gdoenlen.rovert;

import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ObjectMapperCustomizerTest {
    @Test
    void it_should_disable_fail_on_unknown_properties() {
        var customizer = new ObjectMapperCustomizer();
        var mapper = Mockito.mock(ObjectMapper.class);

        customizer.customize(mapper);
        verify(mapper).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}