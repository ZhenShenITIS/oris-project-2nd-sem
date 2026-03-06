package itis.repository;

import itis.model.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final SessionFactory sessionFactory;

    public List<User> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM User", User.class)
                .getResultList();
    }
}
