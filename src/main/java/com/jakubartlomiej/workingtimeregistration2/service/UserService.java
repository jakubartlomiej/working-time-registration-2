package com.jakubartlomiej.workingtimeregistration2.service;

import com.jakubartlomiej.workingtimeregistration2.entity.Role;
import com.jakubartlomiej.workingtimeregistration2.entity.User;
import com.jakubartlomiej.workingtimeregistration2.repository.RoleRepository;
import com.jakubartlomiej.workingtimeregistration2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void update(User user) {
        userRepository.updateWithoutPasswordAndRoles(user.getId(),
                user.getLogin(),
                user.getCardNumber(),
                user.getLastName(),
                user.getFirstName());
    }

    public long count() {
        return userRepository.count();
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public void addRole(Role role, long employeeId) {
        if (userRepository.findById(employeeId).isPresent()) {
            User user = userRepository.findById(employeeId).get();
            if (roleRepository.findById(role.getId()).isPresent()) {
                user.getRoles().add(roleRepository.findById(role.getId()).get());
                userRepository.save(user);
            }
        }
    }

    public void deleteRole(Role role, long employeeId) {
        if (userRepository.findById(employeeId).isPresent()) {
            User user = userRepository.findById(employeeId).get();
            if (roleRepository.findById(role.getId()).isPresent()) {
                user.getRoles().remove(roleRepository.findById(role.getId()).get());
                userRepository.save(user);
            }
        }
    }

    public void deleteAllRole(long userId) {
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).get();
            while (user.getRoles().size() > 0) {
                user.getRoles().remove(0);
            }
            userRepository.save(user);
        }
    }

    public void addLogin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.addLogin(user.getLogin(), user.getPassword(), user.getId());
        addRole(user.getRoles().get(0), user.getId());
    }

    public void deleteOnlyUserById(long id) {
        userRepository.deleteOnlyUserById(id);
    }
}