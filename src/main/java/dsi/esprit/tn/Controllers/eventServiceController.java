package dsi.esprit.tn.Controllers;


import dsi.esprit.tn.Models.Event;
import dsi.esprit.tn.Models.eventFile;
import dsi.esprit.tn.security.jwt.JwtUtils;
import dsi.esprit.tn.services.IEmailingServiceImpl;
import dsi.esprit.tn.services.IeventFileServiceImpl;
import dsi.esprit.tn.services.IeventServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/event")
public class eventServiceController {

    private static final Logger logger = LoggerFactory.getLogger(eventServiceController.class);

    @Autowired
    IeventFileServiceImpl IEFS;
    @Autowired
    IEmailingServiceImpl IemailS;
    @Autowired
    private IeventServiceImpl eventservice;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/test")
    public String test() {
        return "Event api.";
    }

    @GetMapping("/test/auth")
    public String testAuth() {
        return "user authenticated.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean adminTest() {
        return true;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public Boolean userTest() {
        return true;
    }

    @GetMapping("/moderator")
    @PreAuthorize("hasRole('MODERATOR')")
    public Boolean moderatorTest() {
        return true;
    }

    @GetMapping("/getuserid")
    public Long getUserId(@RequestParam String username) {
        return eventservice.showEventUser(username);
    }

    @GetMapping("/getUserEvent")
    public Long getUser(@RequestParam Long idUser, @RequestParam Long idEvent) {
        return eventservice.getUser(idUser, idEvent);
    }

    @GetMapping("/getUserCheck")
    public Boolean getUserEventByUsername(HttpServletRequest request, @RequestParam Long idEvent) {
        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long idUser = eventservice.showEventUser(username);
            return eventservice.getUser(idUser, idEvent) == null;
        }
        return false;
    }

    //    @GetMapping("/getTargets")
//    public List<String> getTargets(@RequestParam String type) {
//        return eventservice.getTargets(type);
//    }
    @GetMapping("/getClubs")
    public List<String> getClubs() {
        return eventservice.getClubs();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/showallEvents")
    public List<Event> showAllEvents() {
        return eventservice.showAllEvent();
    }

    @GetMapping("/showEvent")
    public ResponseEntity<?> showEvent(@Valid @RequestParam Long idEvent) {
        return ResponseEntity.ok(eventservice.showEvent(idEvent));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteEvent")
    public ResponseEntity<?> deleteEvent(@Valid @RequestParam Long idEvent) {
        logger.info("user_events: {}", eventservice.getUsers(idEvent));
        eventservice.deleteUserEvents(idEvent);
        eventservice.deleteEventClubs(idEvent);
        eventservice.deleteEvent(idEvent);
        return ResponseEntity.ok("Event Id:" + idEvent + " is successfully deleted");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("/deleteUserEvent")
    public void deleteUserEvent(HttpServletRequest request, @RequestParam Long idEvent) {
        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long idUser = eventservice.showEventUser(username);
             eventservice.deleteUserEvent(idEvent,idUser);
        }

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteUserEventa")
    public void deleteUserEventAdmin(@RequestParam Long eventId, @RequestParam String username) {

        eventservice.deleteUserEvent(eventId,eventservice.getUsernameId(username));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteUserEventm")
    public void deleteUserEventMail(@RequestParam Long eventId, @RequestParam String username) throws Exception {
        List<String> user = Arrays.asList(eventservice.getUsernameDetails(username).split(",", -1));

        eventservice.deleteUserEvent(eventId,eventservice.getUsernameId(username));
        IemailS.CancelParticipation(user, eventservice.showEvent(eventId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateEvent")
    public Event updateEvent(@RequestBody Event event) {

       return eventservice.updateEvent(event);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/addEvent")
    public Event addEvent(@RequestBody Event event) {

        return eventservice.addEvent(event);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addClubEvent")
    public void addClubEvent(@RequestParam Long idEvent, @RequestBody List<String> clubs) {

        clubs.forEach(e ->
                eventservice.eventClubParticipate(eventservice.getClub(e), idEvent)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/addUserEvent")
    public void addUserEvent(HttpServletRequest request, @RequestParam Long idEvent) {

        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long idUser = eventservice.showEventUser(username);
            if (eventservice.getUser(idUser, idEvent) == null) {
                eventservice.userEventParticipate(eventservice.showEventUser(username), idEvent);
            }
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/getUserClub", produces = "text/plain")
    public String getUserClub(HttpServletRequest request) {
        String jwt = jwtUtils.parseJwt(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        return eventservice.getUserClub(username);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/getUserClubId")
    public Long getUserClubId(HttpServletRequest request) {
        String jwt = jwtUtils.parseJwt(request);
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        return eventservice.getUserClubId(username);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addUserEventa")
    public void addUserEventAdmin(@RequestParam Long idEvent, @RequestParam String username) {

        Long idUser = eventservice.getUser(eventservice.showEventUser(username), idEvent);
        if (idUser == null) {
            eventservice.userEventParticipate(eventservice.showEventUser(username), idEvent);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping(path = "/addFile/{id}")
    public eventFile addFile(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) {
        return IEFS.addFile(file, id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("/deleteFile/{File}")
    public void deleteFile(@PathVariable("File") Long File) {
        IEFS.removeFile(File);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/getFiles/{id}")
    public List<eventFile> EventFiles(@PathVariable("id") Long id) {
        return IEFS.GeteventFiles(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAllFiles")
    public List<eventFile> EventAllFiles() {
        return IEFS.findAll();
    }

    @GetMapping("/getTags")
    public List<String> getEventTags() {
        return eventservice.getTags();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getParticipations")
    public List<Object[]> getEventParticipations(@RequestParam Long idEvent) {
        return eventservice.getEventParticipations(idEvent);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getParticipatables")
    public List<Object[]> getEventParticipatables(@RequestParam Long idEvent) {
        return eventservice.getParticipatableEventUsers(idEvent);
    }

    @GetMapping("/getEventClubs")
    public List<String> getEventClubs(@RequestParam Long idEvent) {
        return eventservice.getClubs(idEvent);
    }

    @GetMapping("/getClubEvents")
    public List<Event> getClubEvents(@RequestParam Long idClub) {
        return eventservice.getClubEvents(idClub);
    }

    @PostMapping("/rateEvent")
    public ResponseEntity<String> RateUserEvent(HttpServletRequest request, @RequestParam Long idEvent,@RequestParam Integer Rating) {
        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long idUser = eventservice.showEventUser(username);
            logger.info("userId:{}",idUser);
            logger.info("eventId:{}",idEvent);
            eventservice.RateUserEvent(Rating,idEvent, idUser);
            return ResponseEntity.ok("Event Rated!");

        }
        return ResponseEntity
                .badRequest()
                .body("Error: Bad request Or Already Participated");

    }

    @GetMapping("/userEvRate")
    public Integer UserEventRate(HttpServletRequest request, @RequestParam Long idEvent) {
        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long idUser = eventservice.showEventUser(username);
            return eventservice.UserEventRate(idEvent,idUser);

        }
        return 0;
    }
    @GetMapping("/userEvConf")
    public Boolean UserEventConfirmation(HttpServletRequest request, @RequestParam Long idEvent) {
        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long idUser = eventservice.showEventUser(username);
            return eventservice.UserEventConfirmation(idEvent,idUser);

        }
        return false;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/generateParticipantBadge/{id}/{user}")
    public void generateParticipantBadge(@PathVariable("id") Long idEvent, @PathVariable("user") String username) throws Exception {

        List<String> user = Arrays.asList(eventservice.getUsernameDetails(username).split(",", -1));

        eventservice.confirmUserEvent(idEvent, eventservice.getUsernameId(user.get(0)));
        Event e = eventservice.showEvent(idEvent);
        IemailS.ParticipationConfirmation(user, e, IemailS.GenerateBadge(user, e));
        IemailS.DeleteBadgeFiles(user, e);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countEvByMonth")
    public Integer countEventsByDate(@RequestParam("month") Integer month, @RequestParam("year") Integer year) {
        //SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        //sdformat.parse(startDate);
        return eventservice.countEventsByMonth(month, year);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countAllEvByMonth")
    public List<Integer[]> countAllEventsByDate(@RequestParam Integer year) {
        //SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        //sdformat.parse(startDate);
        return eventservice.countAllEventsByMonth(year);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countEvStatusByYear")
    public List<Object[]> countEventsStatusByYear(@RequestParam Integer year) {
        return eventservice.countEventsStatusByYear(year);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countEvTypeByYear")
    public List<Object[]> countEventsTypeByYear(@RequestParam Integer year) {
        return eventservice.countEventsTypeByYear(year);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/bestEvOfYear")
    public List<Object[]> bestEventsOfTheYear(@RequestParam Integer year) {
        return eventservice.bestEventsOfTheYear(year);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countAllEv")
    public Integer countAllEvents() {
        return eventservice.countAllEvents();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countAllEvPartc")
    public Integer countAllEventsParticipations() {
        return eventservice.countAllEventsParticipations();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countAllConfEv")
    public Integer countAllConfirmedEvents() {
        return eventservice.countAllConfirmedEvents();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countEvParti")
    public Integer countEventParticipations(@RequestParam Long idEvent) {
        return eventservice.countEventParticipations(idEvent);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countEvConfirmed")
    public Integer countEventConfirmed(@RequestParam Long idEvent) {
        return eventservice.countEventConfirmed(idEvent);
    }
}
