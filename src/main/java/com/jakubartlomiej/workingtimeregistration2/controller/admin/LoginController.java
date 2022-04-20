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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class LoginController {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/user/{employeeId}/add-login")
    public String addLoginPage(@PathVariable long employeeId, Model model, HttpServletResponse response) {
        if (!employeeService.findById(employeeId).isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "error/error";
        }
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new UsernameNotFoundException("Pracwonk nie znaleziony"));

        User user = new User();
        user.setId(employee.getId());
        user.setCardNumber(employee.getCardNumber());
        user.setLastName(employee.getLastName());
        user.setFirstName(employee.getFirstName());
        user.setWork(employee.isWork());
        user.setActive(employee.isActive());

        model.addAttribute("employee", employee);
        model.addAttribute("user", user);
        return "admin/user/add-login";

    }

    @PostMapping("user/{employeeId}/add-login")
    public String addLoginToUser(@ModelAttribute @Valid User user, BindingResult bindingResult, @PathVariable long employeeId, @RequestParam String roleName, Model model) {
        if (userService.findByLogin(user.getLogin()).isPresent()) {
            ObjectError error = new ObjectError("loginExist", "Podany login istnieje w systemie");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employeeService.findById(employeeId).orElseThrow(() -> new UsernameNotFoundException("Pracwonk nie znaleziony")));
            return "admin/user/add-login";
        }

        user.setId(employeeId);
        List<Role> roles = roleService.findByRoleName(roleName)
                .stream()
                .collect(Collectors.toList());
        user.setRoles(roles);
        userService.addLogin(user);
        return "redirect:/admin/user/" + employeeId;
    }
}