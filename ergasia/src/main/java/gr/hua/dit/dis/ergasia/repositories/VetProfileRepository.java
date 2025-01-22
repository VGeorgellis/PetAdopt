package gr.hua.dit.dis.ergasia.repositories;

import gr.hua.dit.dis.ergasia.entities.User;
import gr.hua.dit.dis.ergasia.entities.VetProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VetProfileRepository extends JpaRepository<VetProfile,Long> {

    // Query to find a vet profile by vet
    @Query("SELECT v FROM VetProfile v WHERE v.user = :vet")
    VetProfile findByVet(@Param("vet") User vet);
}
