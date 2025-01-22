package gr.hua.dit.dis.ergasia.controllers;

import gr.hua.dit.dis.ergasia.entities.Role;
import gr.hua.dit.dis.ergasia.entities.User;
import gr.hua.dit.dis.ergasia.repositories.RoleRepository;
import gr.hua.dit.dis.ergasia.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    private UserService userService;

    private RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        // Create a new user
        User user = new User();
        // Add the user and the available roles to the model
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model model){
        System.out.println("Roles: "+user.getRoles());
        // Save the user
        Integer id = userService.saveUser(user);
        String message = "User '"+id+"' saved successfully !";
        model.addAttribute("msg", message);
        return "index";
    }

    @GetMapping("/users")
    public String showUsers(Model model){
        // Add all users and the available roles to the model
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @GetMapping("/user/{user_id}")
    public String showUser(@PathVariable Long user_id, Model model){
        // Add the user and the available roles to the model
        model.addAttribute("user", userService.getUser(user_id));
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/user";
    }

    @PostMapping("/user/{user_id}")
    public String saveStudent(@PathVariable Long user_id, @ModelAttribute("role") Role newRole,@ModelAttribute("user") User user, Model model) {
        User the_user = (User) userService.getUser(user_id);
        // Update the user's information
        the_user.setEmail(user.getEmail());
        the_user.setUsername(user.getUsername());
        the_user.setDescription(user.getDescription());
        Set<Role> roleSet = new HashSet<Role>();
        the_user.setRoles(roleSet);
        the_user.setSelectedRole(user.getSelectedRole());
        userService.updateUser(the_user);
        // Add all users and the available roles to the model
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @GetMapping("/user/role/delete/{user_id}/{role_id}")
    public String deleteRolefromUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        // Remove the role from the user
        user.getRoles().remove(role);
        System.out.println("Roles: "+user.getRoles());
        userService.updateUser(user);
        // Add all users and the available roles to the model
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";

    }

    @GetMapping("/user/role/add/{user_id}/{role_id}")
    public String addRoletoUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        // Add the role to the user
        user.getRoles().add(role);
        System.out.println("Roles: "+user.getRoles());
        userService.updateUser(user);
        // Add all users and the available roles to the model
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";

    }




}