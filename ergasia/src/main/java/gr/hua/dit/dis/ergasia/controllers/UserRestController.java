package gr.hua.dit.dis.ergasia.controllers;

import gr.hua.dit.dis.ergasia.entities.Role;
import gr.hua.dit.dis.ergasia.entities.User;
import gr.hua.dit.dis.ergasia.repositories.RoleRepository;
import gr.hua.dit.dis.ergasia.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserRestController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/register")
    public User createUserTemplate() {
        User user = new User();
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        return user;
    }

    @PostMapping("/save")
    public String saveUser(@RequestBody User user) {
        Long id = userService.saveUser(user);
        return "User '" + id + "' saved successfully!";
    }

    @GetMapping
    public List<User> getAllUsers() {
        return (List<User>) userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return (User) userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable Long userId, @RequestBody User user) {
        User existingUser = (User) userService.getUser(userId);

        if (existingUser == null) {
            return "User not found";
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setDescription(user.getDescription());
        existingUser.setRoles(new HashSet<>(user.getRoles()));
        existingUser.setSelectedRole(user.getSelectedRole());

        userService.updateUser(existingUser);
        return "User updated successfully!";
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public String removeRoleFromUser(@PathVariable Long userId, @PathVariable Integer roleId) {
        User user = (User) userService.getUser(userId);
        Role role = roleRepository.findById(roleId).orElse(null);

        if (user == null || role == null) {
            return "User or role not found";
        }

        user.getRoles().remove(role);
        userService.updateUser(user);
        return "Role removed from user successfully!";
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public String addRoleToUser(@PathVariable Long userId, @PathVariable Integer roleId) {
        User user = (User) userService.getUser(userId);
        Role role = roleRepository.findById(roleId).orElse(null);

        if (user == null || role == null) {
            return "User or role not found";
        }

        user.getRoles().add(role);
        userService.updateUser(user);
        return "Role added to user successfully!";
    }
}
