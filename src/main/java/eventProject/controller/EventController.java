package eventProject.controller;


import java.time.Duration;
import java.util.List;
import eventProject.model.Event;
import eventProject.service.EventService;
import eventProject.util.EventFilter;
import eventProject.util.EventSort;
import eventProject.util.EventSortDirection;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

@RestController
@RequestMapping("/api")
public class EventController {
    private final Bucket bucket;
    public EventController() {
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }
    @Autowired
    EventService eventService;

    @GetMapping("/event/all")
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(required = false) EventFilter eventFilter,
                                                    @RequestParam(required = false) String filterValue,
                                                    @RequestParam(required = false) EventSort sortBy,
                                                    @RequestParam(required = false) EventSortDirection sortDirection) {
        try {
            if (bucket.tryConsume(1)) {  // Could be applied per project to all api , time issue
                List<Event> events = eventService.getAllEvents(eventFilter, filterValue, sortBy, sortDirection);
                if (events.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(events, HttpStatus.OK);
            }
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") long id) {
        return eventService.getEventById(id).map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/event", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            return new ResponseEntity<>(eventService.craeteEvent(event),HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/event/{id}")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
        try {
            return new ResponseEntity<>(eventService.updateEvent(event), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") long id) {
        try {
            eventService.deleteEvent(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/event")
    public ResponseEntity<HttpStatus> deleteAllEvents() {
        try {
            eventService.deleteAllEvents();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
