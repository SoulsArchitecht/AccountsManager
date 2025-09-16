package ru.sshibko.AccountsManager.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
@Builder(toBuilder = true)
public class UserInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "birth_date", nullable = true)
    private LocalDate birthDate;

    @Column(name = "country", nullable = true)
    private String country;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @Column(name = "optional_email", nullable = true)
    private String optionalEmail;

    @Column(name = "avatar_url", nullable = true)
    private String avatarUrl;

    @CreationTimestamp
    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;
}
