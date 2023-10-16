package eventProject;


import eventProject.model.Event;
import eventProject.repository.EventRepository;
import eventProject.repository.EventSubscriberRepository;
import eventProject.service.EventSubscriberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


@SpringBootApplication
@EnableScheduling
@EnableCaching
public class Application implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventSubscriberService eventSubscriberService;

    @Override
    public void run(String... args) {
        initDate();
    }


    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .run(args);
    }

    private void initDate(){
        try{
            logger.info("Inserting -> {}", eventRepository.save(new Event("Event1",
                    "emptyDesc",
                    "venue_B",
                    "A_street",
                    dateFormat.parse("2023-10-06 14:30"),
                    "user_A",
                    50)));

            logger.info("Inserting -> {}", eventRepository.save(new Event("Event2",
                    "emptyDesc",
                    "venue_B",
                    "A_street",
                    dateFormat.parse("2023-10-05 14:30"),
                    "user_A",
                    30)));

            Event event = new Event("Event3",
                    "Event with subscribers",
                    "venue_A",
                    "A_street",
                    dateFormat.parse(dateFormat.format(new Date(System.currentTimeMillis() + 1860 * 1000))),
                    "user_A",
                    50);
            logger.info("Inserting -> {}", eventRepository.save(event));
            eventSubscriberService.addSubscriberEvent("subscriber A ", event.getId());

            logger.info("Inserting -> {}", eventRepository.save(new Event("Event4",
                    "emptyDesc",
                    "venue_A",
                    "A_street",
                    dateFormat.parse(dateFormat.format(new Date(System.currentTimeMillis() + 1860 * 1000))),
                    "user_A",
                    50)));

            logger.info("Inserting -> {}", eventRepository.save(new Event("Event5",
                    "emptyDesc",
                    "venue_A",
                    "B_street",
                    new Date(),
                    "user_A",
                    100)));



            logger.info("All events -> {}", eventRepository.findAll());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 }
