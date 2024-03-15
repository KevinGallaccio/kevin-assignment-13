package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getCreateUser(ModelMap model) {

        model.put("user", new User());

        return "register";
    }

    @PostMapping("/register")
    public String postCreateUser(User user) {
        Address newAddress = new Address();
        newAddress.setUser(user);
        userService.saveAddress(newAddress);
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String getAllUsers(ModelMap model) {
        Set<User> users = userService.findAllUsersWithAccountsAndAddresses();

        model.put("users", users);
        if (users.size() == 1) {
            model.put("user", users.iterator().next());
        }

        return "users";
    }

    @GetMapping("/users/{userId}")
    public String getOneUser(ModelMap model, @PathVariable Long userId) {
        User user = userService.findById(userId);
        model.put("users", Arrays.asList(user));
        model.put("user", user);
        Address address = user.getAddress();
        model.put("address", address);
        return "users";
    }

    @PostMapping("/users/{userId}")
    public String postOneUser(User user, Address address) {
        User originalUser = userService.findById(user.getUserId());
        originalUser.setUsername(user.getUsername());
        originalUser.setName(user.getName());
        if (!user.getPassword().isEmpty()) {
            originalUser.setPassword(user.getPassword());
        }
        Address originalAddress = originalUser.getAddress();
        originalAddress.setAll(address);
        userService.saveAddress(originalAddress);
        userService.saveUser(originalUser);
        return "redirect:/users/" + originalUser.getUserId();
    }

    @PostMapping("/users/{userId}/delete")
    public String deleteOneUser(@PathVariable Long userId) {
        userService.delete(userId);
        return "redirect:/users";
    }


}
