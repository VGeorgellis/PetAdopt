package gr.hua.dit.dis.ergasia.repositories;

import gr.hua.dit.dis.ergasia.entities.Appointment;
import gr.hua.dit.dis.ergasia.entities.CustomerProfile;
import gr.hua.dit.dis.ergasia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Query to find appointments by customer
    @Query("SELECT a FROM Appointment a WHERE a.customer = :customer")
    List<Appointment> findByCustomer(@Param("customer") CustomerProfile customer);

}

