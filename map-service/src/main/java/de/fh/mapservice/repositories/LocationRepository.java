package de.fh.mapservice.repositories;

import de.fh.mapservice.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByStationId(Long stationId);
}
