package gr.hua.dit.dis.ergasia.service;

import gr.hua.dit.dis.ergasia.entities.ShelterProfile;
import gr.hua.dit.dis.ergasia.entities.VetProfile;
import gr.hua.dit.dis.ergasia.repositories.ShelterProfileRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ShelterProfileService {

    private final ShelterProfileRepository shelterProfileRepository;

    public ShelterProfileService(ShelterProfileRepository shelterProfileRepository) {
        this.shelterProfileRepository = shelterProfileRepository;
    }

    public List<ShelterProfile> getAllShelterProfiles() {
        return shelterProfileRepository.findAll();
    }

    public ShelterProfile findByUsername(String username) {
        return shelterProfileRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Shelter not found"));
    }

    public void deleteShelterProfile(Long id) {
        ShelterProfile shelterProfile = shelterProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shelter profile not found"));
        shelterProfileRepository.delete(shelterProfile);
    }

    public ShelterProfile updateShelterProfile(ShelterProfile shelterProfile) {
        ShelterProfile existingShelterProfile = shelterProfileRepository.findById(shelterProfile.getId())
                .orElseThrow(() -> new RuntimeException("Shelter profile not found"));
        return shelterProfileRepository.save(existingShelterProfile);
    }

    public ShelterProfile getShelterProfileById(Long id) {
        return shelterProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shelter profile not found"));
    }

    public ShelterProfile saveShelterProfile(ShelterProfile shelterProfile) {
        return shelterProfileRepository.save(shelterProfile);
    }
}
