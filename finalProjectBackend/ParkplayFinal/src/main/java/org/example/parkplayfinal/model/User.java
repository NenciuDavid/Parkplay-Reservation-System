package org.example.parkplayfinal.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**Reprezinta un utilizator care poate avea rol de parking manager sau de driver*/
@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String licensePlate;
    private List<ParkingLot> managedParkings = new ArrayList<>();

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}