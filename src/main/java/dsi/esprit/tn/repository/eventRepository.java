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

    @Query(value = "SELECT name FROM clubs", nativeQuery = true)
    List<String> getClubs();

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

    @Query(value = "select id from clubs " +
            "inner join club_events on clubs.id=club_events.club_id " +
            "where event_id=?1", nativeQuery = true)
    List<Long> getEventClubsId(Long event_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM club_events WHERE event_id=?1", nativeQuery = true)
    void deleteEventClubs(Long event_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_events WHERE event_id=?1", nativeQuery = true)
    void deleteUserEvents(Long event_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_events WHERE user_id=?1", nativeQuery = true)
    void deleteEventUser(Long user_id);

    @Modifying
    @Transactional
    @Query(value = "update user_events set isConfirmed=true where event_id=?1 and user_id=?2", nativeQuery = true)
    void ConfirmUserEvent(Long event_id, Long user_id);

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


    @Query(value = "select event_id from user_events group by event_id order by avg(Rating) desc", nativeQuery = true)
    List<Integer[]> bestEventsOfAllTime();

    @Query(value = "select event_id,avg(user_events.Rating),name from user_events " +
            "inner join events on user_events.event_id = events.id where year(eventDateEnd)=?1 " +
            "group by event_id " +
            "order by avg(user_events.Rating) desc",
            nativeQuery = true)
    List<Object[]> bestEventsOfTheYear(Integer year);
    @Query(value = "SELECT COUNT(*) FROM events", nativeQuery = true)
    Integer countAllEvents();
    @Query(value = "SELECT COUNT(*) FROM user_events", nativeQuery = true)
    Integer countAllEventsParticipations();
    @Query(value = "SELECT COUNT(*) FROM user_events where event_id=?1", nativeQuery = true)
    Integer countEventParticipations(Long idEvent);
    @Query(value = "SELECT COUNT(*) FROM user_events where isConfirmed=true ", nativeQuery = true)
    Integer countAllConfirmedEvents();
    @Query(value = "SELECT COUNT(*) FROM user_events where isConfirmed=true and event_id=?1", nativeQuery = true)
    Integer countEventConfirmed(Long idEvent);

    //    @Query(value = "SELECT * FROM events WHERE date BETWEEN :startDate and :endDate", nativeQuery = true)
//    List<Event> selectReclamationsByDate(@Param("startDate") Date startDate,@Param("endDate") Date endDate);
    @Query(value = "SELECT COUNT(*) FROM events WHERE MONTH(eventDateEnd)=? AND YEAR(eventDateEnd)=?", nativeQuery = true)
    Integer countEventsByMonth(@Param("month") int month, @Param("year") int year);

    @Query(value = "SELECT MONTH(eventDateEnd),COUNT(*) FROM events WHERE YEAR(eventDateEnd)=?1 GROUP BY MONTH(eventDateEnd)", nativeQuery = true)
    List<Integer[]> countAllEventsByMonth(Integer year);

    @Query(value = "SELECT status,COUNT(*) FROM events WHERE YEAR(eventDateEnd)=?1 GROUP BY status", nativeQuery = true)
    List<Object[]> countEventStatusByYear(Integer year);

    @Query(value = "SELECT type,COUNT(*) FROM events WHERE YEAR(eventDateEnd)=?1 GROUP BY type", nativeQuery = true)
    List<Object[]> countEventTypeByYear(Integer year);
}
