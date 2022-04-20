package com.jakubartlomiej.workingtimeregistration2.repository;

import com.jakubartlomiej.workingtimeregistration2.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCardNumber(String cardNumber);
    List<Employee> findByFirstNameOrLastName(String firstName, String lastName);

}