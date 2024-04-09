package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositoris.UserRepository;

import java.util.List;
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;


    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(getUserById(user.getId()).getPassword());
        }
        userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);

    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByLogin(String username) {
        return userRepository.getUserByLogin(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getUserByLogin(username);
    }
    public boolean isUserAdmin(String username) {

        User user = userRepository.getUserByLogin(username);
        if (user != null) {

            return user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        }
        return false;
    }
}
