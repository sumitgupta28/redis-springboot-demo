package com.spring.redis.controller;

import com.spring.redis.db.entity.AppUser;
import com.spring.redis.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "user")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public AppUser getUser(@RequestParam(required = true) Long id)
    {
        return userService.getUserById(id);
    }

    @PutMapping
    public AppUser updateUser(@RequestParam(required = true) Long id, @RequestBody AppUser appUser)
    {
        AppUser user=userService.updateUserById(id, appUser);
        log.info("updateUser input userId {} , output userId {} ",id,user.getId());
        return user;
    }


    @PostMapping
    public AppUser createUser(@RequestBody AppUser appUser)
    {
       return userService.createUser(appUser);
    }

    @DeleteMapping
    public ResponseEntity<Void> createUser(@RequestParam(required = true) Long id)
    {
         userService.deleteUserById(id);
         return ResponseEntity.ok().build();
    }

}
