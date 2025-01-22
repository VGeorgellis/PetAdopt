package gr.hua.dit.dis.ergasia.repositories;


import gr.hua.dit.dis.ergasia.entities.Appointment;
import gr.hua.dit.dis.ergasia.entities.Pet;
import gr.hua.dit.dis.ergasia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Query to find the pets of a shelter
    @Query("SELECT p FROM Pet p JOIN p.shelter u WHERE u.user.username = :username")
    List<Pet> findOurPets(@Param("username") String username);

    // Query to find the animals that need to be checked by a vet
    @Query("SELECT p FROM Pet p JOIN p.vet u WHERE u.user.username = :username")
    List<Pet> findAnimalsForCheck(@Param("username") String username);

    Optional<Pet> findById(Long id);

    // Query to find the pets that are waiting for adoption
    @Query("SELECT p FROM Pet p WHERE p.readyForAdoption is null and p.goodHealthForAdoption is true ")
    List<Pet> findPetsWaitingForAdoption();

    // Query to find the pets that are ready for adoption
    @Query("SELECT p FROM Pet p WHERE p.readyForAdoption IS TRUE ")
    List<Pet> findPetsForAdoption();

    // Query to find the animals that have been liked by a customer
    @Query("SELECT p FROM Pet p JOIN p.likedByCustomerProfiles cp WHERE cp.user = :customer")
    List<Pet> findLikedAnimals(@Param("customer") User customer);

    // Query to find the pets that have been booked for an appointment by a customer
    @Query("SELECT p FROM Pet p join Appointment a on a.pet=p where a.customer.user = :customer")
    List<Pet> findByAppointment(@Param("customer") User customer);



}