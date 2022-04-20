package com.jakubartlomiej.workingtimeregistration2.service;

import com.jakubartlomiej.workingtimeregistration2.entity.Employee;
import com.jakubartlomiej.workingtimeregistration2.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> findByCardNumber(String cardNumber) {
        return employeeRepository.findByCardNumber(cardNumber);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "lastName"));
    }

    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    public List<Employee> findByFirstNameOrLastName(String firstName, String lastName){
        return employeeRepository.findByFirstNameOrLastName(firstName,lastName);
    }
}
