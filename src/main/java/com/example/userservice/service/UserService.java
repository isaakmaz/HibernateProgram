package com.example.userservice.service;

import com.example.userservice.entity.User;

import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> findById(Long id);
    void update(User user);
    void delete(User user);
}