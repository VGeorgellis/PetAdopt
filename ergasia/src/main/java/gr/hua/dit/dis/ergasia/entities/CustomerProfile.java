package gr.hua.dit.dis.ergasia.entities;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="customer_profiles")
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;





    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "liked_animals",
            joinColumns = @JoinColumn(name ="user_id"),
            inverseJoinColumns = @JoinColumn(name = "pets_id")
    )
    private Set<Pet> liked_animals = new HashSet<>();





    public CustomerProfile(Integer id, User user, Set<Pet> liked_animals) {
        this.id = id;
        this.user = user;
        this.liked_animals = liked_animals;
    }

    public CustomerProfile() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Pet> getLiked_animals() {
        return liked_animals;
    }

    public void setLiked_animals(Set<Pet> liked_animals) {
        this.liked_animals = liked_animals;
    }
}
