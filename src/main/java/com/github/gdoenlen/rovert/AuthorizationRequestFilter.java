package com.github.gdoenlen.rovert;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Request filter that will check if the request is authorized against any endpoint.
 * 
 * Slack will include two headers in the request:
 *  X-Slack-Request-Timestamp
 *  X-Slack-Signature
 * 
 * Both of these headers must be present in the request to calculate whether the
 * request is authentic or not. This is done by concating the version, timestamp,
 * and body of the request with colons (":") and then using an HmacSHA256 hash
 * with the shared secret key and checking if it matches the supplied signature header.
 * 
 * @see https://api.slack.com/docs/verifying-requests-from-slack
 */
@Provider
@ApplicationScoped
public class AuthorizationRequestFilter implements ContainerRequestFilter {

    public static final String SLACK_HEADER_REQUEST_TIMESTAMP = "X-Slack-Request-Timestamp";
    public static final String SLACK_HEADER_SIGNATURE = "X-Slack-Signature";
    public static final String SIGNING_ALGORITHM = "HmacSHA256";

    /** Version of the slack api. Currently the version is always 0. */
    public static final String SLACK_API_VERSION = "v0";

    private static final Response UNAUTHORIZED = Response.status(Status.UNAUTHORIZED).build();

    /** Determines if this filter is enabled */
    private final boolean enabled;

    /** Mac used for signing and checking the signature of each request. */
    private final Mac mac;

    /**
     * @param enabled Determines if this filter is enabled or not. False will allow all requests to come through.
     *                This value should be provided in application.properties -> rovert.authorization.enabled
     * @param secret The shared secret signing key from slack.
     *               This value should be provided in application.properties -> slack.signing-secret
     */
    public AuthorizationRequestFilter(
        @ConfigProperty(name = "rovert.authorization.enabled") boolean enabled, 
        @ConfigProperty(name = "slack.signing-secret") String secret
    ) {
        Objects.requireNonNull(secret);
        
        this.enabled = enabled;
        try {
            this.mac = Mac.getInstance(SIGNING_ALGORITHM);
            var secretKey = new SecretKeySpec(secret.getBytes(), SIGNING_ALGORITHM);
            this.mac.init(secretKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!enabled) {
            return;
        }

        var timestamp = requestContext.getHeaderString(SLACK_HEADER_REQUEST_TIMESTAMP);
        var signature = requestContext.getHeaderString(SLACK_HEADER_SIGNATURE);
        if (timestamp == null || signature == null) {
            requestContext.abortWith(UNAUTHORIZED);
            return;
        }

        var body = IOUtils.toString(requestContext.getEntityStream(), Charset.defaultCharset());    
        var joined = String.join(":", SLACK_API_VERSION, timestamp, body);
        var hash = Base64.getEncoder().encodeToString(mac.doFinal(joined.getBytes()));
        
        if (!signature.equals(SLACK_API_VERSION + "=" + hash)) {
            requestContext.abortWith(UNAUTHORIZED);
        }
    }
}
