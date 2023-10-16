package eventProject.service;

import eventProject.model.Event;
import eventProject.model.EventSubscriber;
import eventProject.repository.EventSubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class EventSubscriberService {

    @Autowired
    EventService eventService;

    @Autowired
    EventSubscriberRepository eventSubscriberRepository;

    Logger logger = LoggerFactory.getLogger(EventSubscriberService.class);
    public EventSubscriber addSubscriberEvent(String userName, Long eventId) throws Exception {
        try {
            Optional<Event> optionalEvent = eventService.getEventById(eventId);
            if (optionalEvent.isPresent()) {
                EventSubscriber eventSub = eventSubscriberRepository.save(new EventSubscriber(optionalEvent.get(), userName));
                logger.info("Event sub created :" + eventSub);
                return eventSub;
            } else {
                throw new NotFoundException("Event not found");
            }
        } catch (Exception e){
            logger.error("Event sub not created",e);
            throw new Exception("Event sub not created");
        }
    }
//    public List<String> getEventSubscriberName(Long eventId){
//        Optional<Event> optionalEvent = eventService.getEventById(eventId);
//        if (optionalEvent.isPresent()) {
//
//        }
//
//    }

}
