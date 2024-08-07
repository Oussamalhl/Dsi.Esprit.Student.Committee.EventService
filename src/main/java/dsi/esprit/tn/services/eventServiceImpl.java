package dsi.esprit.tn.services;

import dsi.esprit.tn.Models.Event;
import dsi.esprit.tn.repository.eventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Service
public class eventServiceImpl implements IeventServiceImpl {

    @Autowired
    eventRepository eventRepository;

    //    @Override
//    public void addEvent(Event event, Long userId) {
//        eventRepository.addEvent(event.getName(),
//                event.getDescription(),
//                event.getStatus(),
//                event.getType(),
//                event.getEventDateStart(),
//                event.getEventDateEnd(),
//                event.getEventLocation(),
//                userId);
//    }
    @Override
    public void userEventParticipate(Long user_id, Long event_id) {
        eventRepository.userEventParticipate(user_id, event_id);
    }

    @Override
    public void eventClubParticipate(Long club_id, Long event_id) {
        eventRepository.eventClubParticipate(club_id, event_id);
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long idEvent) {
        eventRepository.deleteById(idEvent);
    }

    @Override
    public Event updateEvent(Event Event) {
        return eventRepository.save(Event);
    }

    @Override
    public List<Event> showAllEvent() {
        return eventRepository.findAll();
    }

    @Override
    public Long showEventUser(String username) {
        return eventRepository.findUsernameId(username);
    }

    @Override
    public Event showEvent(Long idEvent) {
        return eventRepository.findById(idEvent).orElse(null);
    }

    @Override
    public Long getUser(Long idUser, Long idEvent) {
        return eventRepository.findUserId(idUser, idEvent);

    }

    @Override
    public Long getClub(String name) {
        return eventRepository.findClubId(name);

    }

    @Override
    public List<String> getTags() {
        return eventRepository.getEventTags();

    }

    @Override
    public List<String> getClubs(Long idEvent) {
        return eventRepository.getEventClubs(idEvent);

    }

    @Override
    public List<Event> getClubEvents(Long idClub) {
        return eventRepository.getClubEvents(idClub);

    }

    @Override
    public List<Long> getUsers(Long idEvent) {
        return eventRepository.getEventUsers(idEvent);

    }

    @Override
    public List<Object[]> getEventParticipations(Long idEvent) {
        return eventRepository.getEventParticipations(idEvent);

    }

    @Override
    public List<Object[]> getParticipatableEventUsers(Long idEvent) {
        return eventRepository.getParticipatableEventUsers(idEvent);

    }

    @Override
    public void deleteEventClubs(Long idEvent) {
        eventRepository.deleteEventClubs(idEvent);

    }

    @Override
    public void deleteUserEvents(Long idEvent) {
        eventRepository.deleteUserEvents(idEvent);

    }

    @Override
    public String getUsernameDetails(String username) {
        return eventRepository.findUsernameDetails(username);

    }

    @Override
    public List<String> getClubs() {
        return eventRepository.getClubs();
    }

    @Override
    public Long getUsernameId(String username) {
        return eventRepository.findUsernameId(username);
    }

    @Override
    public void deleteUserEvent(Long idEvent, Long idUser) {
        eventRepository.deleteEventUser(idEvent, idUser);
    }

    @Override
    public void confirmUserEvent(Long eventId, Long userId) {
        eventRepository.ConfirmUserEvent(eventId, userId);
    }

    @Override
    public void RateUserEvent(Integer Rating, Long eventId, Long userId) {
        eventRepository.RateUserEvent(Rating, eventId, userId);
    }

    @Override
    public Integer UserEventRate(Long eventId, Long userId) {
        return eventRepository.UserEventRate(eventId, userId);
    }
    @Override
    public Boolean UserEventConfirmation(Long eventId, Long userId){
        return eventRepository.UserEventConfirmation(eventId, userId);
    }
    @Override
    public Integer countEventsByMonth(int month, int year) {
        return eventRepository.countEventsByMonth(month, year);
    }
    @Override
    public List<Event> upcomingEvents(int month, int year) {
        return eventRepository.upcomingEvents(month, year);
    }
    @Override
    public List<Event> latestEvents(int year){
        return eventRepository.latestEvents(year);
    }

    @Override
    public List<Integer[]> countAllEventsByMonth(Integer year) {
        return eventRepository.countAllEventsByMonth(year);

    }

    @Override
    public List<Object[]> countEventsStatusByYear(Integer year) {
        return eventRepository.countEventStatusByYear(year);

    }

    @Override
    public List<Object[]> countEventsTypeByYear(Integer year) {
        return eventRepository.countEventTypeByYear(year);

    }

    @Override
    public List<Object[]> bestEventsOfTheYear(Integer year) {
        return eventRepository.bestEventsOfTheYear(year);

    }
    @Override
    public Integer eventAverageRating(Long idEvent) {
        return eventRepository.eventAverageRating(idEvent);
    }

    @Override

    public Integer countAllEvents() {
        return eventRepository.countAllEvents();
    }

    @Override

    public Integer countAllEventsParticipations() {
        return eventRepository.countAllEventsParticipations();
    }

    @Override
    public Integer countAllConfirmedEvents() {
        return eventRepository.countAllConfirmedEvents();
    }

    @Override
    public Integer countEventParticipations(Long idEvent) {
        return eventRepository.countEventParticipations(idEvent);
    }

    @Override
    public Integer countEventConfirmed(Long idEvent) {
        return eventRepository.countEventConfirmed(idEvent);
    }

    @Override
    public String getUserClub(String username) {
        return eventRepository.getUserClub(username);
    }

    @Override
    public Long getUserClubId(String username) {
        return eventRepository.getUserClubId(username);
    }


}
