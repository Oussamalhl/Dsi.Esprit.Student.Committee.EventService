package dsi.esprit.tn.services;

import dsi.esprit.tn.Models.eventFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IeventFileServiceImpl {
    eventFile addFile(MultipartFile file, Long id);
    void removeFile(Long f, Long id);
    List<eventFile> findAll();
    List<eventFile> GeteventFiles(Long id);
}
