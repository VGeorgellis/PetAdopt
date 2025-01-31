package gr.hua.dit.dis.ergasia.controllers;

import gr.hua.dit.dis.ergasia.entities.VetProfile;
import gr.hua.dit.dis.ergasia.service.VetProfileService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vets")
public class VetProfileRestController {

    private final VetProfileService vetProfileService;

    public VetProfileRestController(VetProfileService VetProfileService) {
        this.vetProfileService = VetProfileService;
    }

    @GetMapping("")
    public List<VetProfile> getVetProfiles() {
        return vetProfileService.getAllVetProfiles();
    }

    @GetMapping("/{id}")
    public VetProfile getVetProfile(@PathVariable Long id) {
        return vetProfileService.getVetProfileById(id);
    }

    @PutMapping("/{id}")
    public VetProfile updateVetProfile(@PathVariable Long id, @RequestBody VetProfile vetProfile) {
        vetProfile.setId(id);
        return vetProfileService.updateVetProfile(vetProfile);
    }

    @DeleteMapping("/{id}")
    public String deleteVetProfile(@PathVariable Long id) {
        vetProfileService.deleteVetProfile(id);
        return "Vet profile with ID " + id + " was deleted successfully.";
    }

    @GetMapping("/{username}")
    public VetProfile getVetProfileByUsername(@PathVariable String username) {
        return vetProfileService.findByUsername(username);
    }
}
