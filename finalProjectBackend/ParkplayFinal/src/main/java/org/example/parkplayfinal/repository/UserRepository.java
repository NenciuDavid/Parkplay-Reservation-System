package org.example.parkplayfinal.repository;

import org.example.parkplayfinal.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**Repository responsabil cu gestionarea utilizatorilor (tabela users)*/
@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    /**constructor*/
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**Mapper pentru useri*/
    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setLicensePlate(rs.getString("license_plate"));
        return u;
    };

    /**Functie responsabila cu salvarea unui utilizator nou in baza de date*/
    public int save(User user) {
        return jdbcTemplate.update(
                "INSERT INTO users (name, email, password, role, license_plate) VALUES (?, ?, ?, ?, ?)",
                user.getName(), user.getEmail(), user.getPassword(), user.getRole(), user.getLicensePlate()
        );
    }

    /**Functie responsabila cu gasirea unui user dupa email in baza de date*/
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.query(sql, userMapper, email).stream().findFirst();
    }

    /**Functie responsabila cu gasirea unui user dupa id in baza de date*/
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, userMapper, id).stream().findFirst();
    }
}