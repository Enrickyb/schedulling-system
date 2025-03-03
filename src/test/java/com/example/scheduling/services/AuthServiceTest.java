package com.example.scheduling.services;

import com.example.scheduling.dto.AuthDTO;
import com.example.scheduling.dto.AuthResponseDTO;
import com.example.scheduling.enums.UserRole;
import com.example.scheduling.exceptions.EmailAlreadyExistsException;
import com.example.scheduling.exceptions.InvalidCredentialsException;
import com.example.scheduling.exceptions.UserNotFoundException;
import com.example.scheduling.models.User;
import com.example.scheduling.repositories.UserRepository;
import com.example.scheduling.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User user;
    private AuthDTO authDTO;
    private String token;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .passwordHash("encodedPassword")
                .role(UserRole.CUSTOMER)
                .name("Test User")
                .phone("123456789")
                .address("Test Address")
                .build();

        authDTO = new AuthDTO("Test User", "test@example.com", "encodedPassword", "123456789", "Test Address");
        token = "mockedJwtToken";
    }

    @Test
    void register_SuccessfulRegistration_ReturnsAuthResponseDTO() {
        when(userRepository.findByEmail(authDTO.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(authDTO.password())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(any(User.class))).thenReturn(token);
        when(userRepository.save(any(User.class))).thenReturn(user);

        AuthResponseDTO response = authService.register(authDTO);

        assertNotNull(response);
        assertEquals(authDTO.email(), response.user_email());
        assertEquals(authDTO.name(), response.user_name());
        assertEquals(UserRole.CUSTOMER, response.user_role());
        assertEquals(token, response.token());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        when(userRepository.findByEmail(authDTO.email())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(authDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_SuccessfulLogin_ReturnsAuthResponseDTO() {
        when(userRepository.findByEmail(authDTO.email())).thenReturn(Optional.of(user));
        doAnswer(invocation -> null).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        when(jwtUtil.generateToken(user)).thenReturn(token);

        AuthResponseDTO response = authService.login(authDTO);

        assertNotNull(response);
        assertEquals(authDTO.email(), response.user_email());
        assertEquals(authDTO.name(), response.user_name());
        assertEquals(UserRole.CUSTOMER, response.user_role());
        assertEquals(token, response.token());
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail(authDTO.email())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.login(authDTO));
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        when(userRepository.findByEmail(authDTO.email())).thenReturn(Optional.of(user));
        doThrow(new InvalidCredentialsException("E-mail ou senha inválidos!"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(authDTO));
    }
}
