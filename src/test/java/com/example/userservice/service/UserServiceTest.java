package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

// расширение Mockito для JUnit 5
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDaoMock;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        // Создал пустышку
        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setName("Тестовый Юзер");
        fakeUser.setEmail("test@mockito.com");

        //  Если userDaoMock будет вызван метод findById с аргументом 1L,
        //  Тогда вернуть Optional, содержащий нашего fakeUser
        when(userDaoMock.findById(1L)).thenReturn(Optional.of(fakeUser));

        // Вызываю метод у нашего реального сервиса
        Optional<User> result = userService.findById(1L);

        // Проверяю что результат не пустой
        Assertions.assertTrue(result.isPresent());
        // Проверяю, что имя пользователя в результате совпадает с тем, что мы ожидали
        Assertions.assertEquals("Тестовый Юзер", result.get().getName());
        // Убедимся, что метод findById у нашего мока был вызван 1 раз.
        Mockito.verify(userDaoMock).findById(1L);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenUserDoesNotExist() {
        // На вызов findById должен быть пустой Optional
        when(userDaoMock.findById(99L)).thenReturn(Optional.empty());

        // Вызываю метод и сохраняю его результат.
        Optional<User> result = userService.findById(99L);
        // Результат - это действительно пустой Optional?
        Assertions.assertTrue(result.isEmpty());

        Mockito.verify(userDaoMock).findById(99L);
    }

    @Test
    void save_shouldReturnUserWithId_whenUserIsSavedSuccessfully() {
        // Создал пустышку без id
        User userToSave = new User();
        userToSave.setName("Тестовый Юзер");
        userToSave.setEmail("test@mockito.com");
        // Создал пустышку с id, который будет из DAO
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Тестовый Юзер");
        savedUser.setEmail("test@mockito.com");
        // Мок возвращает результат редактирования
        Mockito.when(userDaoMock.save(userToSave)).thenReturn(savedUser);

        User result = userService.save(userToSave);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId()); // Проверяю, что ID действительно присвоился
        Assertions.assertEquals("Тестовый Юзер", result.getName()); // Проверяю остальные поля

        // Проверяю, что сервис вызвал DAO ровно 1 раз
        Mockito.verify(userDaoMock).save(userToSave);

    }

    @Test
    void update_shouldCallDaoUpdate_whenUserIsUpdated() {
        // Создал пустышку
        User userToUpdate = new User();
        userToUpdate.setId(1L);
        userToUpdate.setName("Updated Name");
        // Вызвал service.update() с этим пользователем.
        userService.update(userToUpdate);
        // Проверяю, что сервис вызвал DAO ровно 1 раз
        Mockito.verify(userDaoMock).update(userToUpdate);
    }

    @Test
    void delete_shouldCallDaoDelete_whenUserIsDeleted() {
        // Создал пустышку
        User userToDelete = new User();
        userToDelete.setId(1L);
        userToDelete.setName("Deleted Name");
        // Вызвал service.delete() с этим пользователем.
        userService.delete(userToDelete);
        // Проверяю, что сервис вызвал DAO ровно 1 раз
        Mockito.verify(userDaoMock).delete(userToDelete);
    }

}
