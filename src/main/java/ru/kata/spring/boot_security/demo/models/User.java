package ru.kata.spring.boot_security.demo.models;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints= @UniqueConstraint(columnNames={"id", "username"}))
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 30, message = "Поле должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Имя должно содержать только буквы")
    @Column(unique=true)
    private String username;

    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 30, message = "Поле должно содержать от 2 до 30 символов")
    private String password;

    @Size(min = 2, max = 30, message = "Поле должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Фамилия должна содержать только буквы")
    private String lastname;

    @NotNull(message = "Поле не должно быть пустым")
    @Min(value = 1, message = "Возраст должен быть от одного года")
    @Max(value = 150, message = "возраст не может быть больше 150 лет")
    private Integer age=1;

    @ManyToMany
    @JoinTable (name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(Long id, String username, String lastname, int age) {
        this.id = id;
        this.username = username;
        this.lastname = lastname;
        this.age = age;
    }

    public User() {

    }

    public User(String username, String password, String lastname, Integer age, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.lastname = lastname;
        this.age = age;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // методы UserDetails
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age && Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(lastname, user.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, lastname, age);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + username + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                '}';
    }
}