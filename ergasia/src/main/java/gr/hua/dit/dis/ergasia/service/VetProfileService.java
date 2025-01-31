package gr.hua.dit.dis.ergasia.service;

import gr.hua.dit.dis.ergasia.entities.VetProfile;
import gr.hua.dit.dis.ergasia.repositories.VetProfileRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VetProfileService {

    private final VetProfileRepository vetProfileRepository;

    public VetProfileService(VetProfileRepository vetProfileRepository) {
        this.vetProfileRepository = vetProfileRepository;
    }

    public VetProfile findByUsername(String username) {
        return vetProfileRepository.findByVetUsername(username)
                .orElseThrow(() -> new RuntimeException("Vet not found"));
    }

    public void deleteVetProfile(Long id) {
        VetProfile vetProfile = vetProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vet profile not found"));
        vetProfileRepository.delete(vetProfile);
    }

    public VetProfile updateVetProfile(VetProfile vetProfile) {
        VetProfile existingVetProfile = vetProfileRepository.findById(vetProfile.getId())
                .orElseThrow(() -> new RuntimeException("Vet profile not found"));
        return vetProfileRepository.save(existingVetProfile);
    }

    public List<VetProfile> getAllVetProfiles() {
        return vetProfileRepository.findAll();
    }

    public VetProfile getVetProfileById(Long id) {
        return vetProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vet profile not found"));
    }

    public VetProfile saveVetProfile(VetProfile vetProfile) {
        return vetProfileRepository.save(vetProfile);
    }
}
