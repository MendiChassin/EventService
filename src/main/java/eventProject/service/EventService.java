package eventProject.service;

import eventProject.model.Event;
import eventProject.model.EventSubscriber;
import eventProject.repository.EventRepository;
import eventProject.repository.EventSubscriberRepository;
import eventProject.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static eventProject.util.EventSortDirection.Descending;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventSubscriberRepository eventSubscriberRepository;

    Logger logger = LoggerFactory.getLogger(EventService.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    public Optional<Event> getEventById(Long eventId){
        return eventRepository.findById(eventId);
    }

    public List<Event> getAllEvents(EventFilter eventFilter, String filterValue, EventSort sortBy,EventSortDirection sortDirection) {
        List<Event> events = new ArrayList<>();
        Sort sort = getSortByParamOrDefault(sortBy, sortDirection);

        if(eventFilter != null && filterValue != null){
            switch (eventFilter){
                case venue -> events.addAll(eventRepository.findAllByVenue(filterValue, sort));
                case location -> events.addAll(eventRepository.findAllByLocationName(filterValue, sort));
            }
        } else {
            events.addAll(eventRepository.findAll(sort));
        }
        return events;
    }
    Sort getSortByParamOrDefault(EventSort sortBy,EventSortDirection sortDirection){
        if(sortBy == null) return Sort.by(Sort.Direction.ASC, "id");
        Sort.Direction dir = sortDirection == Descending ? Sort.Direction.DESC: Sort.Direction.ASC;
        return Sort.by(dir, sortBy.name());
    }


    public List<Event> getAllEventsByTime(Date time){
       return eventRepository.findAllByTime(time);
    }

    public Event craeteEvent(Event event) throws Exception {

        try {

            return eventRepository
                    .save(new Event(event.getTitle(),
                            event.getDescription(),
                            event.getVenue(),
                            event.getLocationName(),
                            event.getTime(),
                            event.getCreatorName(),
                            event.getParticipants()));
        } catch (Exception e){
            logger.error("Error occurred while creating an event ",e);
            throw new Exception("Error occurred while creating an event");
        }
    }

    public Event updateEvent(Event event) throws Exception {
        try {
            Optional<Event> eventData = eventRepository.findById(event.getId());
            if (eventData.isPresent()) {
                Event eventToUpdate = eventData.get();
                eventToUpdate.setTime(event.getTime());
                eventToUpdate.setDescription(event.getDescription());
                eventToUpdate.setCreatorName(event.getCreatorName()); // TODO verify this allowed
                eventToUpdate.setTitle(event.getTitle());
                eventToUpdate.setVenue(event.getVenue());
                return eventRepository.save(eventToUpdate);
            } else {
                throw  new NotFoundException("Event not found");
            }
            } catch (Exception e){
            logger.error("Error occurred while updating an event ",e);
            throw new Exception("Error occurred while updating an event");
        }
    }

    public void deleteEvent(Long eventId) throws Exception {
        try {
            eventRepository.deleteById(eventId);
        } catch (Exception e){
            logger.error("Error occurred while deleting an event",e);
            throw new Exception("Error occurred while deleting an event");
        }
    }
    public void deleteAllEvents() throws Exception {
        try {
            eventRepository.deleteAll();
        } catch (Exception e){
            logger.error("Error occurred while deleting all events",e);
            throw new Exception("Error occurred while deleting all events");
        }
    }

    public void notifySubscribers() throws ParseException {
        Date halfHOurBack = dateFormat.parse(dateFormat.format(new Date(System.currentTimeMillis() + 1800 * 1000)));
        log.info("Picking events with time :" + halfHOurBack);
        List<Event> upcomingEvents = getAllEventsByTime(halfHOurBack);
        if(!upcomingEvents.isEmpty()){
            for (Event event : upcomingEvents) {

                log.info("Looking for : " + event.getTitle() + " event subscribers");
                List<EventSubscriber> eventSubs = eventSubscriberRepository.findAllByEvent(event);
                for (EventSubscriber eventSubscriber :eventSubs) {
                    logger.info("Notifying event sub : " + eventSubscriber.getUser_name());
                }

            }
        }
    }

}
