package com.jakubartlomiej.workingtimeregistration2.service;

import com.jakubartlomiej.workingtimeregistration2.entity.Event;
import com.jakubartlomiej.workingtimeregistration2.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public void addEvent(Event event) {
        eventRepository.save(event);
    }

    public List<Event> findByEmployeeIdAndDateBetweenOrderByDateDesc(long employeeId,
                                                                     LocalDateTime dateStart,
                                                                     LocalDateTime dateEnd) {
        return eventRepository.findByEmployeeIdAndDateBetweenOrderByDateDesc(employeeId, dateStart, dateEnd);
    }
}