package dsi.esprit.tn.services;

import com.google.zxing.WriterException;
import com.itextpdf.layout.Document;
import dsi.esprit.tn.Models.Event;

import java.io.IOException;
import java.util.List;

public interface IEmailingServiceImpl {
    void ParticipationConfirmation(List<String> user, Event e, Document document) throws Exception;
    Document GenerateBadge(List<String> user, Event e) throws WriterException, IOException, Exception;
    void DeleteBadgeFiles(List<String> user, Event e);
    void CancelParticipation(List<String> user,Event e) throws Exception;
    void eventUpdate(List<String> user, Event e,String pathPDF) throws Exception;
    void Personalized(List<String> user,String subject, String body) throws Exception;
}
