package com.event.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.event.app.entity.Event;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // You can define custom methods here if needed
}