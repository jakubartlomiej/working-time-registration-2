package com.jakubartlomiej.workingtimeregistration2.service;

import com.jakubartlomiej.workingtimeregistration2.entity.Role;
import com.jakubartlomiej.workingtimeregistration2.entity.User;
import com.jakubartlomiej.workingtimeregistration2.repository.RoleRepository;
import com.jakubartlomiej.workingtimeregistration2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public long count() {
        return roleRepository.count();
    }

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public List<Role> findRoleToGrantByEmployeeId(long employeeId) {
        List<Role> roles = roleRepository.findAll(Sort.by(Sort.Direction.ASC, "simplifiedRoleName"));
        if (userRepository.findById(employeeId).isPresent()) {
            User user = userRepository.findById(employeeId).get();
            for (Role role : user.getRoles()) {
                roles.remove(role);
            }
        }
        return roles;
    }
}