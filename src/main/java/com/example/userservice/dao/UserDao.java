package com.example.userservice.dao;

import com.example.userservice.entity.User;

import java.util.Optional;


public interface UserDao {

    // Метод для создания (возвращает созданного юзера)
    User save(User user);

    // Метод для поиска по ID (возвращает Optional)
    Optional<User> findById(Long id);

    // Метод для обновления
    void update(User user);

    // Метод для удаления
    void delete(User user);

}