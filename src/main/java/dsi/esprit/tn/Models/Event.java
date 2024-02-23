package dsi.esprit.tn.Models;

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
    @Size(max = 20)
    private String name;
    @NotBlank
    @Size(max = 500)
    private String description;
    @NotBlank
    @Temporal(TemporalType.DATE)
    private Date eventDateStart;
    @NotBlank
    @Temporal(TemporalType.DATE)
    private Date eventDateEnd;
    @NotBlank
    @Size(max = 20)
    private String eventTime;
    @NotBlank
    @Size(max = 20)
    private String eventLocation;
    @NotBlank
    @Size(max = 20)
    private String eventMotive;
    @NotBlank
    @Size(max = 20)
    private String type;

    private Boolean status;
    private int   places;
    @ElementCollection
    List<String> tags = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    Set<eventFile> files;

    @ManyToMany(mappedBy = "events")
    private Set<Club> clubs;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
//    private List<Reclamation> reclamations;

//    @ManyToOne(fetch = FetchType.LAZY)
//    Club club;
}
