package com.galvanize.springcrudcheckpoint;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    public Iterable<User> getAll() {
        return this.repository.findAll();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return this.repository.save(user);
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable Long id) {
        return this.repository.findById(id).get();
    }

    @PatchMapping("/users/{id}")
    public User patchById(@PathVariable Long id, @RequestBody User user) {
        User updateUser = this.repository.findById(id).get();
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            updateUser.setPassword(user.getPassword());
        }
        return this.repository.save(updateUser);
    }

    @DeleteMapping ("/users/{id}")
    public Map<String, Long> deleteUser(@PathVariable Long id) {
        this.repository.deleteById(id);
        Map<String, Long> result = new HashMap<>();
        result.put("count", this.repository.count());
        return result;
    }

    @PostMapping("/users/authenticate")
    public Map<String, Object> logIn(@RequestBody User user) {
        User login = this.repository.findByEmail(user.getEmail());
        Map<String, Object> result = new HashMap<>();
        if (login.getPassword().equals(user.getPassword())) {
            result.put("authenticated", true);
            result.put("user", login);
        } else {
            result.put("authenticated", false);
        }
        return result;
    }
}
