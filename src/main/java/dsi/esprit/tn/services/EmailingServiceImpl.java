package dsi.esprit.tn.services;

import com.itextpdf.kernel.pdf.PdfReader;
import dsi.esprit.tn.Models.Event;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.google.zxing.WriterException;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

@Service
public class EmailingServiceImpl implements IEmailingServiceImpl {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    IQRCodeGeneratorImpl IQRS;
    static String TMP_UPLOAD_FOLDER ="E:\\Esprit DSI\\dsi.esprit.eventService\\eventservice\\tmp\\";


    public void ParticipationConfirmation(List<String> user, Event e, String pathPDF) throws Exception {

        // Mail with Badge attachement
        File f = new File(pathPDF);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        MimeMessage mm = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mm, true);
        mimeMessageHelper.setFrom("mail"+user.get(1));
        mimeMessageHelper.setTo(user.get(1));
        mimeMessageHelper.setText("Hello " + user.get(2) + " " + user.get(3) + "," + "\n \n"
                + "Your participation in the following event has been confirmed: \n" + "Event: " + e.getName()
                + "- " + e.getDescription() + "\nLocation: " + e.getEventLocation() + "\nDate: \n" + "	Starts At :"
                + dateFormat.format(e.getEventDateStart()) + "\n" + "	Ends At: "
                + dateFormat.format(e.getEventDateEnd()) + "\n" + "Time: " + e.getEventTime() + "\nEvent Link: "
                + "https://www.dsi.esprit.tn/Events/" + e.getName()+ ".\n"
                + "You will find your access badge attached below. Please note that it is required to access the event.\n"
                + "Thank you for your participation. We look forward to hearing your feedback on the event after attending.\n\n"
                + "Regards,\n" + "The DSI ESPRIT Team");
        mimeMessageHelper.setSubject("Participation confirmation");
        mimeMessageHelper.addAttachment("Badge", f);

        emailSender.send(mm);

    }

    // pdf generation for Participant badge
    public String GenerateBadge(List<String> user, Event e) throws WriterException, IOException, Exception {

        String pathPDF =TMP_UPLOAD_FOLDER+"event\\"+ user.get(1) + user.get(2) + ".pdf";
        String script1 = "   Participant: ".concat(user.get(0)) + "\n\n";
        String script2 = "   Event: " + e.getName();
        String script3 = "PARTICIPANT";
        Text text1 = new Text(script1);
        Text text2 = new Text(script2);
        Text text3 = new Text(script3);
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        DeviceRgb blue = new DeviceRgb(11, 170, 192);
        Paragraph p = new Paragraph();
        Paragraph p2 = new Paragraph();
        p.setFont(font);
        p.setBold();
        p.setFontSize(25);
        p.add(text1);
        p.add(text2);
        p2.setFont(font);
        p2.setBold();
        p2.setFontSize(40);
        p2.setFontColor(blue);
        p2.add(text3);
        byte[] qrimg = IQRS.getQRCodeImage("https://www.dsi.esprit.tn/Events/" + e.getName(), 250,
                250);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pathPDF));
        pdfDoc.addNewPage();
        Document document = new Document(pdfDoc);

        float width = pdfDoc.getDefaultPageSize().getWidth();
        float height = pdfDoc.getDefaultPageSize().getHeight();
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(1));
        canvas.rectangle(20, 20, width - 40, height - 40);
        canvas.stroke();


        Path path = Paths.get(TMP_UPLOAD_FOLDER+"logo-esprit.png");

        ImageData data = ImageDataFactory.create(Files.readAllBytes(path));



        Image image = new Image(data);
        image.setFixedPosition(112f, 450f);
        document.add(image);

        data = ImageDataFactory.create(qrimg);
        image = new Image(data);
        // set Absolute Position
        image.setFixedPosition(175f, 200f);
        // set Scaling
        image.setAutoScaleHeight(false);
        image.setAutoScaleWidth(false);
        // set Rotation

        document.add(image);
        document.showTextAligned(p2, 165, 170, null);
        document.showTextAligned(p, 40, 40, null);

        document.close();

        return (pathPDF);

    }


    public void DeleteBadgeFiles(List<String> user, Event e) {
        String pathPDF =TMP_UPLOAD_FOLDER+"event\\"+ user.get(1) + user.get(2) + ".pdf";
        File f = new File(pathPDF);
        try {
            if (f.delete())
            {
                System.out.println(f.getName() + " deleted");
            } else {
                System.out.println("failed");
            }
        } catch (

                Exception exe) {
            exe.printStackTrace();
        }

    }


    public void CancelParticipation(List<String> user,Event e) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        MimeMessage mm = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mm, true);
        mimeMessageHelper.setFrom(user.get(1));
        mimeMessageHelper.setTo(user.get(1));
        mimeMessageHelper.setText("Hello " + user.get(2) + " " + user.get(3) + "," + "\n \n"
                + "You are no longer a participant in the following event: \n" + "Event: "
                + e.getName() + "- " + e.getDescription() + "\nLocation: "
                + e.getEventLocation() + "\nDate: \n" + "	Starts At :"
                + dateFormat.format(e.getEventDateStart()) + "\n" + "	Ends At: "
                + dateFormat.format(e.getEventDateEnd()) + "\n" + "Time: "
                + e.getEventTime() + "\nEvent Link: " + "https://www.dsi.esprit.tn/Events/"
                + e.getName() + ".\n"
                + "If you think there has been an error, please contact us via our reclamations section.\n\n"
                + "Regards,\n" + "The DSI ESPRIT Team");
        mimeMessageHelper.setSubject("Participation cancelled");

        emailSender.send(mm);

    }


    public void eventUpdate(List<String> user, Event e,String pathPDF) throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        MimeMessage mm = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mm, true);
        mimeMessageHelper.setFrom(user.get(1));
        mimeMessageHelper.setTo(user.get(1));
        mimeMessageHelper.setText("Hello " + user.get(2) + " " + user.get(3) + "," + "\n \n"
                + "Updates/changes have been added to an event you are participating in. Please note the new changes: \n" + "Event: " + e.getName()
                + "- " + e.getDescription() + "\n"+"Location"+": " + e.getEventLocation() + "\nDate: \n" + "	Starts At :"
                + dateFormat.format(e.getEventDateStart()) + "\n" + "	Ends At: "
                + dateFormat.format(e.getEventDateEnd()) + "\n" + "Time: " + e.getEventTime() + "\nEvent Link: "
                + "https://www.dsi.esprit.tn/Events/" + e.getName() + ".\n"
                +"We apologize for any inconvience. If you have any questions or concerns, please contact us through our complaints section.\n"
                + "Thank you for your participation. We look forward to hearing your feedback on the event after attending.\n\n"
                + "Regards,\n" + "The DSI ESPRIT Team");
        mimeMessageHelper.setSubject("Event Updates");
        emailSender.send(mm);

    }
    public void Personalized(List<String> user,String subject, String body) throws Exception {
        File f = new File(TMP_UPLOAD_FOLDER+"logo-esprit.png");
        MimeMessage mm = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mm, true);
        mimeMessageHelper.setFrom(user.get(1));
        mimeMessageHelper.setTo(user.get(1));
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        mimeMessageHelper.addInline("The DSI ESPRIT Team", f);

        emailSender.send(mm);

    }
}
