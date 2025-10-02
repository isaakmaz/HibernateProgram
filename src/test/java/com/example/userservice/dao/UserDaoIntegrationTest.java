package com.example.userservice.dao;

import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.time.LocalDateTime;
import java.util.Optional;

@Testcontainers
class UserDaoIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static SessionFactory sessionFactory;
    private UserDao userDao; // Теперь это интерфейс, а не класс-реализация

    @BeforeAll
    static void setup() {
        // объект Configuration
        Configuration configuration = new Configuration();

        // Свойства
        configuration.setProperty("hibernate.connection.url", postgresqlContainer.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgresqlContainer.getUsername());
        configuration.setProperty("hibernate.connection.password", postgresqlContainer.getPassword());
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.driver_class", "org.postgresql.Driver");
        // Схема будет создаваться перед тестами и удаляться после.
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.show_sql", "true");

        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
        //  создал свежий экземпляр UserDao
    void init() {
        userDao = new UserDaoImpl(sessionFactory);
    }

    @Test
    void shouldSaveUserAndThenFindItById() {
        // Подготовка данных
        User newUser = new User();
        newUser.setName("Test User");
        newUser.setEmail("test@example.com");
        newUser.setAge(30);
        newUser.setCreatedAt(LocalDateTime.now());

        userDao.save(newUser);

        // Проверяем, что ID был присвоен после сохранения
        Assertions.assertNotNull(newUser.getId());

        Optional<User> foundUserOptional = userDao.findById(newUser.getId());

        // Проверяем, что пользователь был найден
        Assertions.assertTrue(foundUserOptional.isPresent());

        // Проверяем, что данные в найденном пользователе соответствуют тому, что мы сохраняли
        User foundUser = foundUserOptional.get();
        Assertions.assertEquals("Test User", foundUser.getName());
        Assertions.assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void shouldUpdateUser() {
        User newUser = new User();
        newUser.setName("Оригинал");
        newUser.setEmail("test12@example.com");
        newUser.setAge(30);
        newUser.setCreatedAt(LocalDateTime.now());

        userDao.save(newUser);

        newUser.setName("Обновлено");

        newUser.setAge(31);

        userDao.update(newUser);

        // Вытаскиваю свежую копию из БД
        Optional<User> updatedUserOptional = userDao.findById(newUser.getId());
        // пользователь нашелся?
        Assertions.assertTrue(updatedUserOptional.isPresent());

        User updatedUser = updatedUserOptional.get();
        Assertions.assertEquals("Обновлено", updatedUser.getName());
        Assertions.assertEquals(31, updatedUser.getAge());
    }

    @Test
    void shouldDeleteUser() {
        // Подготовка данных
        User newUser = new User();
        newUser.setName("Елизаветта Карпова");
        newUser.setEmail("test45@example.com");
        newUser.setAge(21);
        newUser.setCreatedAt(LocalDateTime.now());

        userDao.save(newUser);

        // Проверяем, что ID был присвоен после сохранения
        Assertions.assertNotNull(newUser.getId());

        Optional<User> foundUserOptional = userDao.findById(newUser.getId());

        userDao.delete(newUser);

        // Попробуем найти удаленного пользователя
        Optional<User> deletedUserOptional = userDao.findById(newUser.getId());
        // пользователь нашелся?
        Assertions.assertTrue(deletedUserOptional.isEmpty());

    }

}