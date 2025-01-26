package gr.hua.dit.dis.ergasia.repositories;


import gr.hua.dit.dis.ergasia.entities.ShelterProfile;
import gr.hua.dit.dis.ergasia.entities.User;
import gr.hua.dit.dis.ergasia.entities.VetProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.image.ShortLookupTable;
import java.util.Optional;

@Repository
public interface ShelterProfileRepository extends JpaRepository<ShelterProfile,Long> {

    // Query to find a shelter profile by shelter
    @Query("SELECT s FROM ShelterProfile s WHERE s.user = :shelter")
    ShelterProfile findByShelter(@Param("shelter") User shelter);
    Optional<ShelterProfile> findByShelterUsername(String username);
}
