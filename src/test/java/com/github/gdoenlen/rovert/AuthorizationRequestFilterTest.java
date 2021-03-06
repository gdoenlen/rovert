package com.github.gdoenlen.rovert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AuthorizationRequestFilterTest {

    @Test
    void it_should_abort_if_slack_headers_are_not_present() throws Exception {
        var filter = new AuthorizationRequestFilter(true, "doesn'tmatter");
        var context = Mockito.mock(ContainerRequestContext.class);
        when(context.getHeaderString(anyString())).thenReturn(null);

        filter.filter(context);
        verify(context).abortWith(any());
    }
    
    @Test
    void it_shouldnt_abort_if_the_signature_hashes_match() throws Exception {
        var secret = "secret";
        var filter = new AuthorizationRequestFilter(true, secret);
        var mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, secret.getBytes());

        var timestamp = "timestamp";
        var body = "body";
        var joined = String.join(":", AuthorizationRequestFilter.SLACK_API_VERSION, timestamp, body);
        byte[] hashBytes = mac.doFinal(joined.getBytes());
        var hash = Hex.encodeHexString(hashBytes); 
        var signature = AuthorizationRequestFilter.SLACK_API_VERSION + "=" + hash;
        var context = Mockito.mock(ContainerRequestContext.class);
        when(context.getHeaderString(AuthorizationRequestFilter.SLACK_HEADER_REQUEST_TIMESTAMP)).thenReturn(timestamp);
        when(context.getHeaderString(AuthorizationRequestFilter.SLACK_HEADER_SIGNATURE)).thenReturn(signature);
        when(context.getEntityStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));

        filter.filter(context);
        verify(context, never()).abortWith(any());
    }

    @Test
    void it_should_abort_if_the_signature_hashes_dont_match() throws Exception {
        var secret = "secret";
        var filter = new AuthorizationRequestFilter(true, secret);

        var timestamp = "timestamp";
        var body = "body";
        var signature = "this_will_not_match";
        var context = Mockito.mock(ContainerRequestContext.class);
        when(context.getHeaderString(AuthorizationRequestFilter.SLACK_HEADER_REQUEST_TIMESTAMP)).thenReturn(timestamp);
        when(context.getHeaderString(AuthorizationRequestFilter.SLACK_HEADER_SIGNATURE)).thenReturn(signature);
        when(context.getEntityStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));

        filter.filter(context);
        verify(context).abortWith(any());
    }
}