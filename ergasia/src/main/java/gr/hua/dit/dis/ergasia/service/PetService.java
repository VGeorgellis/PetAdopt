package gr.hua.dit.dis.ergasia.service;

import gr.hua.dit.dis.ergasia.entities.Pet;
import gr.hua.dit.dis.ergasia.entities.ShelterProfile;
import gr.hua.dit.dis.ergasia.entities.User;
import gr.hua.dit.dis.ergasia.entities.VetProfile;
import gr.hua.dit.dis.ergasia.repositories.PetRepository;
import gr.hua.dit.dis.ergasia.repositories.ShelterProfileRepository;
import gr.hua.dit.dis.ergasia.repositories.UserRepository;
import gr.hua.dit.dis.ergasia.repositories.VetProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PetService {

    private VetProfileRepository vetProfileRepository;

    private ShelterProfileRepository shelterProfileRepository;

    private PetRepository petRepository;

    private UserRepository userRepository;


    public PetService(PetRepository petRepository,VetProfileRepository vetProfileRepository,ShelterProfileRepository shelterProfileRepository ,UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.vetProfileRepository = vetProfileRepository;
        this.shelterProfileRepository = shelterProfileRepository;
    }

    @Transactional
    public Long savePet(Pet pet, User shelter, User vet) {

        VetProfile vetProfile = vetProfileRepository.findByVet(vet);

        ShelterProfile shelterProfile = shelterProfileRepository.findByShelter(shelter);

        pet.setVet(vetProfile);
        pet.setShelter(shelterProfile);
        petRepository.save(pet);

        vetProfile.getAnimalsForCheck().add(pet);
        vetProfileRepository.save(vetProfile);

        shelterProfile.getOurPets().add(pet);
        shelterProfileRepository.save(shelterProfile);


        return pet.getId();
    }

    @Transactional
    public Object getOurPets(User shelter) {
        return petRepository.findOurPets(shelter.getUsername());
    }

    @Transactional
    public Object getAnimalsForCheck(User vet) {return petRepository.findAnimalsForCheck(vet.getUsername());}


}
