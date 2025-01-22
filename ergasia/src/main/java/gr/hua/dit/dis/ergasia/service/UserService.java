package gr.hua.dit.dis.ergasia.service;

import gr.hua.dit.dis.ergasia.entities.*;
import gr.hua.dit.dis.ergasia.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private VetProfileRepository vetProfileRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private ShelterProfileRepository shelterProfileRepository;

    private CustomerProfileRepository customerProfileRepository;


    public UserService(UserRepository userRepository, CustomerProfileRepository customerProfileRepository,ShelterProfileRepository shelterProfileRepository,RoleRepository roleRepository, VetProfileRepository vetProfileRepository,BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.vetProfileRepository = vetProfileRepository;
        this.shelterProfileRepository = shelterProfileRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Integer saveUser(User user) {
        String passwd= user.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        user.setPassword(encodedPassword);



        Role role = roleRepository.findByName(user.getSelectedRole())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        if(user.getSelectedRole().equals("ROLE_VET") )
        {
            VetProfile vetProfile= new VetProfile();

            vetProfile.setAnimalsForCheckNo(0);

            vetProfile.setUser(user);

            vetProfileRepository.save(vetProfile);
        } else if(user.getSelectedRole().equals("ROLE_SHELTER") ){

            ShelterProfile shelterProfile = new ShelterProfile();

            shelterProfile.setUser(user);

            shelterProfileRepository.save(shelterProfile);

        } else if(user.getSelectedRole().equals("ROLE_USER") ){
            CustomerProfile customerProfile = new CustomerProfile();
            customerProfile.setUser(user);
            customerProfileRepository.save(customerProfile);
        }

        user = userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public Integer updateUser(User user) {
        Role role = roleRepository.findByName(user.getSelectedRole())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);


        user = userRepository.save(user);
        return user.getId();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);

        if(opt.isEmpty())
            throw new UsernameNotFoundException("User with email: " +username +" not found !");
        else {
            User user = opt.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.getRoles()
                            .stream()
                            .map(role-> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toSet())
            );
        }
    }

    @Transactional
    public Object getUsers() {
        // Return all users except admins
        return userRepository.findAllUsersExceptAdmins();
    }

    public Object getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Transactional
    public void updateOrInsertRole(Role role) {
        roleRepository.updateOrInsert(role);
    }
}