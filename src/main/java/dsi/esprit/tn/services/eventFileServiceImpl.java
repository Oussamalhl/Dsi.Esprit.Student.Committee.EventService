package dsi.esprit.tn.services;

import dsi.esprit.tn.Models.Event;
import dsi.esprit.tn.Models.eventFile;
import dsi.esprit.tn.repository.eventFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class eventFileServiceImpl implements IeventFileServiceImpl{

    @Autowired
    IeventServiceImpl IES;
    @Autowired
    eventFileRepository fr;

    public eventFile addFile(MultipartFile file, Long id) {
        Event event = IES.showEvent(id);
        try {

            //FTPService.uFileUpload(file, "event", id);


            eventFile f = new eventFile();
            f.setUploadDate(new Date());
            f.setFileName(file.getOriginalFilename());
            //f.setFilePath(TMP_UPLOAD_FOLDER + "event" + "\\" + file.getOriginalFilename());
            System.out.println("original file size: " + file.getBytes().length);
            f.setPicByte(file.getBytes());
            f.setEvent(event);
            return fr.save(f);
        } catch (Exception e) {
            System.out.println("Error Uploading file");
        }
        return null;
    }

    // compress the image bytes before storing it in the database
//    public static byte[] compressBytes(byte[] data) {
//        Deflater deflater = new Deflater();
//        deflater.setInput(data);
//        deflater.finish();
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        while (!deflater.finished()) {
//            int count = deflater.deflate(buffer);
//            outputStream.write(buffer, 0, count);
//        }
//        try {
//            outputStream.close();
//        } catch (IOException e) {
//        }
//        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
//
//        return outputStream.toByteArray();
//    }

    // uncompress the image bytes before returning it to the angular application
//    public byte[] decompressBytes(byte[] data) {
//        Inflater inflater = new Inflater();
//        inflater.setInput(data);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
//        byte[] buffer = new byte[1024];
//        try {
//            while (!inflater.finished()) {
//                int count = inflater.inflate(buffer);
//                outputStream.write(buffer, 0, count);
//            }
//            outputStream.close();
//        } catch (IOException | DataFormatException ioe) {
//        }
//        return outputStream.toByteArray();
//    }


    public void removeFile(Long f) {
        eventFile file = fr.findById(f).orElse(null);
        if (!(file == null)) {
            try {
                //FTPService.uFileremove(file.getFileName(), "reclamation", file.getEvent().getId());
                fr.delete(file);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }


    public List<eventFile> findAll() {
        return fr.findAll();

    }

    public List<eventFile> GeteventFiles(Long id) {
        List<eventFile> list = new ArrayList<eventFile>();
        fr.findAll().forEach(f -> {
            if (f.getEvent().getId() == id)
                list.add(f);
        });
        return list;
    }
}
