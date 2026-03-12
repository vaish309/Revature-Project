package com.revhire.revhire.service;

import com.revhire.revhire.dto.*;
import com.revhire.revhire.entity.Employer;
import com.revhire.revhire.entity.User;
import com.revhire.revhire.exception.BadRequestException;
import com.revhire.revhire.repository.EmployerRepository;
import com.revhire.revhire.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public LoginResponse registerJobSeeker(RegisterJobSeekerRequest request) {
        log.info("Registering job seeker: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .location(request.getLocation())
                .currentEmploymentStatus(request.getCurrentEmploymentStatus())
                .role(User.Role.JOB_SEEKER)
                .enabled(true)
                .build();

        user = userRepository.save(user);
        String token = jwtService.generateToken(user);

        log.info("Job seeker registered successfully: {}", user.getEmail());
        
        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    @Transactional
    public LoginResponse registerEmployer(RegisterEmployerRequest request) {
        log.info("Registering employer: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.EMPLOYER)
                .enabled(true)
                .build();

        user = userRepository.save(user);

        Employer employer = Employer.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .industry(request.getIndustry())
                .description(request.getDescription())
                .website(request.getWebsite())
                .location(request.getLocation())
                .companySize(request.getCompanySize())
                .build();

        employerRepository.save(employer);

        String token = jwtService.generateToken(user);

        log.info("Employer registered successfully: {}", user.getEmail());
        
        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for: {}", request.getEmail());
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        String token = jwtService.generateToken(user);

        log.info("User logged in successfully: {}", user.getEmail());
        
        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }
}
