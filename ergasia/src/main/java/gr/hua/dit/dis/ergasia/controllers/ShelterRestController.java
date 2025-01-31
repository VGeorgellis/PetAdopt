package gr.hua.dit.dis.ergasia.controllers;

import gr.hua.dit.dis.ergasia.entities.ShelterProfile;
import gr.hua.dit.dis.ergasia.service.ShelterProfileService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shelters")
public class ShelterRestController {

    private final ShelterProfileService shelterProfileService;

    public ShelterRestController(ShelterProfileService shelterProfileService) {
        this.shelterProfileService = shelterProfileService;
    }

    @GetMapping("")
    public List<ShelterProfile> getShelterProfiles() { return shelterProfileService.getAllShelterProfiles();}

    @GetMapping("/{username}")
    public ShelterProfile getVetProfileByUsername(@PathVariable String username) {
        return shelterProfileService.findByUsername(username);
    }

    @GetMapping("/{id}")
    public ShelterProfile getShelterProfile(@PathVariable Long id) {
        return shelterProfileService.getShelterProfileById(id);
    }

    @PutMapping("/{id}")
    public ShelterProfile updateShelterProfile(@PathVariable Long id, @RequestBody ShelterProfile shelterProfile) {
        shelterProfile.setId(id);
        return shelterProfileService.updateShelterProfile(shelterProfile);
    }

    @DeleteMapping("/{id}")
    public String deleteShelterProfile(@PathVariable Long id) {
        shelterProfileService.deleteShelterProfile(id);
        return "Shelter profile with ID " + id + " was deleted successfully.";
    }

}
