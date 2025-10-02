package com.example.userservice;

import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.entity.User;

import java.util.Optional;
import java.util.Scanner;

import com.example.userservice.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final UserDao userDao = new UserDaoImpl(HibernateUtil.getSessionFactory());


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            logger.info("Введите команду (create, read, update, delete, exit):");
            String command = scanner.nextLine();

            switch (command) {
                case "create":
                    logger.info("Введите Имя и Фамилию:");
                    String name = scanner.nextLine();
                    if (name.isBlank()) {
                        logger.warn("Имя не может быть пустым. Операция отменена.");
                        break;
                    }

                    logger.info("Введите email:");
                    String email = scanner.nextLine();
                    if (email.isBlank() || !email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
                        logger.warn("Неверный формат email. Операция отменена.");
                        break;
                    }

                    logger.info("Введите возраст:");
                    String ageString = scanner.nextLine();
                    int ageAsInt;
                    try {
                        ageAsInt = Integer.parseInt(ageString);
                        if (ageAsInt < 0 || ageAsInt > 120) {
                            logger.warn("Возраст должен быть в разумных пределах (от 0 до 120). Операция отменена.");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        logger.warn("Неверный формат возраста. Введите целое число. Операция создания отменена.");
                        break;
                    }

                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setEmail(email);
                    newUser.setAge(ageAsInt);
                    newUser.setCreatedAt(java.time.LocalDateTime.now());

                    userDao.save(newUser);

                    logger.info("Пользователь успешно создан: " + newUser);
                    break;
                case "read":
                    logger.info("Введите ID пользователя для поиска:");
                    String idString = scanner.nextLine();
                    Long idAsLong;
                    try {
                        idAsLong = Long.parseLong(idString);
                    } catch (NumberFormatException e) {
                        logger.warn("Неверный формат id. Введите целое число. Операция создания отменена.");
                        break;
                    }
                    Optional<User> foundUser = userDao.findById(idAsLong);
                    if (foundUser.isPresent()) {
                        logger.info("Найденный пользователь: " + foundUser.get());
                    } else {
                        logger.info("Пользователь с ID " + idAsLong + " не найден.");
                    }
                    break;
                case "update":
                    logger.info("Введите ID пользователя для обновления:");
                    String idUpdateString = scanner.nextLine();
                    Long idToUpdate;
                    try {
                        idToUpdate = Long.parseLong(idUpdateString);
                    } catch (NumberFormatException e) {
                        logger.warn("Неверный формат ID. Операция отменена.");
                        break;
                    }
                    Optional<User> userToUpdateOptional = userDao.findById(idToUpdate);
                    if (userToUpdateOptional.isEmpty()) {
                        logger.warn("Пользователь с ID " + idToUpdate + " не найден.");
                        break;
                    }
                    User userToUpdate = userToUpdateOptional.get();
                    logger.info("Введите новое имя (или оставьте пустым, чтобы не менять):");
                    String newName = scanner.nextLine();
                    if (!newName.isBlank()) {
                        userToUpdate.setName(newName);
                    }

                    logger.info("Введите новый возраст (или оставьте пустым, чтобы не менять):");
                    String newAgeString = scanner.nextLine();
                    if (!newAgeString.isBlank()) {
                        try {
                            int newAge = Integer.parseInt(newAgeString);
                            userToUpdate.setAge(newAge);
                        } catch (NumberFormatException e) {
                            logger.warn("Неверный формат возраста. Возраст не будет изменен.");
                        }
                    }
                    userDao.update(userToUpdate);
                    logger.info("Пользователь обновлен: " + userToUpdate);
                    break;

                case "delete":
                    logger.info("Введите ID пользователя для удаления:");
                    String idDeleteString = scanner.nextLine();
                    long idToDelete;
                    try {
                        idToDelete = Long.parseLong(idDeleteString);
                    } catch (NumberFormatException e) {
                        logger.warn("Неверный формат ID. Операция отменена.");
                        break;
                    }
                    Optional<User> userToDeleteOptional = userDao.findById(idToDelete);
                    if (userToDeleteOptional.isEmpty()) {
                        logger.warn("Пользователь с ID " + idToDelete + " не найден.");
                    } else {
                        userDao.delete(userToDeleteOptional.get());
                        logger.info("Пользователь с ID " + idToDelete + " успешно удален.");
                    }
                    break;

                case "exit":
                    logger.info("Завершение работы программы.");
                    scanner.close();
                    HibernateUtil.shutdown();
                    return;

                default:
                    logger.warn("Неизвестная команда. Пожалуйста, используйте create, read, update, delete или exit.");
                    break;
            }
        }
    }
}