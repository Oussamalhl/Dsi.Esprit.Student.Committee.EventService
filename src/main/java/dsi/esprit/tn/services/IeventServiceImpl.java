package dsi.esprit.tn.services;

import dsi.esprit.tn.Models.Event;

import java.util.List;

public interface IeventServiceImpl {
    void addEvent(Event event);
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
    void deleteEventUsers(Long idEvent);
    List<String> getClubs(Long idEvent);
    List<Long> getUsers(Long idEvent);
    List<Object[]> getEventParticipations(Long idEvent);
    List<Object[]> getParticipatableEventUsers(Long idEvent);
    String getUsernameDetails(String username);

}
