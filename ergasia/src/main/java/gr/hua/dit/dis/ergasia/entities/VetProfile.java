package gr.hua.dit.dis.ergasia.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vet_profiles")
public class VetProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "animals_vet",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(name = "pets_id")
    )
    private Set<Pet> animalsForCheck = new HashSet<>();

    private Integer animalsForCheckNo;

    public VetProfile() {
    }

    public VetProfile(Long id, User user, Set<Pet> animalsForCheck) {
        this.id = id;
        this.user = user;
        this.animalsForCheck = animalsForCheck;
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

    public Set<Pet> getAnimalsForCheck() {
        return animalsForCheck;
    }

    public void setAnimalsForCheck(Set<Pet> animalsForCheck) {
        this.animalsForCheck = animalsForCheck;
    }

}
