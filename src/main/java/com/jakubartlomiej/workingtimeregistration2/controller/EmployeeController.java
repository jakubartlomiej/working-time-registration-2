package com.jakubartlomiej.workingtimeregistration2.controller;

import com.jakubartlomiej.workingtimeregistration2.entity.Employee;
import com.jakubartlomiej.workingtimeregistration2.entity.Event;
import com.jakubartlomiej.workingtimeregistration2.service.EmployeeService;
import com.jakubartlomiej.workingtimeregistration2.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EventService eventService;

    @GetMapping("/employee")
    public String employee() {
        return "employee/employee";
    }

    @PostMapping("/employee")
    public String findEmployee(@RequestParam(value = "cardNumber") String cardNumber, Model model) {
        Optional<Employee> employee = employeeService.findByCardNumber(cardNumber);
        if (employee.isPresent() && employee.get().isActive()) {
            return "redirect:/employee/" + cardNumber;
        } else {
            model.addAttribute("cardNotFound", "Brak przypisanej karty w systemie lub karta zablokowana!");
            return "employee/employee";
        }
    }

    @GetMapping("/employee/{cardNumber}")
    public String getEmployeeInfo(@PathVariable String cardNumber, Model model, HttpServletResponse response) {
        if (employeeService.findByCardNumber(cardNumber).isPresent()) {
            model.addAttribute("employee", employeeService.findByCardNumber(cardNumber).get());
            return "employee/info";
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "error/error";
        }
    }

    @PostMapping("/employee/{cardNumber}")
    public String submitEvent(@PathVariable String cardNumber) {
        Employee employee = employeeService.findByCardNumber(cardNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Pracownik nie znaleziony"));
        String eventName = (employee.isWork()) ? "Wyjście" : "Wejście";

        Event event = new Event();
        event.setEmployee(employee);
        event.setDate(LocalDateTime.now());
        event.setEventName(eventName);
        eventService.addEvent(event);

        employee.setWork(eventName.equalsIgnoreCase("Wejście"));
        employeeService.save(employee);
        return "redirect:/employee";
    }
}
