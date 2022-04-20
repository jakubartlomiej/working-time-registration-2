package com.jakubartlomiej.workingtimeregistration2.controller.admin;

import com.jakubartlomiej.workingtimeregistration2.entity.User;
import com.jakubartlomiej.workingtimeregistration2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class PasswordController {
    private final UserService userService;

    @GetMapping("/user/{employeeId}/password")
    public String resetPasswordPage(@PathVariable long employeeId, Model model) {
        User user = userService.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("User nie znaleziony"));
        model.addAttribute("user", user);
        return "admin/password/password";
    }

    @PostMapping("/user/{employeeId}/password")
    public String resetPassword(@RequestParam @Min(value = 8, message = "Minimum 8 znaków") String newPassword,
                                @PathVariable long employeeId,
                                Model model) {
        User user = userService.findById(employeeId).orElseThrow(() -> new UsernameNotFoundException("User nie znaleziony"));

        if (newPassword.length() < 8) {
            model.addAttribute("error", "Minimum 8 znaków");
            model.addAttribute("user", user);
            return "admin/password/password";
        }

        user.setPassword(newPassword);
        userService.save(user);
        return "redirect:/admin/user/" + employeeId;
    }
}
