package dsi.esprit.tn.Models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class eventFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long fileId;

    String fileName;

    String filePath;
    @Temporal(TemporalType.TIMESTAMP)
    Date uploadDate;

    @ManyToOne
    Event event;
}