package com.github.gdoenlen.rovert;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.container.ContainerRequestContext;

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
        var mac = Mac.getInstance(AuthorizationRequestFilter.SIGNING_ALGORITHM);
        var key = new SecretKeySpec(secret.getBytes(), AuthorizationRequestFilter.SIGNING_ALGORITHM);
        mac.init(key);

        var timestamp = "timestamp";
        var body = "body";
        var joined = String.join(":", AuthorizationRequestFilter.SLACK_API_VERSION, timestamp, body);
        byte[] hashBytes = mac.doFinal(joined.getBytes());
        var hash = Base64.getEncoder().encodeToString(hashBytes); 
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
        var mac = Mac.getInstance(AuthorizationRequestFilter.SIGNING_ALGORITHM);
        var key = new SecretKeySpec(secret.getBytes(), AuthorizationRequestFilter.SIGNING_ALGORITHM);
        mac.init(key);

        var timestamp = "timestamp";
        var body = "body";
        var joined = String.join(":", AuthorizationRequestFilter.SLACK_API_VERSION, timestamp, body);
        byte[] hashBytes = mac.doFinal(joined.getBytes());
        var hash = Base64.getEncoder().encodeToString(hashBytes); 
        var signature = "this_will_not_match";
        var context = Mockito.mock(ContainerRequestContext.class);
        when(context.getHeaderString(AuthorizationRequestFilter.SLACK_HEADER_REQUEST_TIMESTAMP)).thenReturn(timestamp);
        when(context.getHeaderString(AuthorizationRequestFilter.SLACK_HEADER_SIGNATURE)).thenReturn(signature);
        when(context.getEntityStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));

        filter.filter(context);
        verify(context).abortWith(any());
    }
}