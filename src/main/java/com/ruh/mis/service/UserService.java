package com.ruh.mis.service;

import com.ruh.mis.model.AppRole;
import com.ruh.mis.model.Role;
import com.ruh.mis.model.User;
import com.ruh.mis.repository.RoleRepository;
import com.ruh.mis.repository.UserRepository;
import com.ruh.mis.security.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    public MessageResponse registerUser(String username, String email, String password, Set<String> strRoles) {
        if (userRepository.existsByUserName(username)) {
            return new MessageResponse("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            return new MessageResponse("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(username, email, encoder.encode(password));
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role defaultRole = roleRepository.findByRoleName(AppRole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Default role not found."));
            roles.add(defaultRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "ar":
                        roles.add(getRole(AppRole.ROLE_AR));
                        break;
                    case "hod":
                        roles.add(getRole(AppRole.ROLE_HOD));
                        break;
                    case "modulecoordinator":
                        roles.add(getRole(AppRole.ROLE_MODULE_COORDINATOR));
                        break;
                    case "lecturer":
                        roles.add(getRole(AppRole.ROLE_LECTURER));
                        break;
                    case "student":
                        roles.add(getRole(AppRole.ROLE_STUDENT));
                        break;
                    default:
                        throw new RuntimeException("Error: Invalid role provided.");
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return new MessageResponse("User registered successfully!");
    }

    public void registerLecturerUser(String username, String email, String password) {
        registerUser(username, email, password, Set.of("lecturer"));
    }

    public void registerStudentUser(String username, String email, String password) {
        registerUser(username, email, password, Set.of("student"));
    }

    private Role getRole(AppRole roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
    }
}
