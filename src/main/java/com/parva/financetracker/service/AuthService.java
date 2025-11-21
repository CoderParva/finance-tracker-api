package com.parva.financetracker.service;

import com.parva.financetracker.dto.AuthResponse;
import com.parva.financetracker.dto.LoginRequest;
import com.parva.financetracker.dto.RegisterRequest;
import com.parva.financetracker.exception.ResourceAlreadyExistsException;
import com.parva.financetracker.model.User;
import com.parva.financetracker.repository.UserRepository;
import com.parva.financetracker.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    public AuthResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        
        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        
        userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }
    
    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        // Get user from database
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }
}