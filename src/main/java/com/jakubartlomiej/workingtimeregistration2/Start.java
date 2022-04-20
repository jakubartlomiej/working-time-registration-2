package com.jakubartlomiej.workingtimeregistration2;

import com.jakubartlomiej.workingtimeregistration2.entity.Role;
import com.jakubartlomiej.workingtimeregistration2.entity.User;
import com.jakubartlomiej.workingtimeregistration2.service.RoleService;
import com.jakubartlomiej.workingtimeregistration2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Start {
    private final RoleService roleService;
    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        if (roleService.count() == 0) {
            Role roleAdmin = new Role();
            roleAdmin.setRoleName("ROLE_ADMIN");
            roleAdmin.setSimplifiedRoleName("Administrator");
            Role roleUser = new Role();
            roleUser.setRoleName("ROLE_USER");
            roleUser.setSimplifiedRoleName("Użytkownik");
            roleService.addRole(roleAdmin);
            roleService.addRole(roleUser);
        }
        if (userService.count() == 0) {
            List<Role> role_admin = roleService.findByRoleName("ROLE_ADMIN")
                    .stream()
                    .collect(Collectors.toList());
            User user = new User();
            user.setLogin("admin");
            user.setPassword("admin");
            user.setRoles(role_admin);
            user.setCardNumber("000000000");
            user.setFirstName("Admin");
            user.setLastName("Admin");
            userService.save(user);
        }
    }
}