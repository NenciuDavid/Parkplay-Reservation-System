package org.example.parkplayfinal.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user, "Obiectul User ar trebui să fie creat (nu null)");
        assertNull(user.getName(), "Numele ar trebui să fie null inițial");
    }

    @Test
    void testAllArgsConstructor() {
        User driver = new User("Eu", "eu@test.com", "pass123", "DRIVER");

        assertEquals("Eu", driver.getName());
        assertEquals("eu@test.com", driver.getEmail());
        assertEquals("pass123", driver.getPassword());
        assertEquals("DRIVER", driver.getRole());
    }

    @Test
    void testSettersAndGetters() {
        User manager = new User();
        manager.setId(10L);
        manager.setRole("MANAGER");
        manager.setEmail("manager@test.com");

        assertEquals(10L, manager.getId());
        assertEquals("MANAGER", manager.getRole());
        assertNull(manager.getLicensePlate(), "Managerul nu ar trebui să aibă număr de înmatriculare setat");
    }
}