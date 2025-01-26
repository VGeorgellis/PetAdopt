package gr.hua.dit.dis.ergasia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotEmpty
    @Size(min = 1, max = 50)
    private String name;

    @Column
    @NotEmpty()
    private String species;

    @Column
    @NotEmpty()
    private String sex;

    @Column
    private Boolean goodHealthForAdoption;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "our_pets",
            joinColumns = @JoinColumn(name ="pets_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private ShelterProfile shelter = new ShelterProfile();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "animals_vet",
            joinColumns = @JoinColumn(name ="pets_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private VetProfile vet = new VetProfile();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "liked_animals",
            joinColumns = @JoinColumn(name ="pets_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<CustomerProfile> likedByCustomerProfiles = new HashSet<>();

    @Column
    private Boolean readyForAdoption;

    public Boolean getReadyForAdoption() {
        return readyForAdoption;
    }

    public void setReadyForAdoption(Boolean readyForAdoption) {
        this.readyForAdoption = readyForAdoption;
    }

    public VetProfile getVet() {
        return vet;
    }

    public void setVet(VetProfile vet) {
        this.vet = vet;
    }

    public Pet(Long id, String sex, String species, String name) {
        this.id = id;
        this.sex = sex;
        this.species = species;
        this.name = name;
    }

    public Pet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public ShelterProfile getShelter() {
        return shelter;
    }

    public void setShelter(ShelterProfile shelter) {
        this.shelter = shelter;
    }


    public Boolean getGoodHealthForAdoption() {
        return goodHealthForAdoption;
    }

    public void setGoodHealthForAdoption(Boolean goodHealthForAdoption) {
        this.goodHealthForAdoption = goodHealthForAdoption;
    }

    public Set<CustomerProfile> getLikedByCustomerProfiles() {
        return likedByCustomerProfiles;
    }

}
