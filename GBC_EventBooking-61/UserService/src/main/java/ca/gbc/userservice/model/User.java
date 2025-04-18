package ca.gbc.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="t_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userId;

    private String name;
    private String email;
    private String role;

    @Column(name = "usertype")
    private String userType;    // i.e., Student, Staff, or Faculty

}
