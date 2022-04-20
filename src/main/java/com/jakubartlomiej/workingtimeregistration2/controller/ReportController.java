package com.jakubartlomiej.workingtimeregistration2.controller;

import com.jakubartlomiej.workingtimeregistration2.entity.Employee;
import com.jakubartlomiej.workingtimeregistration2.entity.Event;
import com.jakubartlomiej.workingtimeregistration2.service.EmployeeService;
import com.jakubartlomiej.workingtimeregistration2.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final EventService eventService;
    private final EmployeeService employeeService;

    @GetMapping("/show")
    public String showReportPage(Model model) {
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employeeList", employees);
        return "report/show";
    }

    @PostMapping("/show")
    public String submitShowReport(@RequestParam long employeeId, @RequestParam String dateStart, @RequestParam String dateEnd, Model model) {
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employeeList", employees);

        DateTimeFormatter formatterStartDay = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();

        DateTimeFormatter formatterEndDay = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 23)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 59)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 59)
                .toFormatter();

        List<Event> report = eventService.findByEmployeeIdAndDateBetweenOrderByDateDesc(employeeId,
                LocalDateTime.parse(dateStart, formatterStartDay),
                LocalDateTime.parse(dateEnd, formatterEndDay));

        model.addAttribute("eventList", report);
        return "report/show";
    }
}
