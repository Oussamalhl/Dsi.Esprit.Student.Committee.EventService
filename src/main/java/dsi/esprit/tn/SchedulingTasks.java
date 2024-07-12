package dsi.esprit.tn;

import dsi.esprit.tn.Controllers.eventServiceController;
import dsi.esprit.tn.Models.Event;
import dsi.esprit.tn.services.IeventServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Component
public class SchedulingTasks {
    @Autowired
    IeventServiceImpl IES;
    private static final Logger logger = LoggerFactory.getLogger(eventServiceController.class);

    @Scheduled(fixedRate = 30000)
    public void checkEventStatus() {
        int i;
        Date dateFormat = new Date();
        List<Event> eventList = IES.showAllEvent();
        for (i = 0; i < eventList.size() - 1; i++) {
            if (eventList.get(i).getStatus()=="Started" && eventList.get(i).getEventDateEnd().after(dateFormat) ) {
                System.out.println(i);
                eventList.get(i).setStatus("Ended");
                System.out.println(eventList.get(i).getStatus());
            } else if (eventList.get(i).getStatus()=="Started" && eventList.get(i).getEventDateStart().equals(dateFormat)){
                eventList.get(i).setStatus("Ongoing");
            }
        }
    }
    @Scheduled(fixedRate=5000)
    public void showTime(){
        Date dateFormat = new Date();
        logger.info("The time is now {}", dateFormat);
    }
}
