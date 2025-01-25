package gr.hua.dit.dis.ergasia.controllers;

import gr.hua.dit.dis.ergasia.entities.*;
import gr.hua.dit.dis.ergasia.repositories.*;
import gr.hua.dit.dis.ergasia.service.PetService;
import gr.hua.dit.dis.ergasia.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetRestController {

    private final PetRepository petRepository;
    private final VetProfileRepository vetProfileRepository;
    private final ShelterProfileRepository shelterProfileRepository;
    private final UserService userService;
    private final PetService petService;
    private final UserRepository userRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final AppointmentRepository appointmentRepository;

    public PetRestController(UserService userService, AppointmentRepository appointmentRepository, PetService petService,
                             CustomerProfileRepository customerProfileRepository, UserRepository userRepository,
                             PetRepository petRepository, VetProfileRepository vetProfileRepository, ShelterProfileRepository shelterProfileRepository) {
        this.userService = userService;
        this.petService = petService;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.vetProfileRepository = vetProfileRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.appointmentRepository = appointmentRepository;
        this.shelterProfileRepository = shelterProfileRepository;
    }

    @GetMapping("/our")
    public List<Pet> getOurPets() {
        User user = getAuthenticatedUser();
        return (List<Pet>) petService.getOurPets(user);
    }

    @PostMapping("/add")
    public String addPet(@RequestBody Pet pet) {
        User shelter = getAuthenticatedUser();
        User vet = userRepository.findVetWithTheLessAnimals()
                .orElseThrow(() -> new RuntimeException("Vet not found"));

        petService.savePet(pet, shelter, vet);
        return "Pet added successfully!";
    }

    @GetMapping("/check")
    public List<Pet> getPetsForCheck() {
        User vet = getAuthenticatedUser();
        return (List<Pet>) petService.getAnimalsForCheck(vet);
    }

    @PutMapping("/checked/healthy/{id}")
    public String checkPetHealthy(@PathVariable Long id) {
        User vet = getAuthenticatedUser();
        Pet pet = getPetById(id);

        VetProfile vetProfile = vetProfileRepository.findByVet(vet);
        vetProfile.getAnimalsForCheck().remove(pet);
        vetProfile.setAnimalsForCheckNo(vetProfile.getAnimalsForCheckNo() - 1);
        vetProfileRepository.save(vetProfile);

        pet.setGoodHealthForAdoption(true);
        petRepository.save(pet);

        return "Pet marked as healthy";
    }

    @PutMapping("/checked/not-healthy/{id}")
    public String checkPetNotHealthy(@PathVariable Long id) {
        User vet = getAuthenticatedUser();
        Pet pet = getPetById(id);

        VetProfile vetProfile = vetProfileRepository.findByVet(vet);
        vetProfile.getAnimalsForCheck().remove(pet);
        vetProfile.setAnimalsForCheckNo(vetProfile.getAnimalsForCheckNo() - 1);
        vetProfileRepository.save(vetProfile);

        pet.setGoodHealthForAdoption(false);
        petRepository.save(pet);

        return "Pet marked as not healthy";
    }

    @PutMapping("/ready/{id}")
    public String markPetReady(@PathVariable Long id) {
        Pet pet = getPetById(id);
        pet.setReadyForAdoption(true);
        petRepository.save(pet);

        return "Pet marked as ready for adoption";
    }

    @PutMapping("/not-ready/{id}")
    public String markPetNotReady(@PathVariable Long id) {
        Pet pet = getPetById(id);
        pet.setReadyForAdoption(false);
        petRepository.save(pet);

        return "Pet marked as not ready for adoption";
    }

    @PostMapping("/like/{id}")
    public String likePet(@PathVariable Long id) {
        User customer = getAuthenticatedUser();
        Pet pet = getPetById(id);

        CustomerProfile customerProfile = customerProfileRepository.findByCustomer(customer);
        customerProfile.getLiked_animals().add(pet);
        pet.getLikedByCustomerProfiles().add(customerProfile);
        petRepository.save(pet);
        customerProfileRepository.save(customerProfile);

        return "Pet liked successfully!";
    }

    @PostMapping("/unlike/{id}")
    public String unlikePet(@PathVariable Long id) {
        User customer = getAuthenticatedUser();
        Pet pet = getPetById(id);

        CustomerProfile customerProfile = customerProfileRepository.findByCustomer(customer);
        customerProfile.getLiked_animals().remove(pet);
        pet.getLikedByCustomerProfiles().remove(customerProfile);
        petRepository.save(pet);
        customerProfileRepository.save(customerProfile);

        return "Pet unliked successfully!";
    }

    @PostMapping("/appointment")
    public String bookAppointment(@RequestParam("date") String date, @RequestParam("petId") Long petId) {
        User customer = getAuthenticatedUser();
        CustomerProfile customerProfile = customerProfileRepository.findByCustomer(customer);
        Pet pet = getPetById(petId);

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        appointment.setCustomer(customerProfile);
        appointment.setPet(pet);
        appointment.setShelter(pet.getShelter());
        appointmentRepository.save(appointment);

        return "Appointment booked successfully!";
    }

    @DeleteMapping("/appointment/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentRepository.deleteById(id);
        return "Appointment canceled successfully!";
    }

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Pet getPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet not found"));
    }
}

