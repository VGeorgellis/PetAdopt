package gr.hua.dit.dis.ergasia.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @ManyToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private ShelterProfile shelter;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerProfile customer;

    public Appointment() {
    }

    public Appointment(LocalDate appointmentDate, ShelterProfile shelter, Pet pet, CustomerProfile customer) {
        this.appointmentDate = appointmentDate;
        this.shelter = shelter;
        this.pet = pet;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public ShelterProfile getShelter() {
        return shelter;
    }

    public void setShelter(ShelterProfile shelter) {
        this.shelter = shelter;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public CustomerProfile getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerProfile customer) {
        this.customer = customer;
    }
}
