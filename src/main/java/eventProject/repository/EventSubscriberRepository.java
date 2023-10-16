package eventProject.repository;

import eventProject.model.Event;
import eventProject.model.EventSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventSubscriberRepository extends JpaRepository<EventSubscriber, Long> {
        List<EventSubscriber> findAllByEvent(Event event);
}

