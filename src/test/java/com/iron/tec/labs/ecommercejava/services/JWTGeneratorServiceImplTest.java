package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTGeneratorServiceImplTest {

    @Mock
    JwtEncoder encoder;

    @InjectMocks
    JWTGeneratorServiceImpl jwtGeneratorService;

    String jwtExample = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYWxlamFuZHJvaGllcnJvIiwiZXhwIjoxNjczMDI5MzQ3LCJpYXQiOjE2NzMwMjU3NDcsInNjb3BlIjoiQURNSU4ifQ.szxvGqnDu2UekZqiIgbNyXhpnufYUWGrxcWNEcPtsCl4JL3-0Q6GA64iZkweeDk0bBPETlR7dWp5lbfx6jTA5ftH6A55ioNWtP5c5Zxxna0kAF-iV7zFMkWBdcUnfyS92Et6YimtfUm3uq5YW_-xQk1qRZVf2f41F8UdTFXjMKvL5OdfnWmhKHas2h1iW0DgBNBM45mrHeEcAxg64if4fRfdHWA-Xtow6hAPL7WdpDS64vmPzmZhpxi_32dLsmZuQPimCIlGKuv5sic5VzBaH8Jkp8h4PLeZDHrh9CBD4fCTauuyiRRPaunQGTzyyt2pbmJUisAJ4Nlv08sPAXt-Lg";

    @Test
    void testGenerateToken() {
        Authentication authentication = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(encoder.encode(any())).thenReturn(jwt);
        when(jwt.getTokenValue()).thenReturn(jwtExample);
        when(authentication.getPrincipal()).thenReturn(AppUser.builder().id(UUID.randomUUID()).build());

        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getAuthorities()).thenAnswer(x -> authorities);

        String token = jwtGeneratorService.generateToken(authentication);
        assertEquals(jwtExample, token);
    }

}
