package com.jakubartlomiej.workingtimeregistration2.repository;

import com.jakubartlomiej.workingtimeregistration2.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEmployeeIdAndDateBetweenOrderByDateDesc(Long employee_id, LocalDateTime dateStart, LocalDateTime dateEnd);
}