package dsi.esprit.tn.services;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FTPService {
    static FTPClient ftp = new FTPClient();
    static String TMP_UPLOAD_FOLDER = "E:\\Esprit DSI\\dsi.esprit.reclamationService\\recamationservice\\tmp\\";
//    static String SERVER_DOMAIN = "localhost";
//    static String SERVER_USERNAME = "esprit";
//    static String SERVER_PASSWORD = "esprit";



    public static String uFileUpload(MultipartFile file,String Type, Long typeId) throws IOException {
        if (file.isEmpty()) {
            System.out.println("Empty File");
            return "Empty File";
        } else {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(TMP_UPLOAD_FOLDER +Type+"\\"+file.getOriginalFilename());
            Files.write(path, bytes);
            System.out.println("File successfully uploaded to local storage : " + file.getOriginalFilename());
//            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
//            int reply;
//            ftp.connect(SERVER_DOMAIN);
//            reply = ftp.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftp.disconnect();
//                System.out.println("Exception in connecting to FTP Server");
//            }
//            ftp.login(SERVER_USERNAME, SERVER_PASSWORD);
//            ftp.setFileType(FTP.BINARY_FILE_TYPE);
//            ftp.enterLocalPassiveMode();
//            try {
//                InputStream input = new FileInputStream(new File(TMP_UPLOAD_FOLDER + file.getOriginalFilename()));
//                System.out.println(input);
//                ftp.makeDirectory("/tmp/"+Type);
//                ftp.makeDirectory("/tmp/"+Type+"/"+typeId.toString());
//                ftp.storeFile("/tmp/"+Type+"/"+typeId.toString()+"/" + file.getOriginalFilename(), input);
//                ftp.logout();
//                ftp.disconnect();
//                System.out.println("File Uploaded !");
//                input.close();
//                Files.delete(path);
//
//            } catch (Exception e) {
//                System.out.println("Error uploading file to remote server");
//            }

        }
        return "";

    }
    public static String uFileremove(String fileName,String Type,Long typeId) throws IOException {
        try {

//            ftp.connect(SERVER_DOMAIN);
//
//            int replyCode = ftp.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(replyCode)) {
//                System.out.println("Connect failed");
//                return "Connect failed";
//            }
//
//            boolean success = ftp.login(SERVER_USERNAME, SERVER_PASSWORD);
//
//            if (!success) {
//                System.out.println("Could not login to the server");
//                return "Could not login to the server";
//            }
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
