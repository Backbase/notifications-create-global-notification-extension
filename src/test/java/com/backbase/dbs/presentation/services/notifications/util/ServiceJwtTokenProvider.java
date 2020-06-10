package com.backbase.dbs.presentation.services.notifications.util;

import com.backbase.buildingblocks.jwt.core.JsonWebTokenProducerType;
import com.backbase.buildingblocks.jwt.core.exception.JsonWebTokenException;
import com.backbase.buildingblocks.jwt.core.properties.JsonWebTokenProperties;
import com.backbase.buildingblocks.jwt.core.properties.Signature;
import com.backbase.buildingblocks.jwt.core.properties.TokenKey;
import com.backbase.buildingblocks.jwt.core.properties.TokenKeyType;
import com.backbase.buildingblocks.jwt.core.token.JsonWebTokenClaimsSet;
import com.backbase.buildingblocks.jwt.core.type.JsonWebTokenTypeFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ServiceJwtTokenProvider {

    private static final String BBUSER = "bbuser";


    private ServiceJwtTokenProvider() {
    }

    public static String createServiceToken() throws JsonWebTokenException {
        return createServiceToken(BBUSER);
    }

    private static String createServiceToken(String subClaim) throws JsonWebTokenException {
        JsonWebTokenProducerType<JsonWebTokenClaimsSet, String> producer = createJsonWebTokenProducerType();
        return "Bearer " + producer.createToken(createServiceClaimsSet(subClaim));
    }

    private static JsonWebTokenProducerType<JsonWebTokenClaimsSet, String> createJsonWebTokenProducerType()
        throws JsonWebTokenException {
        JsonWebTokenProperties properties = createJsonWebTokenProperties();

        return JsonWebTokenTypeFactory.getProducer(properties);
    }

    private static JsonWebTokenProperties createJsonWebTokenProperties() {
        JsonWebTokenProperties properties = new JsonWebTokenProperties() {

            @Override
            public String getType() {
                return JsonWebTokenTypeFactory.SIGNED_TOKEN_TYPE;
            }
        };

        Signature signature = new Signature();
        TokenKey tokenKey = new TokenKey();
        tokenKey.setType(TokenKeyType.ENV);
        tokenKey.setValue("SIG_SECRET_KEY");
        tokenKey.setId("");
        signature.setKey(tokenKey);
        properties.setSignature(signature);
        return properties;
    }

    private static JsonWebTokenClaimsSet createServiceClaimsSet(String subClaim) {

        final Map<String, Object> claimMap = new HashMap<>();

        claimMap.put("scope", new String[]{"api:service"});
        claimMap.put("sub", subClaim);
        claimMap.put("exp", 214741283647L);
        claimMap.put("iat", 1484820196L);
        claimMap.put("said", "qwerty");

        return new JsonWebTokenClaimsSet() {

            @Override
            public Map<String, Object> getClaims() {
                return claimMap;
            }

            @Override
            public Optional<Object> getClaim(String claimName) {
                return Optional.ofNullable(claimMap.get(claimName));
            }
        };
    }

}
