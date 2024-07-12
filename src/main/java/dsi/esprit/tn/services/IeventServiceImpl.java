package dsi.esprit.tn.services;

import dsi.esprit.tn.Models.Event;

import java.util.List;

public interface IeventServiceImpl {
    Event addEvent(Event event);
    void deleteEvent(Long idEvent);
    Event updateEvent(Event Event);
    List<Event> showAllEvent();
    Event showEvent(Long idEvent);
    Long showEventUser(String username);
    Long getUser(Long idUser,Long idEvent);
    void userEventParticipate(Long user_id,Long event_id);
    Long getClub(String name);
    void eventClubParticipate(Long club_id,Long event_id);
    List<String> getTags();
    void deleteEventClubs(Long idEvent);
    void deleteUserEvents(Long idEvent);
    List<String> getClubs(Long idEvent);
    List<Long> getUsers(Long idEvent);
    List<Object[]> getEventParticipations(Long idEvent);
    List<Object[]> getParticipatableEventUsers(Long idEvent);
    String getUsernameDetails(String username);
    List<String> getClubs();
    Long getUsernameId(String username);
    void deleteUserEvent(Long idEvent, Long idUser);
    void confirmUserEvent(Long eventId, Long userId);

    List<Object[]> countEventsTypeByYear(Integer year);
    List<Object[]> countEventsStatusByYear(Integer year);
    List<Integer[]> countAllEventsByMonth(Integer year);
    Integer countEventsByMonth(int month,int year);
    List<Event> upcomingEvents(int month, int year);
    List<Event> latestEvents(int year);
    List<Object[]> bestEventsOfTheYear(Integer year);
    Integer eventAverageRating(Long idEvent);
    Integer countAllEvents();
    Integer countAllEventsParticipations();
    Integer countAllConfirmedEvents();
    Integer countEventParticipations(Long idEvent);
    Integer countEventConfirmed(Long idEvent);
    String getUserClub(String username);
    List<Event> getClubEvents(Long idClub);
    Long getUserClubId(String username);
    void RateUserEvent(Integer Rating, Long eventId, Long userId);
    Integer UserEventRate(Long eventId, Long userId);
    Boolean UserEventConfirmation(Long eventId, Long userId);
}
