package dsi.esprit.tn.repository;

import dsi.esprit.tn.Models.eventFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface eventFileRepository extends JpaRepository<eventFile, Long> {
}
