package eventProject.integration;

import eventProject.model.Event;
import eventProject.Application;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testEventController {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();


    // TODO global config
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");



    @Test
    public void testCreateEvent() throws ParseException {
        Event event = buildEventEntity();
        HttpEntity<Event> request = new HttpEntity<>(event);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port +
                "/api/event", request, String.class);

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testGetEvent() throws ParseException {
        Event event = buildEventEntity();
        HttpEntity<Event> request = new HttpEntity<>(event);
        restTemplate.postForEntity("http://localhost:" + port + "/api/event", request, String.class);


        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port +
                "/api/event/1", String.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private Event buildEventEntity() throws ParseException {
        return new Event("EventTitle1",
                "emptyDesc",
                "venue_A",
                "A_street",
                dateFormat.parse(dateFormat.format(new Date(System.currentTimeMillis() + 1820 * 1000))),
                "user_A",
                50);
    }
}
