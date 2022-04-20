package com.jakubartlomiej.workingtimeregistration2.controller.admin;

import com.jakubartlomiej.workingtimeregistration2.entity.Employee;
import com.jakubartlomiej.workingtimeregistration2.entity.Role;
import com.jakubartlomiej.workingtimeregistration2.entity.User;
import com.jakubartlomiej.workingtimeregistration2.service.EmployeeService;
import com.jakubartlomiej.workingtimeregistration2.service.RoleService;
import com.jakubartlomiej.workingtimeregistration2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RoleController {
    private final RoleService roleService;
    private final EmployeeService employeeService;
    private final UserService userService;

    @GetMapping("/user/{employeeId}/role-grant")
    public String grantRolePage(@PathVariable long employeeId, Model model) {
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new UsernameNotFoundException("Pracownik nie znaleziony"));
        List<Role> roles = roleService.findRoleToGrantByEmployeeId(employeeId);

        model.addAttribute("employee", employee);
        model.addAttribute("roles", roles);
        model.addAttribute("role", new Role());

        return "admin/role/grant";
    }

    @PostMapping("/user/{employeeId}/role-grant")
    public String grantRole(@PathVariable long employeeId, Role role) {
        userService.addRole(role, employeeId);

        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/role-delete")
    public String deleteRolePage(Model model, @PathVariable long employeeId) {
        User user = userService.findById(employeeId)
                .orElseThrow(() -> new UsernameNotFoundException("User nie znaleziony"));

        model.addAttribute("user", user);
        model.addAttribute("role", new Role());

        return "admin/role/delete";
    }

    @PostMapping("/user/{employeeId}/role-delete")
    public String deleteRole(@PathVariable long employeeId, Role role) {
        userService.deleteRole(role, employeeId);
        return "redirect:/admin/user/" + employeeId;
    }
}
