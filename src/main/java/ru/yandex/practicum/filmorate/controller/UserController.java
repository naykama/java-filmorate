package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail().isBlank() || user.getEmail().indexOf('@') == -1) {
            log.error("User create failed. Incorrect email");
            throw new ValidationException("Incorrect email");
        } else if (user.getLogin().isBlank() || user.getLogin().indexOf(' ') != -1) {
            log.error("User create failed. Incorrect login");
            throw new ValidationException("Incorrect login");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("User create failed. Incorrect date of birth {}", user.getBirthday());
            throw new ValidationException("Incorrect date of birth");
        } else {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(id);
            users.put(id++, user);
            log.debug("User {} was created", user);
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (users.containsKey(user.getId())){
            users.put(user.getId(), user);
        } else {
            log.error("User update failed. There is not user with such id");
            throw new ValidationException("There is not user with such id");
        }
        return user;
    }

    @GetMapping
    public List<User> get() {
        return new ArrayList<>(users.values());
    }
}
