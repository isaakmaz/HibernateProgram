package com.example.userservice.dao;

import com.example.userservice.entity.User;
import com.example.userservice.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Сохранение user которого передаем
            session.persist(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при сохранении пользователя " + user, e);
        }
        // Возвращаем user, но теперь внутри будет id
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            // ловим логи
            logger.error("Ошибка при поиске пользователя по id: " + id, e);  // В случае ошибки вернем пустой Optional
            return Optional.empty();
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при обновлении пользователя " + user, e);
        }
    }

    @Override
    public void delete(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.remove(user);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при удалении пользователя " + user, e);
        }
    }

}
