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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(AuthDTO request) {
        // Verifica se o e-mail já está cadastrado
        Optional<User> existingUser = userRepository.findByEmail(request.email());
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Este e-mail já está em uso.");
        }

        // Cria e salva o novo usuário
        User newUser = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password())) // 🔹 Salva a senha criptografada
                .role(UserRole.CUSTOMER) // 🔹 Usuário padrão é CUSTOMER
                .phone(request.phone())
                .name(request.name())
                .address(request.address())
                .build();


        userRepository.save(newUser);
        // Gera o token JWT
        String token = jwtUtil.generateToken(newUser);

        return new AuthResponseDTO(token, newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getRole(), newUser.getAddress(), newUser.getPhone());
    }

    public AuthResponseDTO login(AuthDTO request) {
        try{


            // Busca o usuário no banco
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado!"));


            // Autentica o usuário no Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );


            // Gera o token JWT
            String token = jwtUtil.generateToken(user);

            return new AuthResponseDTO(token, user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getAddress(), user.getPhone());

        }
        catch (Exception e){
            throw new InvalidCredentialsException("E-mail ou senha inválidos!");
        }

    }
}
