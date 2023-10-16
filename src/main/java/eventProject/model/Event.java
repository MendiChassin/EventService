package eventProject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "event")
public class Event {

    // Better with UUID , more complicated when retrieve by ID
    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JoinColumn(name = "event_id",insertable = false, updatable = false)
    @JsonBackReference
    private Set<EventSubscriber> eventSubscribers;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "venue")
    private String venue;

    @Column(name = "location_name")
    private String locationName;

    // TODO time
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @CreatedDate
    private Instant createdAt;
    @Column(name= "time")
    private Date time;

    @Column(name= "creator_name")
    private String creatorName;

    @Column(name = "participants")
    private int participants;


    public Event(){}

    public Event( String title,
                  String description,
                  String venue, String locationName, Date eventTime,
                  String creatorName,
                  int participants) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.locationName = locationName;
        this.time = eventTime;
        this.creatorName = creatorName;
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", venue='" + venue + '\'' +
                ", locationName='" + locationName + '\'' +
                ", Time=" + time +
                ", creatorName='" + creatorName + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVenue() {
        return venue;
    }

    public String getLocationName() {
        return locationName;
    }

    public Date getTime() {
        return time;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Set<EventSubscriber> getEventSubscribers() {
        return eventSubscribers;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setEventSubscribers(Set<EventSubscriber> eventSubscribers) {
        this.eventSubscribers = eventSubscribers;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
}
