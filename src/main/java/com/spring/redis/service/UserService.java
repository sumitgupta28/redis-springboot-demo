package com.spring.redis.service;


import com.spring.redis.db.entity.AppUser;
import com.spring.redis.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @CachePut(value = "users", key = "#appUser.id")
    public AppUser createUser(AppUser appUser) {
        return userRepository.saveAndFlush(appUser);
    }

    @Cacheable(value = "users", key = "#id")
    public AppUser getUserById(Long id) {
        Optional<AppUser> userOptional = userRepository.findById(id);
        return userOptional.orElse(AppUser.builder().userName("NA").firstName("NA").lastName("NA").emailAdded("NA").build());
    }

    @CacheEvict(value = "users", key = "#id")
    public AppUser updateUserById(Long id, AppUser appUser) {
        log.info("updateUserById [userId] - {}", id);
        Optional<AppUser> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            AppUser existingAppUser = userOptional.get();
            log.info("existingAppUser [userId] - {}", existingAppUser.getId());
            existingAppUser.setUserName(appUser.getUserName());
            existingAppUser.setFirstName(appUser.getFirstName());
            existingAppUser.setLastName(appUser.getLastName());
            existingAppUser.setEmailAdded(appUser.getEmailAdded());
            return userRepository.save(existingAppUser);
        }
        return AppUser.builder().userName("NA").firstName("NA").lastName("NA").emailAdded("NA").build();
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
