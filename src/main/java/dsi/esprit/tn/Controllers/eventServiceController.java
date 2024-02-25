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
        //if(!eventservice.getUsers(idEvent).isEmpty())
        eventservice.deleteEventUsers(idEvent);
        //if(!eventservice.getClubs(idEvent).isEmpty())
        eventservice.deleteEventClubs(idEvent);
        eventservice.deleteEvent(idEvent);
        return ResponseEntity.ok("Event Id:" + idEvent + " is successfully deleted");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateEvent")
    public ResponseEntity<?> updateEvent(@RequestBody Event event) {

        eventservice.updateEvent(event);
        return ResponseEntity.ok("Eventid: " + event.getId() + " is successfully updated");

    }

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

    @PostMapping("/addClubEvent")
    public ResponseEntity<?> addClubEvent(@RequestParam Long idEvent, @RequestBody List<String> clubs) {

        clubs.forEach(e ->
                eventservice.eventClubParticipate(eventservice.getClub(e),idEvent)
        );
        return ResponseEntity.ok("Club(s) participated!");
    }

    @PostMapping("/addUserEvent")
    public ResponseEntity<?> addUserEvent(HttpServletRequest request, @RequestParam Long idEvent) {

        String jwt = jwtUtils.parseJwt(request);
        if (jwt != null) {
            logger.info("Eventjwt: {}", jwt);
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Long idUser=eventservice.getUser(eventservice.showEventUser(username), idEvent);
            logger.info("EventUdetails: {}", username);
            if (idUser==null) {
                logger.info("usereventsCheck: {}", idUser);
                eventservice.userEventParticipate(eventservice.showEventUser(username), idEvent);
                return ResponseEntity.ok("user participated!");
            }
        }
        return ResponseEntity
                .badRequest()
                .body("Error: Bad request Or Already Participated");
    }
    @PostMapping("/addUserEventa")
    public ResponseEntity<?> addUserEventAdmin(@RequestParam Long idEvent,@RequestParam String username) {

            Long idUser=eventservice.getUser(eventservice.showEventUser(username), idEvent);
            if (idUser==null) {
                eventservice.userEventParticipate(eventservice.showEventUser(username), idEvent);
                return ResponseEntity.ok("user participated!");
            }

        return ResponseEntity
                .badRequest()
                .body("Error: Bad request Or Already Participated");
    }

    @PostMapping(path = "/addFile/{id}")
    public eventFile addFile(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) throws IOException {
        return IEFS.addFile(file, id);
    }

    @DeleteMapping("/{id}/deleteFile/{File}")
    public void deleteFile(@PathVariable("File") Long File, @PathVariable("id") long id) throws IOException {
        IEFS.removeFile(File, id);
    }

    @GetMapping("/getFiles/{id}")
    public List<eventFile> EventFiles(@PathVariable("id") Long id) {
        return IEFS.GeteventFiles(id);
    }

    @GetMapping("/getAllFiles")
    public List<eventFile> EventAllFiles() {
        return IEFS.findAll();
    }
    @GetMapping("/getTags")
    public List<String> getEventTags() {
        return eventservice.getTags();
    }
    @GetMapping("/getParticipations")
    public List<Object[]> getEventParticipations(@RequestParam Long idEvent) {
        return eventservice.getEventParticipations(idEvent);
    }
    @GetMapping("/getParticipatables")
    public List<Object[]> getEventParticipatables(@RequestParam Long idEvent) {
        return eventservice.getParticipatableEventUsers(idEvent);
    }
    @GetMapping("/getEventClubs")
    public List<String> getEventClubs(@RequestParam Long idEvent) {
        return eventservice.getClubs(idEvent);
    }

    @GetMapping("/generateParticipantBadge/{id}/{user}")
    public void generateParticipantBadge(@PathVariable("id") Long idEvent,@PathVariable("user") String username) throws Exception {

        List<String> user = Arrays.asList(eventservice.getUsernameDetails(username).split(",", -1));
        logger.info("MailBadge user details: {}", user);
        //logger.info("user de array: {}", Arrays.asList(user.get(0).split(",", -1)));
        logger.info("MailBadge user details: {}", user.get(1));

        Event e = eventservice.showEvent(idEvent);
        IemailS.ParticipationConfirmation(user, e, IemailS.GenerateBadge(user, e));
        //IemailS.DeleteBadgeFiles(user, e);

    }
}
