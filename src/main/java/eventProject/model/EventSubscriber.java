package eventProject.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "eventSubscriber")
public class EventSubscriber {
    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonManagedReference
    private Event event;

    private String user_name;

    public EventSubscriber(){}

    public EventSubscriber(Event event, String user_name) {
        this.event = event;
        this.user_name = user_name;
    }

    public Event getEvent() {
        return event;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "EventSubscriber{" +
                "id=" + id +
                ", event=" + event +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
