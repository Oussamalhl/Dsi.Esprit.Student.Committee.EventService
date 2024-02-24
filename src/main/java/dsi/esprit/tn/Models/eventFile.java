package dsi.esprit.tn.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "picByte", length = 65535)
    private byte[] picByte;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    Event event;
}