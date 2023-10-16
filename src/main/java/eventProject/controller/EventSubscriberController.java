package eventProject.controller;

import eventProject.model.EventSubscriber;
import eventProject.service.EventSubscriberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EventSubscriberController {

    @Autowired
    EventSubscriberService eventSubscriberService;

    Logger logger = LoggerFactory.getLogger(EventSubscriberController.class);
    @PostMapping("/eventSubscriber/subscribe")

    public ResponseEntity<EventSubscriber> addSubscriberEvent(@RequestParam("user_name") String userName, @RequestParam("event_id") Long eventId) {
        try {
            EventSubscriber eventSub = eventSubscriberService.addSubscriberEvent(userName, eventId);
            return new ResponseEntity<>(eventSub,HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating events sub");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
