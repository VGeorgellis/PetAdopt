package gr.hua.dit.dis.ergasia.controllers;

import gr.hua.dit.dis.ergasia.entities.*;
import gr.hua.dit.dis.ergasia.repositories.*;
import gr.hua.dit.dis.ergasia.service.PetService;
import gr.hua.dit.dis.ergasia.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PetController {


    private PetRepository petRepository;

    private VetProfileRepository vetProfileRepository;

    private ShelterProfileRepository shelterProfileRepository;

    private UserService userService;

    private PetService petService;

    private UserRepository userRepository;

    private CustomerProfileRepository customerProfileRepository;
    private  AppointmentRepository appointmentRepository;


    public PetController(UserService userService, AppointmentRepository appointmentRepository,PetService petService, CustomerProfileRepository customerProfileRepository,UserRepository userRepository, PetRepository petRepository, VetProfileRepository vetProfileRepository) {
        this.userService = userService;
        this.petService = petService;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.vetProfileRepository = vetProfileRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/ourPets")
    public String showPets(Model model){
        // Get the email of the logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Add the user's pets to the model
        model.addAttribute("pets", petService.getOurPets(user));

        return "pets/ourPets";
    }

    @GetMapping("/addPet")
    public String addPet(Model model) {
        // Create a new pet
        Pet pet = new Pet();
        model.addAttribute("pet", pet);

        return "pets/addPet";
    }

    @PostMapping("/savePet")
    public String savePet(@ModelAttribute("pet") Pet pet, Model model) {
        // Get the email of the logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        User shelter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the vet with the fewest animals to check
        User vet=userRepository.findVetWithTheLessAnimals()
                .orElseThrow(() -> new RuntimeException("Vet not found"));




        // Save the pet
        petService.savePet(pet, shelter, vet);

        model.addAttribute("msg", "Pet added successfully!");
        return "index";
    }

    @GetMapping("/checkPets")
    public String checkPets(Model model) {
        // Get the email of the logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Add the animals that need to be checked by the vet to the model
        model.addAttribute("pets", petService.getAnimalsForCheck(user));

        return "vets/checkPets";
    }

    @GetMapping("/pet/checked/healthy/{pet_id}")
    public String checkPetHealthy(@PathVariable Long pet_id, Model model) {
        // Get the email of the logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        }
        else {
            email = principal.toString();
        }

        User vet = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = (Pet) petRepository.findById(pet_id).orElseThrow(() -> new RuntimeException("Pet not found"));

        VetProfile vetProfile = vetProfileRepository.findByVet(vet);


        // Remove the animal from the list of animals to be checked
        vetProfile.getAnimalsForCheck().remove(pet);
        vetProfile.setAnimalsForCheckNo(vetProfile.getAnimalsForCheckNo()-1);


        vetProfileRepository.save(vetProfile);
        // Set the animal as healthy for adoption
        pet.setGoodHealthForAdoption(Boolean.TRUE);

        petRepository.save(pet);
        // Add the animals that need to be checked by the vet to the model
        model.addAttribute("pets", petService.getAnimalsForCheck(vet));

        return "vets/checkPets";

    }

    @GetMapping("/pet/checked/notHealthy/{pet_id}")
    public String checkPetNotHealthy(@PathVariable Long pet_id, Model model) {
        // Get the email of the logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        }
        else {
            email = principal.toString();
        }

        User vet = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = (Pet) petRepository.findById(pet_id).orElseThrow(() -> new RuntimeException("Pet not found"));


        VetProfile vetProfile = vetProfileRepository.findByVet(vet);
        // Remove the animal from the list of animals to be checked
        vetProfile.getAnimalsForCheck().remove(pet);
        vetProfile.setAnimalsForCheckNo(vetProfile.getAnimalsForCheckNo()-1);

        vetProfileRepository.save(vetProfile);
        // Set the animal as not healthy for adoption
        pet.setGoodHealthForAdoption(Boolean.FALSE);



        petRepository.save(pet);

        // Add the animals that need to be checked by the vet to the model
        model.addAttribute("pets", petService.getAnimalsForCheck(vet));

        return "vets/checkPets";


    }

    @GetMapping("/pet/ready/{id}")
    public String petIsReady(@PathVariable Long id, Model model) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet not found"));
        // Set the animal as ready for adoption
        pet.setReadyForAdoption(Boolean.TRUE);
        petRepository.save(pet);

        // Add the animals waiting for adoption to the model
        model.addAttribute("pets", petRepository.findPetsWaitingForAdoption());

        return "admins/petsWaitingForAdoption";

    }

    @GetMapping("/pet/notReady/{id}")
    public String petIsNotReady(@PathVariable Long id, Model model) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet not found"));
        // Set the animal as not ready for adoption
        pet.setReadyForAdoption(Boolean.FALSE);
        petRepository.save(pet);

        // Add the animals waiting for adoption to the model
        model.addAttribute("pets", petRepository.findPetsWaitingForAdoption());

        return "admins/petsWaitingForAdoption";

    }

    @GetMapping("/petsWaitingForAdoption")
    public String petsWaitingForAdoption(Model model) {
        // Add the animals waiting for adoption to the model
        model.addAttribute("pets", petRepository.findPetsWaitingForAdoption());
        return "admins/petsWaitingForAdoption";
    }

    @GetMapping("/petsForAdoption")
    public String petsForAdoption(Model model) {
        // Get the email of the logged-in user

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CustomerProfile customerProfile = customerProfileRepository.findByCustomer(customer);

        List<Appointment> appointments = appointmentRepository.findByCustomer(customerProfile);


        // Add the customer's appointments, the animals they have booked an appointment with,
        // the animals that are available for adoption, and the animals the customer has liked to the model
        model.addAttribute("appointments", appointments);
        model.addAttribute("petsAppointments", petRepository.findByAppointment(customer));
        model.addAttribute("pets", petRepository.findPetsForAdoption());
        model.addAttribute("likedPets", petRepository.findLikedAnimals(customer));
        return "customers/petsForAdoption";
    }

    @GetMapping("/pet/liked/{id}")
    public String petLiked(@PathVariable Long id, Model model) {
        // Get the email of the logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        }
        else {
            email = principal.toString();
        }

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet not found"));


        CustomerProfile customerProfile = customerProfileRepository.findByCustomer(customer);
        // Add the animal to the customer's liked animals
        customerProfile.getLiked_animals().add(pet);
        petRepository.save(pet);
        pet.getLikedByCustomerProfiles().add(customerProfile);
        customerProfileRepository.save(customerProfile);

        // Add the customer's appointments, the animals they have booked an appointment with,
        // the animals that are available for adoption, and the animals the customer has liked to the model
        model.addAttribute("appointments", appointmentRepository.findByCustomer(customerProfile));
        model.addAttribute("petsAppointments", petRepository.findByAppointment(customer));
        model.addAttribute("pets", petRepository.findPetsForAdoption());
        model.addAttribute("likedPets", petRepository.findLikedAnimals(customer));


        return "customers/petsForAdoption";

    }

    @GetMapping("/pet/unliked/{id}")
    public String petUnLiked(@PathVariable Long id, Model model) {
        // Get the email of the logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        }
        else {
            email = principal.toString();
        }

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = petRepository.findById(id).orElseThrow(() -> new RuntimeException("Pet not found"));


        CustomerProfile customerProfile = customerProfileRepository.findByCustomer(customer);
        // Remove the animal from the customer's liked animals
        customerProfile.getLiked_animals().remove(pet);
        petRepository.save(pet);
        pet.getLikedByCustomerProfiles().remove(customerProfile);
        customerProfileRepository.save(customerProfile);

        // Add the customer's appointments, the animals they have booked an appointment with,
        // the animals that are available for adoption, and the animals the customer has liked to the model
        model.addAttribute("appointments", appointmentRepository.findByCustomer(customerProfile));
        model.addAttribute("petsAppointments", petRepository.findByAppointment(customer));
        model.addAttribute("pets", petRepository.findPetsForAdoption());
        model.addAttribute("likedPets", petRepository.findLikedAnimals(customer));


        return "customers/petsForAdoption";

    }

    @PostMapping("/book-appointment")
    public String bookAppointment(@RequestParam("date") String date, @RequestParam("petId") Long petId, Model model) {
        // Get the email of the logged-in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CustomerProfile customerProfile = customerProfileRepository.findByCustomer(customer);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        Appointment appointment = new Appointment();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Set the appointment date
        appointment.setAppointmentDate(LocalDate.parse(date, formatter));

        appointment.setCustomer(customerProfile);
        appointment.setPet(pet);
        appointment.setShelter(pet.getShelter());
        // Save the appointment
        appointmentRepository.save(appointment);


        // Add the customer's appointments, the animals they have booked an appointment with,
        // the animals that are available for adoption, and the animals the customer has liked to the model
        model.addAttribute("appointments", appointmentRepository.findByCustomer(customerProfile));
        model.addAttribute("msg", "Appointment booked successfully!");
        model.addAttribute("petsAppointments", petRepository.findByAppointment(customer));
        model.addAttribute("pets", petRepository.findPetsForAdoption());
        model.addAttribute("likedPets", petRepository.findLikedAnimals(customer));


        return "redirect:/petsForAdoption";
    }

    @PostMapping("/cancel-appointment/{id}")
    public String cancelAppointment(@PathVariable Long id, Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CustomerProfile customerProfile = customerProfileRepository.findByCustomer(customer);



        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointmentRepository.delete(appointment);

        model.addAttribute("appointments", appointmentRepository.findByCustomer(customerProfile));
        model.addAttribute("msg", "Appointment booked successfully!");
        model.addAttribute("petsAppointments", petRepository.findByAppointment(customer));
        model.addAttribute("pets", petRepository.findPetsForAdoption());
        model.addAttribute("likedPets", petRepository.findLikedAnimals(customer));


        return "customers/petsForAdoption";
    }

}
