package com.ruh.mis.security;

import com.ruh.mis.model.Role;
import com.ruh.mis.model.AppRole;
import com.ruh.mis.model.User;
import com.ruh.mis.repository.RoleRepository;
import com.ruh.mis.repository.UserRepository;
import com.ruh.mis.security.jwt.AuthEntryPointJwt;
import com.ruh.mis.security.jwt.AuthTokenFilter;
import com.ruh.mis.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }


    
    @Bean
    @org.springframework.core.annotation.Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        // Configure the security of the application by specifying
        // which requests are allowed or denied
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(
                "/public/transcripts/**",
                "/api/transcripts/**",
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles

            Role arRole = roleRepository.findByRoleName(AppRole.ROLE_AR)
                    .orElseGet(() -> {
                        Role newArRole = new Role(AppRole.ROLE_AR);
                        return roleRepository.save(newArRole);
                    });


            Role hodRole = roleRepository.findByRoleName(AppRole.ROLE_HOD)
                    .orElseGet(() -> {
                        Role newHodRole = new Role(AppRole.ROLE_HOD);
                        return roleRepository.save(newHodRole);
                    });

            Role modulecoordinatorRole = roleRepository.findByRoleName(AppRole.ROLE_MODULE_COORDINATOR)
                    .orElseGet(() -> {
                        Role newModuleCoordinatoeRole = new Role(AppRole.ROLE_MODULE_COORDINATOR);
                        return roleRepository.save(newModuleCoordinatoeRole);
                    });



            Role lecturerRole = roleRepository.findByRoleName(AppRole.ROLE_LECTURER)
                    .orElseGet(() -> {
                        Role newLecturerRole = new Role(AppRole.ROLE_LECTURER);
                        return roleRepository.save(newLecturerRole);
                    });

            Role studentRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseGet(() -> {
                        Role newStudentRole = new Role(AppRole.ROLE_STUDENT);
                        return roleRepository.save(newStudentRole);
                    });


            Set<Role> arRoles = Set.of(arRole, hodRole, modulecoordinatorRole, lecturerRole, studentRole);
            Set<Role> hodRoles = Set.of(hodRole, modulecoordinatorRole, lecturerRole, studentRole);
            Set<Role> modulecoordinatorRoles = Set.of(modulecoordinatorRole, lecturerRole, studentRole);
            Set<Role> lecturerRoles = Set.of(lecturerRole, studentRole);
            Set<Role> studentRoles = Set.of(studentRole);


            // Create users if not already present
            if (!userRepository.existsByUserName("ar1")) {
                User ar1 = new User("ar1", "ar1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(ar1);
            }

//            if (!userRepository.existsByUserName("receptionist1")) {
//                User receptionist1 = new User("receptionist1", "receptionist1@example.com", passwordEncoder.encode("password2"));
//                userRepository.save(receptionist1);
//            }
//
//            if (!userRepository.existsByUserName("admin")) {
//                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
//                userRepository.save(admin);
//            }

            // Update roles for existing users
            userRepository.findByUserName("ar1").ifPresent(ar -> {
                ar.setRoles(arRoles);
                userRepository.save(ar);
            });

//            userRepository.findByUserName("receptionist1").ifPresent(receptionist -> {
//                receptionist.setRoles(receptionistRoles);
//                userRepository.save(receptionist);
//            });
//
//            userRepository.findByUserName("admin").ifPresent(admin -> {
//                admin.setRoles(adminRoles);
//                userRepository.save(admin);
//            });
        };
    }
}
