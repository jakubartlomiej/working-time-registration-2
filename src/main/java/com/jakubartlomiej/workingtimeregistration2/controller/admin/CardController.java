package com.jakubartlomiej.workingtimeregistration2.controller.admin;

import com.jakubartlomiej.workingtimeregistration2.entity.Employee;
import com.jakubartlomiej.workingtimeregistration2.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CardController {
    private final EmployeeService employeeService;

    @GetMapping("/user/{employeeId}/card-disabled")
    public String disabledCard(@PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Pracownik nie znaleziony"));

        if (employee != null) {
            employee.setActive(false);
            employeeService.save(employee);
        }

        return "redirect:/admin/user/" + employeeId;
    }

    @GetMapping("/user/{employeeId}/card-enabled")
    public String enabledCard(@PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Pracownik nie znaleziony"));
        if (employee != null) {
            employee.setActive(true);
            employeeService.save(employee);
        }
        return "redirect:/admin/user/" + employeeId;
    }
}