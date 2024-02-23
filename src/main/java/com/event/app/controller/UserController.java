package com.event.app.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.event.app.entity.Event;
import com.event.app.entity.User;
import com.event.app.repository.EventRepository;
import com.event.app.repository.UserRepository;

@RestController
@RequestMapping
public class UserController {

	private final UserRepository userRepository;
	private final EventRepository eventRepository;

	public UserController(UserRepository userRepository, EventRepository eventRepository) {
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
	}

	@GetMapping
	public List<User> getEvents() {
		return userRepository.findAll();
	}

	@GetMapping("/{id}")
	public User getUser(@PathVariable Long id) {
		return userRepository.findById(id).orElseThrow(RuntimeException::new);
	}

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) throws URISyntaxException {
		List<Event> events = new ArrayList<>();
		user.setEvents(events); // Set the events list for the user
		User savedUser = userRepository.save(user);

		return ResponseEntity.created(new URI("/User/" + savedUser.getId())).body(savedUser);
	}

	@GetMapping("/{id}/events")
	public ResponseEntity<List<Event>> getEvents(@PathVariable Long id) {
		List<Event> userEvents = userRepository.findById(id).map(User::getEvents).orElse(Collections.emptyList());
		return ResponseEntity.ok(userEvents);
	}

	@PostMapping("/{id}/events")
	public ResponseEntity<Event> createEvent(@PathVariable Long id, @RequestBody Event event)
			throws URISyntaxException {
		Optional<User> optionalUser = userRepository.findById(id);

		if (optionalUser.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		User user = optionalUser.get();
		event.setUser(user);

		Event savedEvent = eventRepository.save(event);
		user.getEvents().add(savedEvent);
		return ResponseEntity.created(new URI("/Event/" + savedEvent.getId())).body(savedEvent);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
		eventRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}