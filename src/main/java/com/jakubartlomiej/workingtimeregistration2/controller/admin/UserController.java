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
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final EmployeeService employeeService;

    @GetMapping("/add-user")
    public String addUserPage(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/user/add";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult) {
        if (employeeService.findByCardNumber(employee.getCardNumber()).isPresent()) {
            ObjectError error = new ObjectError("cardNumberExist", "Podany numer karty istnieje w systemie");
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) {
            return "admin/user/add";
        }
        employeeService.save(employee);
        return "redirect:/admin/users";
    }

    @GetMapping("/add-user-with-login")
    public String addUserWithLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "admin/user/add-with-login";
    }

    @PostMapping("/add-user-with-login")
    public String addUserWithLogin(@ModelAttribute("user") @Valid User user,
                                   BindingResult bindingResult,
                                   @RequestParam String roleName) {
        if (employeeService.findByCardNumber(user.getCardNumber()).isPresent()) {
            ObjectError error = new ObjectError("cardNumberExist", "Podany numer karty istnieje w systemie");
            bindingResult.addError(error);
        }

        if (userService.findByLogin(user.getLogin()).isPresent()) {
            ObjectError error = new ObjectError("loginExist", "Podany login istnieje w systemie");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            return "admin/user/add-with-login";
        }

        List<Role> roles = roleService.findByRoleName(roleName)
                .stream()
                .collect(Collectors.toList());
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/{employeeId}/user-delete")
    public String confirmDeleteUser(@PathVariable long employeeId, Model model) {
        model.addAttribute("user", userService.findById(employeeId).
                orElseThrow(() -> new RuntimeException("User nie znaleziony")));

        return "admin/user/delete";
    }

    @PostMapping("/user/{employeeId}/user-delete")
    public String deleteUserAccount(@PathVariable long employeeId) {
        User user = userService.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("User nie znaleziony"));

        userService.deleteAllRole(user.getId());
        userService.deleteOnlyUserById(user.getId());

        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/users")
    public String getEmployeeListPage(Model model) {
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employeesList", employees);
        return "admin/user/users";
    }

    @GetMapping("/user")
    public String searchUser(@RequestParam String search, Model model) {
        List<Employee> employees = employeeService.findByFirstNameOrLastName(search, search);

        model.addAttribute("employeesList", employees);
        model.addAttribute("searchValue", search);
        return "admin/user/users";
    }

    @GetMapping("/user/{employeeId}")
    public String employeeInfoPage(@PathVariable long employeeId, Model model, HttpServletResponse response) {
        if (employeeService.findById(employeeId).isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "error/error";
        }

        if (userService.findById(employeeId).isEmpty()) {
            Employee employee = employeeService.findById(employeeId)
                    .orElseThrow(() -> new UsernameNotFoundException("Pracownik nie znaleizony"));

            model.addAttribute("employee", employee);
            return "admin/user/user-info";
        } else {
            User user = userService.findById(employeeId)
                    .orElseThrow(() -> new UsernameNotFoundException("User nie znaleziony"));

            model.addAttribute("user", user);
            return "admin/user/user-with-login-info";
        }
    }

    @PostMapping("/user-with-login/{employeeId}")
    public String updateUser(@ModelAttribute("user") @Valid User updateUser,
                             BindingResult bindingResult,
                             @PathVariable long employeeId) {
        User user = userService.findById(employeeId)
                .orElseThrow(() -> new UsernameNotFoundException("User nie znaleziony"));

        checkFieldInUserWithLogin(updateUser, bindingResult, user);

        if (bindingResult.hasErrors()) {
            updateUser.setRoles(user.getRoles());
            return "admin/user/user-with-login-info";
        }

        userService.update(updateUser);
        return "redirect:/admin/users";
    }

    @PostMapping("/user/{employeeId}")
    public String updateEmployee(@ModelAttribute("employee") @Valid Employee updateEmployee,
                                 BindingResult bindingResult,
                                 @PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new UsernameNotFoundException("Pracownik nie znaleziony"));

        if (!employee.getCardNumber().equals(updateEmployee.getCardNumber())) {
            if (employeeService.findByCardNumber(updateEmployee.getCardNumber()).isPresent()) {
                ObjectError error = new ObjectError("cardNumberExist", "Podany numer karty istnieje w systemie");
                bindingResult.addError(error);
            }
        }

        if (bindingResult.hasErrors()) {
            return "admin/user/user-info";
        }

        employeeService.save(updateEmployee);
        return "redirect:/admin/users";
    }

    private void checkFieldInUserWithLogin(User updateUser, BindingResult bindingResult, User user) {
        if (!user.getCardNumber().equals(updateUser.getCardNumber())) {
            if (employeeService.findByCardNumber(updateUser.getCardNumber()).isPresent()) {
                ObjectError error = new ObjectError("cardNumberExist", "Podany numer karty istnieje w systemie");
                bindingResult.addError(error);
            }
        }

        if (!user.getLogin().equals(updateUser.getLogin())) {
            if (userService.findByLogin(user.getLogin()).isPresent()) {
                ObjectError error = new ObjectError("loginExist", "Podany login istnieje w systemie");
                bindingResult.addError(error);
            }
        }
    }
}
