package gr.hua.dit.dis.ergasia.repositories;


import gr.hua.dit.dis.ergasia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    @Query("SELECT vet FROM User vet " +
            "JOIN VetProfile VP on vet = VP.user " +
            "where VP.animalsForCheckNo = (SELECT MIN(animalsForCheckNo) FROM VetProfile )" +
            "ORDER BY vet.id ASC " +
            "limit 1")
    Optional<User> findVetWithTheLessAnimals();

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name <> 'ROLE_ADMIN'")
    List<User> findAllUsersExceptAdmins();


}