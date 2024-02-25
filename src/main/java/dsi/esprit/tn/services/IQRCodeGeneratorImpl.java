package dsi.esprit.tn.services;

import com.google.zxing.WriterException;

import java.io.IOException;

public interface IQRCodeGeneratorImpl {
    String generateQRCodeImage (String text, int width, int height, String filePath) throws WriterException, IOException;
    byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException;

}
