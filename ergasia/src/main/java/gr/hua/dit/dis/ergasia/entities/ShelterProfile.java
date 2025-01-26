package gr.hua.dit.dis.ergasia.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shelter_profiles")
public class ShelterProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "our_pets",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(name = "pets_id")
    )
    private Set<Pet> ourPets = new HashSet<>();

    public ShelterProfile() {
    }

    public ShelterProfile(Long id, User user, Set<Pet> ourPets) {
        this.id = id;
        this.user = user;
        this.ourPets = ourPets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Pet> getOurPets() {
        return ourPets;
    }

    public void setOurPets(Set<Pet> ourPets) {
        this.ourPets = ourPets;
    }
}
