package dsi.esprit.tn.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    @Size(max = 600)
    private String description;
    @Temporal(TemporalType.DATE)
    private Date eventDateStart;
    @Temporal(TemporalType.DATE)
    private Date eventDateEnd;
    @Size(max = 50)
    private String eventTime;
    @NotBlank
    @Size(max = 50)
    private String eventLocation;
    @NotBlank
    @Size(max = 50)
    private String eventMotive;
    @NotBlank
    @Size(max = 20)
    private String type;

    @NotBlank
    @Size(max = 20)
    private String status;
    private int   places;

    @ElementCollection
    List<String> tags = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    Set<eventFile> files;

//    @ManyToMany(mappedBy = "events")
//    private Set<Club> clubs;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
//    private List<Reclamation> reclamations;

//    @ManyToOne(fetch = FetchType.LAZY)
//    Club club;
}
