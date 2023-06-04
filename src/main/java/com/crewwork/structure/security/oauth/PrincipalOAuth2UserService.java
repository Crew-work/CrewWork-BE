package com.crewwork.structure.security.oauth;

import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(
                userRequest.getClientRegistration().getRegistrationId(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(),
                oAuth2User.getAttributes());

        Member member = memberRepository.findByUsername(oAuth2Attribute.getUsername()).orElseGet(() ->
                memberRepository.save(Member.builder()
                        .provider(oAuth2Attribute.getProvider())
                        .providerId(oAuth2Attribute.getProviderId())
                        .username(oAuth2Attribute.getUsername())
                        .password(passwordEncoder.encode(oAuth2Attribute.getPassword()))
                        .contact(oAuth2Attribute.getEmail())
                        .picture(oAuth2Attribute.getPicture())
                        .role(oAuth2Attribute.getRole())
                        .build())
        );

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
