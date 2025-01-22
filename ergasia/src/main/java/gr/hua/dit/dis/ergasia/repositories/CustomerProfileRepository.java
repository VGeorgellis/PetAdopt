package gr.hua.dit.dis.ergasia.repositories;


import gr.hua.dit.dis.ergasia.entities.CustomerProfile;
import gr.hua.dit.dis.ergasia.entities.ShelterProfile;
import gr.hua.dit.dis.ergasia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    // Query to find a customer profile by customer
    @Query("SELECT c FROM CustomerProfile c WHERE c.user = :customer")
    CustomerProfile findByCustomer(@Param("customer") User customer);



}
