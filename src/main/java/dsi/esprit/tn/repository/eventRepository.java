package dsi.esprit.tn.repository;

import dsi.esprit.tn.Models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface eventRepository extends JpaRepository<Event, Long> {

    @Modifying
    @Transactional
    @Query(value = "insert into user_events (user_id,event_id,isConfirmed) VALUES (:user_id,:event_id,false)", nativeQuery = true)
    void userEventParticipate(@Param("user_id") Long user_id,
                              @Param("event_id") Long event_id
    );

    @Query(value = "SELECT id FROM clubs WHERE name=?1", nativeQuery = true)
    Long findClubId(String name);

    @Modifying
    @Transactional
    @Query(value = "insert into club_events (club_id,event_id) VALUES (:club_id,:event_id)", nativeQuery = true)
    void eventClubParticipate(@Param("club_id") Long club_id,
                              @Param("event_id") Long event_id
    );

    @Query(value = "SELECT id FROM users WHERE username=?1", nativeQuery = true)
    Long findUsernameId(String username);
    @Query(value = "SELECT username,email,firstname,lastname FROM users WHERE username=?1", nativeQuery = true)
    String findUsernameDetails(String username);

    @Query(value = "SELECT user_id FROM user_events WHERE user_id=?1 AND event_id=?2", nativeQuery = true)
    Long findUserId(Long user_id, Long event_id);

    @Query(value = "SELECT DISTINCT tags FROM Event_tags", nativeQuery = true)
    List<String> getEventTags();

    @Query(value = "select name from clubs " +
            "inner join club_events on clubs.id=club_events.club_id " +
            "where event_id=?1", nativeQuery = true)
    List<String> getEventClubs(Long event_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM club_events WHERE event_id=?1", nativeQuery = true)
    void deleteEventClubs(Long event_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_events WHERE event_id=?1", nativeQuery = true)
    void deleteEventUsers(Long event_id);

    @Query(value = "SELECT user_id FROM user_events WHERE event_id=?1", nativeQuery = true)
    List<Long> getEventUsers(Long event_id);

    @Query(value = "select username,email,name,isConfirmed from users " +
            "inner join user_events on users.id=user_events.user_id and user_events.event_id=?1 " +
            "inner join clubs on users.club_id=clubs.id",
            nativeQuery = true)
    List<Object[]> getEventParticipations(Long event_id);

    @Query(value = "select username,email,name from users " +
            "inner join club_events on users.club_id=club_events.club_id and club_events.event_id=?1 " +
            "inner join clubs on users.club_id=clubs.id where users.id not in " +
            "(select user_id from user_events where user_events.event_id=?1) ",
            nativeQuery = true)
    List<Object[]> getParticipatableEventUsers(Long event_id);
}
