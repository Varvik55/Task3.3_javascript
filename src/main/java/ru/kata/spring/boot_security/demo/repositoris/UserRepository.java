package ru.kata.spring.boot_security.demo.repositoris;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.models.User;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "select u from User u left join fetch u.roles where u.username=:username")
    User getUserByLogin(@Param("username") String username);
}
