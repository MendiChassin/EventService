package eventProject.scheduler;

import java.text.ParseException;
import eventProject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    EventService eventService;

    @Scheduled(fixedRate = 60000)
    public void notifySubscribers() throws ParseException {
        eventService.notifySubscribers();
    }
}
