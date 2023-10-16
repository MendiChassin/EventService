package eventProject.repository;

import java.util.Date;
import java.util.List;

import eventProject.model.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    public List<Event> findAllByLocationName(String locationName, Sort sort);
    public List<Event> findAllByVenue(String venue, Sort sort);
    public List<Event> findAllByTime(Date time);
}
