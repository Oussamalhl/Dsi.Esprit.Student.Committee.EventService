package dsi.esprit.tn.services;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FTPService {
    static FTPClient ftp = new FTPClient();
    static String TMP_UPLOAD_FOLDER = "E:\\Esprit DSI\\dsi.esprit.reclamationService\\recamationservice\\tmp\\";



    public static String uFileUpload(MultipartFile file,String Type, Long typeId) throws IOException {
        if (file.isEmpty()) {
            System.out.println("Empty File");
            return "Empty File";
        } else {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(TMP_UPLOAD_FOLDER +Type+"\\"+file.getOriginalFilename());
            Files.write(path, bytes);
            System.out.println("File successfully uploaded to local storage : " + file.getOriginalFilename());

        }
        return "";

    }
    public static String uFileremove(String fileName,String Type,Long typeId) throws IOException {
        try {

            String fileToDelete =TMP_UPLOAD_FOLDER +Type+"\\"+fileName;

            boolean deleted = ftp.deleteFile(fileToDelete);
            if (deleted) {
                System.out.println("The file was deleted successfully.");
            } else {
                System.out.println("Could not delete the  file, it may not exist.");
            }

        } catch (IOException ex) {
            System.out.println("Oh no, there was an error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // logs out and disconnects from server
            try {
                if (ftp.isConnected()) {
                    ftp.logout();
                    ftp.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }
}
