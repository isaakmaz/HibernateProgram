package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    // Конструктор для внедрения зависимости
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }


    @Override
    public Optional<User> findById(Long id) {
       return userDao.findById(id);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(User user) {
        userDao.delete(user);
    }
}
