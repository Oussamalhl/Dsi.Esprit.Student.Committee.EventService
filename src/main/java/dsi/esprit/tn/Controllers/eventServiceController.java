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

    @GetMapping("/getUser")
    public Long getUser(@RequestParam Long idUser, @RequestParam Long idEvent) {
        return eventservice.getUser(idUser, idEvent);
    }

    //    @GetMapping("/getTargets")
//    public List<String> getTargets(@RequestParam String type) {
//        return eventservice.getTargets(type);
//    }
    @GetMapping("/getClubs")
    public List<String> getClubs() {
        return eventservice.getClubs();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/showallEvents")
    public ResponseEntity<?> showAllEvents() {
        return ResponseEntity.ok(eventservice.showAllEvent());
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteUserEvent")
    public ResponseEntity<?> deleteUserEvent(@RequestParam String username) {
        eventservice.deleteUserEvent(eventservice.getUsernameId(username));
        return ResponseEntity.ok( username +" is no longer participated");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteUserEventm")
    public ResponseEntity<?> deleteUserEventMail(@RequestParam Long eventId,@RequestParam String username) throws Exception {
        List<String> user = Arrays.asList(eventservice.getUsernameDetails(username).split(",", -1));

        eventservice.deleteUserEvent(eventservice.getUsernameId(username));
        IemailS.CancelParticipation(user,eventservice.showEvent(eventId));
        return ResponseEntity.ok( username +"no longer participated and notified by Mail");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateEvent")
    public ResponseEntity<?> updateEvent(@RequestBody Event event) {

        eventservice.updateEvent(event);
        return ResponseEntity.ok("Eventid: " + event.getId() + " is successfully updated");

    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/addEvent")
    public ResponseEntity<?> addEvent(HttpServletRequest request, @RequestBody Event event) {

        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null) {
            logger.info("RECjwt: {}", jwt);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            logger.info("RECdetails: {}", username);
            eventservice.addEvent(event);
            return ResponseEntity.ok(event.toString());


        }
        return ResponseEntity
                .badRequest()
                .body("Error: Bad request");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addClubEvent")
    public ResponseEntity<?> addClubEvent(@RequestParam Long idEvent, @RequestBody List<String> clubs) {

        clubs.forEach(e ->
                eventservice.eventClubParticipate(eventservice.getClub(e), idEvent)
        );
        return ResponseEntity.ok("Club(s) participated!");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/addUserEvent")
    public ResponseEntity<?> addUserEvent(HttpServletRequest request, @RequestParam Long idEvent) {

        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null) {
            logger.info("Eventjwt: {}", jwt);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long idUser = eventservice.getUser(eventservice.showEventUser(username), idEvent);
            logger.info("EventUdetails: {}", username);
            if (idUser == null) {
                logger.info("usereventsCheck: {}", idUser);
                eventservice.userEventParticipate(eventservice.showEventUser(username), idEvent);
                return ResponseEntity.ok("user participated!");
            }
        }
        return ResponseEntity
                .badRequest()
                .body("Error: Bad request Or Already Participated");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addUserEventa")
    public ResponseEntity<?> addUserEventAdmin(@RequestParam Long idEvent, @RequestParam String username) {

        Long idUser = eventservice.getUser(eventservice.showEventUser(username), idEvent);
        if (idUser == null) {
            eventservice.userEventParticipate(eventservice.showEventUser(username), idEvent);
            return ResponseEntity.ok("user participated!");
        }

        return ResponseEntity
                .badRequest()
                .body("Error: Bad request Or Already Participated");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping(path = "/addFile/{id}")
    public eventFile addFile(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) throws IOException {
        return IEFS.addFile(file, id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping("/{id}/deleteFile/{File}")
    public void deleteFile(@PathVariable("File") Long File, @PathVariable("id") long id) throws IOException {
        IEFS.removeFile(File, id);
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/generateParticipantBadge/{id}/{user}")
    public void generateParticipantBadge(@PathVariable("id") Long idEvent, @PathVariable("user") String username) throws Exception {

        List<String> user = Arrays.asList(eventservice.getUsernameDetails(username).split(",", -1));

        eventservice.confirmUserEvent(idEvent,eventservice.getUsernameId(user.get(0)));
        Event e = eventservice.showEvent(idEvent);
        IemailS.ParticipationConfirmation(user, e, IemailS.GenerateBadge(user, e));
        IemailS.DeleteBadgeFiles(user, e);

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countEvByMonth")
    public Integer countEventsByDate(@RequestParam("month") Integer month, @RequestParam("year")Integer year) {
        //SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        //sdformat.parse(startDate);
        return eventservice.countEventsByMonth(month,year);
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
