package com.crewwork.structure.security.oauth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class OAuth2Attribute {

    private String provider;
    private String providerId;
    private Map<String, Object> attributes;

    private String username;
    private String password;
    private String email;
    private String picture;
    private String role;

    public static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        switch (provider){
            case "github":
                return ofGitHub(provider, attributeKey, attributes);
            case "google":
                return ofGoogle(provider, attributeKey, attributes);
            default:
                throw new RuntimeException("OAuth2 povider를 찾을 수 없습니다");
        }
    }

    private static OAuth2Attribute ofGitHub(String provider, String attributeKey, Map<String, Object> attributes) {
        String providerId = String.valueOf(attributes.get(attributeKey));

        return OAuth2Attribute.builder()
                .provider(provider)
                .providerId(providerId)
                .username(provider + "_" + providerId)
                .password("password1232421412")
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("avatar_url"))
                .role("ROLE_USER")
                .build();
    }

    private static OAuth2Attribute ofGoogle(String provider, String attributeKey, Map<String, Object> attributes) {
        String providerId = String.valueOf(attributes.get(attributeKey));

        return OAuth2Attribute.builder()
                .provider(provider)
                .providerId(providerId)
                .username(provider + "_" + providerId)
                .password("password1232421412")
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .role("ROLE_USER")
                .build();
    }

}
